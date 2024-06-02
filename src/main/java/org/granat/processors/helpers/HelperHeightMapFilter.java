package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - очищение выбросов с карты высот.
 */
public class HelperHeightMapFilter implements IHelper {

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
        int amount = matrix.get("amount").intValue();

        if (amount != 0) {
            matrix.remove("rows");
            matrix.remove("cols");
            matrix.remove("amount");
        } else {
            return null;
        }

        //TODO: переработать таким образом, чтобы выбирался только искомый слой
        //Вычисляем плоскость, проходящую через множество точек
        removeIgnored(matrix, rows, cols);

        matrix.put("rows", (double) rows);
        matrix.put("cols", (double) rows);
        matrix.put("amount", (double) rows);

        return matrix;
    }
}
