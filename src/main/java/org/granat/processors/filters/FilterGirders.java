package org.granat.processors.filters;

import org.granat.processors.helpers.*;
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
    IHelper helperHeightMapClassesMetadata = HelperHeightMapClassesMetadata::run;

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
        data.remove("height-map-delta"); heightMapDelta = null;

        //Создаётся карта классов по карте высот
        Map<String, Double> heightMapClasses = helperHeightMapClasses.run(null, data);
        data.put("height-map-classes", heightMapClasses);
        data.remove("height-map-borders"); heightMapBorders = null;

        //Составляется карта метаданных классов (количество, минимумы, максимумы)
        Map<String, Double> heightMapClassesMetadata = helperHeightMapClassesMetadata.run(null, data);
        data.put("height-map-classes-metadata", heightMapClassesMetadata);

        //Выбирается множество нижних классов на приблизительно одинаковой высоте
        //Выбрать нижние поверхности балок (количество -> max != max)

        //Разметить точки в соответствии с извлечённой информацией

    }
}
