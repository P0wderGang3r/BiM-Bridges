package org.granat.processors.filters;

import org.granat.processors.helpers.IHelper;
import org.granat.processors.helpers.height_map.algo.*;
import org.granat.processors.helpers.height_map.base.HelperHeightMap;
import org.granat.processors.helpers.height_map.base.HelperHeightMapBorders;
import org.granat.processors.helpers.height_map.base.HelperHeightMapClasses;
import org.granat.processors.helpers.height_map.base.HelperHeightMapDelta;
import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FilterGirders {

    static IHelper helperHeightMap = HelperHeightMap::run;
    static IHelper helperHeightMapDelta = HelperHeightMapDelta::run;
    static IHelper helperHeightMapBorders = HelperHeightMapBorders::run;
    static IHelper helperHeightMapClasses = HelperHeightMapClasses::run;

    static IHelper helperHeightMapClassesMetadata = HelperHeightClassesMetadata::run;
    static IHelper helperHeightMapClassesMetadataFiltered = HelperHeightClassesMetadataFiltered::run;
    static IHelper helperHeightMapGroups = HelperHeightGroups::run;
    static IHelper helperHeightMapGroupsMetadata = HelperHeightGroupsMetadata::run;
    static IHelper helperHeightMapGroupsMetadataFiltered = HelperHeightGroupsMetadataFiltered::run;

    private static void filter(
            Supplier<Stream<Point>> pointsStreams,
            Map<String, Double> parameters,
            Map<String, Double> heightMap,
            Map<String, Double> heightMapClasses,
            Map<String, Double> heightMapClassesMetadata,
            Map<String, Double> heightMapGroups,
            Map<String, Double> heightMapGroupsMetadata
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
        //Нормализация
        double norm = parameters.get("norm");

        pointsStreams.get().forEach(point -> {
            int row = (int) ((point.getCoordinates()[axisRow] * norm + 1) / 2 * rows);
            int col = (int) ((point.getCoordinates()[axisCol] * norm + 1) / 2 * cols);

            //Если сопоставленной с классом искомой группы нет, пропускаем
            //Для удобства записываем текущий класс ...
            Double currentClass = heightMapClasses.get(row + "-" + col);
            if (currentClass != null) {
                //System.out.print("C: " + currentClass.intValue());
                //... и текущую группу...
                Double currentGroup = heightMapGroups.get("class-" + currentClass.intValue());
                if (currentGroup != null) {
                    //System.out.print(", G: " + currentGroup.intValue());
                    //... и текущие метаданные группы.
                    Double currentGroupMetadata = heightMapGroupsMetadata.get("group-med-" + currentGroup.intValue());
                    if (currentGroupMetadata != null) {
                        //System.out.print(", VAL: " + point.getCoordinates()[axisVal]);
                        //System.out.print(", MIN: " + heightMapClassesMetadata.get("class-min-" + currentClass.intValue()));
                        //System.out.print(", MAX: " + heightMapClassesMetadata.get("class-max-" + currentClass.intValue()));
                        //Если точка находится на совпадающих координатах, то присваиваем класс
                        if (heightMapClassesMetadata.get("class-min-" + currentClass.intValue()) <= point.getCoordinates()[axisVal] &&
                                heightMapClassesMetadata.get("class-max-" + currentClass.intValue()) >= point.getCoordinates()[axisVal] &&
                                heightMapGroupsMetadata.get("group-min-" + currentGroup.intValue()) <= point.getCoordinates()[axisVal] &&
                                heightMapGroupsMetadata.get("group-max-" + currentGroup.intValue()) >= point.getCoordinates()[axisVal]
                        ) {
                            point.setLowerGirderClassValue(currentClass.intValue());
                        }
                    }
                }
            }
            //System.out.println();
        });
    }

    /**
     *
     * @param pointsStreams облако точек
     * @param parameters "radius" - предельное расстояние между точками
     */
    public static void run(Supplier<Stream<Point>> pointsStreams, Map<String, Double> parameters) {
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

        //Составляется карта метаданных классов (количество, минимумы, максимумы)
        Map<String, Double> heightMapClassesMetadataFiltered = helperHeightMapClassesMetadataFiltered.run(null, data);
        data.put("height-map-classes-metadata", heightMapClassesMetadataFiltered);

        //Разбивается множество классов на группы по сходным высотам
        Map<String, Double> heightMapGroups = helperHeightMapGroups.run(null, data);
        data.put("height-map-groups", heightMapGroups);

        //Составляется карта метаданных групп классов (количество, минимумы, максимумы)
        Map<String, Double> heightMapGroupsMetadata = helperHeightMapGroupsMetadata.run(null, data);
        data.put("height-map-groups-metadata", heightMapGroupsMetadata);

        //Выбирается множество нижних классов на приблизительно одинаковой высоте
        //Выбрать нижние поверхности балок (количество -> max != max)
        Map<String, Double> heightMapGroupsMetadataFiltered = helperHeightMapGroupsMetadataFiltered.run(null, data);
        data.remove("height-map-groups-metadata"); heightMapGroupsMetadata = null; //Высвобождение памяти

        //Разметить точки в соответствии с извлечённой информацией
        filter(pointsStreams, parameters, heightMap, heightMapClasses, heightMapClassesMetadata, heightMapGroups, heightMapGroupsMetadataFiltered);
    }
}
