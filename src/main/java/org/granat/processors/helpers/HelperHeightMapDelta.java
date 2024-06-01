package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - вычисление карты высот.
 */
public class HelperHeightMapDelta implements IHelper {

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

        //Преобразовываем карту высот в карту изменения высот
        buildDeltaHeightMap(matrix, rows, cols);

        return matrix;
    }
}
