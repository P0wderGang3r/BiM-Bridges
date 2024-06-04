package org.granat.processors.filters;

import org.granat.processors.helpers.*;
import org.granat.processors.helpers.height_map.algo.HelperHeightGroupsMetadata;
import org.granat.processors.helpers.height_map.base.HelperHeightMap;
import org.granat.processors.helpers.height_map.base.HelperHeightMapBorders;
import org.granat.processors.helpers.height_map.base.HelperHeightMapClasses;
import org.granat.processors.helpers.height_map.base.HelperHeightMapDelta;
import org.granat.processors.helpers.height_map.algo.HelperHeightGroupsFiltered;
import org.granat.processors.helpers.height_map.algo.HelperHeightClassesMetadata;
import org.granat.processors.helpers.height_map.algo.HelperHeightGroups;
import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
    Кластеризация с помощью SVM.
    Уравнение выглядит так:
    Расстояние_Гиперплоскость_n -> max
    Расстояние_Гиперплоскость_n - Расстояние_Гиперплоскость_m = ±погрешность
 */
public class FilterGirders {

    IHelper helperHeightMap = HelperHeightMap::run;
    IHelper helperHeightMapDelta = HelperHeightMapDelta::run;
    IHelper helperHeightMapBorders = HelperHeightMapBorders::run;
    IHelper helperHeightMapClasses = HelperHeightMapClasses::run;

    IHelper helperHeightMapClassesMetadata = HelperHeightClassesMetadata::run;
    IHelper helperHeightMapGroups = HelperHeightGroups::run;
    IHelper helperHeightMapGroupsMetadata = HelperHeightGroupsMetadata::run;
    IHelper helperHeightMapGroupsFiltered = HelperHeightGroupsFiltered::run;

    private void filter(
            Supplier<Stream<Point>> pointsStreams,
            Map<String, Double> parameters,
            Map<String, Double> heightMap,
            Map<String, Double> heightMapClasses,
            Map<String, Double> heightMapGroupsFiltered
    ) {
        //Количество строк в матрице
        int rows = parameters.get("rows").intValue();
        //Количество колонок в матрице
        int cols = parameters.get("cols").intValue();
        //Номер измерения, соответствующего строке матрицы
        int axisRow = parameters.get("axis-row").intValue();
        //Номер измерения, соответствующего колонке матрицы
        int axisCol = parameters.get("axis-col").intValue();
        //Номер измерения, с которого снимаются значения для матрицы
        int axisVal = parameters.get("axis-val").intValue();

        pointsStreams.get().forEach(point -> {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (heightMap.get(row + "-" + col) == null) continue;
                    if (heightMapGroupsFiltered.get(
                            "class-" + heightMapClasses.get(row + "-" + col).intValue()) == null) continue;
                    //TODO: если точка находится на совпадающих координатах, то присваиваем класс
                }
            }
        });
    }

    /**
     *
     * @param pointsStreams облако точек
     * @param parameters "radius" - предельное расстояние между точками
     */
    public void run(Supplier<Stream<Point>> pointsStreams, Map<String, Double> parameters) {
        //Функция, которая проводит максимальное количество разделяющих линий в пространстве
        //Множество таких линий должно быть приблизительно равной удалённости между границами классов
        //На входе - множество точек после применения фильтра поиска пролётных строений моста

        Map<String, Map<String, Double>> data = new HashMap<>();
        data.put("metadata", parameters);

        //Создаётся карта высот
        Map<String, Double> heightMap = helperHeightMap.run(pointsStreams, data);
        data.put("height-map", heightMap);

        //Создаётся карта изменения высот
        Map<String, Double> heightMapDelta = helperHeightMapDelta.run(null, data);
        data.put("height-map-delta", heightMapDelta);

        //Создаётся карта классов по карте высот
        Map<String, Double> heightMapBorders = helperHeightMapBorders.run(null, data);
        data.put("height-map-borders", heightMapBorders);
        data.remove("height-map-delta"); heightMapDelta = null; //Высвобождение памяти

        //Создаётся карта классов по карте высот
        Map<String, Double> heightMapClasses = helperHeightMapClasses.run(null, data);
        data.put("height-map-classes", heightMapClasses);
        data.remove("height-map-borders"); heightMapBorders = null; //Высвобождение памяти

        //Составляется карта метаданных классов (количество, минимумы, максимумы)
        Map<String, Double> heightMapClassesMetadata = helperHeightMapClassesMetadata.run(null, data);
        data.put("height-map-classes-metadata", heightMapClassesMetadata);

        //Разбивается множество классов на группы по сходным высотам
        Map<String, Double> heightMapGroups = helperHeightMapGroups.run(null, data);
        data.put("height-map-groups", heightMapGroups);

        //Составляется карта метаданных групп классов (количество, минимумы, максимумы)
        Map<String, Double> heightMapGroupsMetadata = helperHeightMapGroupsMetadata.run(null, data);
        data.put("height-map-groups-metadata", heightMapGroupsMetadata);

        //Выбирается множество нижних классов на приблизительно одинаковой высоте
        //Выбрать нижние поверхности балок (количество -> max != max)
        Map<String, Double> heightMapGroupsFiltered = helperHeightMapGroupsFiltered.run(null, data);
        data.remove("height-map-groups"); heightMapGroups = null; //Высвобождение памяти
        data.remove("height-map-groups-metadata"); heightMapGroupsMetadata = null; //Высвобождение памяти

        //Разметить точки в соответствии с извлечённой информацией
        filter(pointsStreams, parameters, heightMap, heightMapClasses, heightMapGroupsFiltered);
    }
}
