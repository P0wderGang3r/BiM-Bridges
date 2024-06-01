package org.granat.processors.filters;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Класс, отвечающий за выбор одного из срезов, соответствующих горизонтальным опорным строениям мостовых сооружений.
 */
public class FilterSuperstructures {
    /**
     *
     * @param pointsStreams облако точек
     * @param parameters "radius" - предельное расстояние между точками
     */
    public void run(Supplier<Stream<Point>> pointsStreams, Map<String, Double> parameters) {
        //Создаётся вектор плотностей по оси oZ

        //Размечаются все точки по принадлежности выбросам в векторе плотностей

    }

}
