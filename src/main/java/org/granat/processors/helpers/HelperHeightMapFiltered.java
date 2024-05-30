package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - фильтрация выбросов на карте высот.
 */
public class HelperHeightMapFiltered implements IHelper {

    private void buildDeltaHeightMap(Map<String, Double> matrix, int rows, int cols) {

        //Преобразовываем карту высот в карту изменения высот
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                //Если в матрице есть точка в следующей строке, то ...
                if (matrix.get((row + 1) + "-" + col) != null) {
                    //... присваиваем изменение координаты по оси oY текущей точке.
                    matrix.put("delta-row-" + row + "-" + col,
                            matrix.get(row  + "-" + col) - matrix.get((row + 1) + "-" + col));
                    //Иначе считаем, что изменение равно null.
                }
                //Если в матрице есть точка в следующем столбце, то ...
                if (matrix.get(row + "-" + (col + 1)) != null) {
                    //... присваиваем изменение координаты по оси oX текущей точке.
                    matrix.put("delta-col-" + row + "-" + col,
                            matrix.get(row  + "-" + col) - matrix.get(row + "-" + (col + 1)));
                    //Иначе считаем, что изменение равно null.
                }
            }
        }
    }

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

    private void removeIgnored(Map<String, Double> matrix, int rows, int cols) {
        //Удаляем все отбракованные точки
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (matrix.get("ignored-" + row + "-" + col) != null) {
                    matrix.remove(row + "-" + col);
                    matrix.remove("ignored-" + row + "-" + col);
                    matrix.remove("delta-row-" + row + "-" + col);
                    matrix.remove("delta-col-" + row + "-" + col);
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
        int amount = matrix.size() - 2;

        if (amount <= 2) return null;

        //Вычисляем центральную тенденцию всех значений матрицы на основе среднего арифметическое
        AtomicReference<Double> tendency = new AtomicReference<>(0.0);
        matrix.forEach((key, value) -> tendency.getAndAccumulate(value, Double::sum));
        tendency.set(tendency.get() / amount);

        //Вычисляем среднее абсолютное отклонение для всех значений матрицы
        AtomicReference<Double> med = new AtomicReference<>(0.0);
        matrix.forEach((key, value) -> med.set(Math.abs(value - tendency.get())));
        med.set(med.get() / amount);

        //Преобразовываем карту высот в карту изменения высот
        buildDeltaHeightMap(matrix, rows, cols);

        //Помечаем выбросы в карте высот с помощью карты изменения высот
        buildCullingMap(matrix, med, rows, cols);

        //Вычисляем плоскость, проходящую через множество точек
        removeIgnored(matrix, rows, cols);

        return matrix;
    }
}
