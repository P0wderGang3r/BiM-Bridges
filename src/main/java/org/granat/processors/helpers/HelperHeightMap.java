package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - генератор карты высот.
 */
public class HelperHeightMap {

    /**
     * @param pointsStreamSupplier Множество точек
     * @param parameters rows, cols - размерность карты высот;
     *                   axis-row, axis-col, axis-val - сопоставление осей X / Y / Z с их позицией и значением в карте высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {
        if (parameters.get("rows") == null || parameters.get("cols") == null ||
                parameters.get("axis-row") == null ||
                parameters.get("axis-col") == null ||
                parameters.get("axis-val") == null) return null;

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
        //Количество элементов матрицы
        AtomicInteger amount = new AtomicInteger();

        Map<String, Double> matrix = new HashMap<>();

        matrix.put("rows", (double) rows);
        matrix.put("cols", (double) cols);

        pointsStreamSupplier.get().forEach(
                point -> {
                    //Ставим в соответствие координате строку от 0 до rows
                    int row = (int) ((point.getCoordinates()[axisRow] + 1) * rows);
                    //Ставим в соответствие координате столбец от 0 до cols
                    int col = (int) ((point.getCoordinates()[axisCol] + 1) * cols);
                    //Увеличиваем количество элементов матрицы, если искомого элемента ещё не существует
                    if (matrix.get(row + "-" + col) == null) amount.getAndIncrement();
                    //Создаём новый элемент матрицы
                    matrix.put(row + "-" + col, Math.max(
                            matrix.get(row + "-" + col) == null ? -1 : matrix.get(row + "-" + col),
                            point.getCoordinates()[axisVal]));
                });

        matrix.put("amount", (double) amount.get());
        return matrix;
    }

}