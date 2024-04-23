package org.granat.filters;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FilterDensity {

    /**
     * Проход по каждой точке в срезе,
     * чтобы добавить значение количества точек среза каждому значению соответствующего параметра точки.
     */
    private static void updateParameterValue(
            Stream<Point> pointsStream,
            int dimension,
            double slice,
            double delta,
            long numberOfPointsInSlice
    ) {
        pointsStream.forEach(point -> {
            //Если точка находится в координатах среза
            if (point.getCoordinates()[dimension] > (slice - delta) &&
                    point.getCoordinates()[dimension] < (slice + delta)) {
                point.addDensityParameterValue(numberOfPointsInSlice);
            }
        });
    }

    /**
     * Проход по каждой точке в срезе, чтобы подсчитать их количество в срезе.
     * 
     * @return количество точек в срезе
     */
    private static long accumulateNumberOfPointsInSlice(
            Stream<Point> pointsStream,
            int dimension,
            double slice,
            double delta
    ) {
        var numberOfPointsInSlice = new Object() {
            public int value = 0;
        };
        pointsStream.forEach(point -> {
            //Если точка находится в координатах среза
            if (point.getCoordinates()[dimension] > (slice - delta) &&
                    point.getCoordinates()[dimension] < (slice + delta)) {
                numberOfPointsInSlice.value += 1;
            }
        });

        return numberOfPointsInSlice.value;
    }

    /**
     * Функция, считающая количество точек, попавших во множество срезов в каждом из измерений,
     * и записывающая "вес" каждой точки в её соответствующий параметр.
     * @param pointsStreamSupplier потоки точек пространства.
     * @param parameters "delta" - дельта оценивания; "bound" - граничное значение пространства.
     */
    public static void preprocess(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {
        //Устанавливаем в ноль значение параметра веса каждой точки
        pointsStreamSupplier.get().forEach(point -> point.setDensityParameterValue(0));

        //Перемещения среза за "такт"
        double delta = parameters.get("delta") / 2;
        double bound = parameters.get("bound");

        //Проходимся по каждому измерению dimension - оси oX, oY, oZ
        for (int dimension = 0; dimension < 3; dimension++) {
            //Проходимся по каждому срезу в рамках измерения dimension
            for (double slice = 0 - bound - delta;
                 slice < bound + delta; slice += delta) {
                //Ищем количество точек в срезе
                long numberOfPointsInSlice = accumulateNumberOfPointsInSlice(
                        pointsStreamSupplier.get(), dimension, slice, delta);
                //Записываем количество точек в срезе в соответствующий параметр каждой точки
                updateParameterValue(
                        pointsStreamSupplier.get(), dimension, slice, delta, numberOfPointsInSlice);
            }
        }
    }

    /**
     * @param pointsStreamSupplier поток точек пространства.
     * @param parameters "threshold" - граничное значение для фильтра.
     */
    public static void filter(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {
        long threshold = parameters.get("threshold").longValue();

        pointsStreamSupplier.get().forEach(
                point -> point.setDensityFilterValue(point.getDensityParameterValue() > threshold));
    }
}
