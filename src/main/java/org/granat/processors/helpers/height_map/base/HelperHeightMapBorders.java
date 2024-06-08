package org.granat.processors.helpers.height_map.base;

import org.granat.scene.objects.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - разметка срезов на карте высот.
 */
public class HelperHeightMapBorders {

    /**
     * Алгоритм пометки границ в карте высот.
     */
    private static Map<String, Double> buildBordersMap(Map<String, Double> heightMapDelta, AtomicReference<Double> med, int rows, int cols) {
        Map<String, Double> bordersMap = new HashMap<>();

        //Помечаем границы классов в карте высот.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                //Если в матрице есть изменение относительно точки в следующей строке ...
                if (heightMapDelta.get("row-" + row + "-" + col) != null &&
                        //... и если изменение больше, чем абсолютное отклонение, ...
                        Math.abs(heightMapDelta.get("row-" + row + "-" + col)) >= med.get()) {
                    ///... то присваиваем текущему элементу матрицы и следующему значение границы.
                    bordersMap.put(row + "-" + col, 1.0);
                    bordersMap.put((row + 1) + "-" + col, 1.0);
                }

                //Если текущий элемент матрицы граничит с пустотой ...
                if (heightMapDelta.get("row-" + row + "-" + col) != null &&
                        heightMapDelta.get("row-" + (row + 1) + "-" + col) == null) {
                    ///... то присваиваем текущему элементу матрицы и следующему значение границы.
                    bordersMap.put(row + "-" + col, 1.0);
                }

                //Если в матрице есть изменение относительно точки в следующем столбце ...
                if (heightMapDelta.get("col-" + row + "-" + col) != null &&
                        //... и если изменение больше, чем абсолютное отклонение, ...
                        Math.abs(heightMapDelta.get("col-" + row + "-" + col)) >= med.get()) {
                    ///... то присваиваем текущему элементу матрицы и следующему значение границы.
                    bordersMap.put(row + "-" + col, 1.0);
                    bordersMap.put(row + "-" + (col + 1), 1.0);
                }

                //Если текущий элемент матрицы граничит с пустотой ...
                if (heightMapDelta.get("col-" + row + "-" + col) != null &&
                        heightMapDelta.get("col-" + row + "-" + (col + 1)) == null) {
                    ///... то присваиваем текущему элементу матрицы и следующему значение границы.
                    bordersMap.put(row + "-" + col, 1.0);
                }
            }
        }

        return bordersMap;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> metadata = data.get("metadata");
        Map<String, Double> heightMap = data.get("height-map");
        Map<String, Double> heightMapDelta = data.get("height-map-delta");

        //Количество строк в матрице
        int rows = metadata.get("rows").intValue();
        //Количество колонок в матрице
        int cols = metadata.get("cols").intValue();
        //Количество существующих точек в матрице
        int amount = metadata.get("amount").intValue();

        //Вычисляем центральную тенденцию всех значений матрицы на основе среднего арифметическое
        AtomicReference<Double> tendency = new AtomicReference<>(0.0);
        heightMapDelta.forEach((key, value) -> tendency.getAndAccumulate(value, Double::sum));
        System.out.println(tendency);
        tendency.set(tendency.get() / (amount * 2));
        System.out.println(amount);

        //Вычисляем среднее абсолютное отклонение для всех значений матрицы
        AtomicReference<Double> med = new AtomicReference<>(0.0);
        heightMapDelta.forEach((key, value) -> med.set(med.get() + Math.abs(value - tendency.get())));
        med.set(med.get() / (double) amount);
        System.out.println(med.get());

        //Помечаем границы классов в карте высот
        Map<String, Double> heightMapBorders = buildBordersMap(heightMapDelta, med, rows, cols);

        return heightMapBorders;
    }
}