package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - разметка срезов на карте высот.
 */
public class HelperHeightMapSlice implements IHelper {

    private void buildCullingMap(Map<String, Double> matrix, AtomicReference<Double> med, int rows, int cols) {

        //Помечаем выбросы в карте высот с помощью карты изменения высот
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                //Если в матрице есть изменение относительно точки в следующей строке, то ...
                if (matrix.get("delta-row-" + row + "-" + col) != null) {
                    //... если изменение больше, чем абсолютное отклонение, то бракуем.
                    if (matrix.get("delta-row-" + row + "-" + col) > med.get())
                        matrix.put("ignored-" + (row + 1) + "-" + col, 1.0);
                    //Если точка находится на уровне игнорируемой с некоторой погрешностью или ниже неё, то так же бракуем.
                    if (matrix.get("ignored-" + row + "-" + col) != null &&
                            matrix.get("delta-row-" + row + "-" + col) >= -(med.get() / 10.0)) {
                        matrix.put("ignored-" + (row + 1) + "-" + col, 1.0);
                    }
                }

                //Если в матрице есть изменение относительно точки в следующем столбце, то ...
                if (matrix.get("delta-col-" + row + "-" + col) != null) {
                    //... если изменение больше, чем абсолютное отклонение, то бракуем.
                    if (matrix.get("delta-col-" + row + "-" + col) > med.get())
                        matrix.put("ignored-" + row + "-" + (col + 1), 1.0);
                    //Если точка находится на уровне игнорируемой с некоторой погрешностью или ниже неё, то так же бракуем.
                    if (matrix.get("ignored-" + row + "-" + col) != null &&
                            matrix.get("delta-col-" + row + "-" + col) >= -(med.get() / 10.0)) {
                        matrix.put("ignored-" + row + "-" + (col + 1), 1.0);
                    }
                }
            }
        }
    }

    /**
     * @param matrix карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    @Override
    public Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Double> matrix) {
        //Количество строк в матрице
        int rows = matrix.get("rows").intValue();
        //Количество колонок в матрице
        int cols = matrix.get("cols").intValue();
        //Количество существующих точек в матрице
        int amount = matrix.get("amount").intValue();

        if (amount != 0) {
            matrix.remove("rows");
            matrix.remove("cols");
            matrix.remove("amount");
        } else {
            return null;
        }

        //TODO: доработки - а не будет ли лучше работать алгоритм, если он будет вычислять САО относительно карты изменений высот.

        //Вычисляем центральную тенденцию всех значений матрицы на основе среднего арифметическое
        AtomicReference<Double> tendency = new AtomicReference<>(0.0);
        matrix.forEach((key, value) -> tendency.getAndAccumulate(value, Double::sum));
        tendency.set(tendency.get() / amount);

        //Вычисляем среднее абсолютное отклонение для всех значений матрицы
        AtomicReference<Double> med = new AtomicReference<>(0.0);
        matrix.forEach((key, value) -> med.set(Math.abs(value - tendency.get())));
        med.set(med.get() / amount);

        //TODO: доработки - разделение по классам, если переход является слишком резким относительно предыдущей точки.

        //Помечаем выбросы в карте высот с помощью карты изменения высот
        buildCullingMap(matrix, med, rows, cols);

        matrix.put("rows", (double) rows);
        matrix.put("cols", (double) rows);
        matrix.put("amount", (double) rows);

        return matrix;
    }
}