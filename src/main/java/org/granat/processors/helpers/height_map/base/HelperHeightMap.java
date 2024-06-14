package org.granat.processors.helpers.height_map.base;

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
     * @param data rows, cols - размерность карты высот;
     *                   axis-row, axis-col, axis-val - сопоставление осей X / Y / Z с их позицией и значением в карте высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Map<String, Double>> data) {
        Map<String, Double> metadata = data.get("metadata");

        if (metadata.get("rows") == null || metadata.get("cols") == null ||
                metadata.get("axis-row") == null ||
                metadata.get("axis-col") == null ||
                metadata.get("axis-val") == null) return null;

        //Количество строк в матрице
        int rows = metadata.get("rows").intValue();
        //Количество колонок в матрице
        int cols = metadata.get("cols").intValue();
        //Номер измерения, соответствующего строке матрицы
        int axisRow = metadata.get("axis-row").intValue();
        //Номер измерения, соответствующего колонке матрицы
        int axisCol = metadata.get("axis-col").intValue();
        //Номер измерения, с которого снимаются значения для матрицы
        int axisVal = metadata.get("axis-val").intValue();
        //Нормализация
        double norm = metadata.get("norm");
        //Количество элементов матрицы
        AtomicInteger amount = new AtomicInteger(0);

        Map<String, Double> matrix = new HashMap<>();

        pointsStreamSupplier.get().forEach(
                point -> {
                    //Ставим в соответствие координате строку от 0 до rows
                    int row = (int) ((point.getCoordinates()[axisRow] * norm + 1) / 2 * rows);
                    //Ставим в соответствие координате столбец от 0 до cols
                    int col = (int) ((point.getCoordinates()[axisCol] * norm + 1) / 2 * cols);
                    //Увеличиваем количество элементов матрицы, если искомого элемента ещё не существует
                    if (matrix.get(row + "-" + col) == null) amount.getAndIncrement();
                    //Создаём новый элемент матрицы
                    matrix.put(row + "-" + col, Math.min(
                            matrix.getOrDefault(row + "-" + col, point.getCoordinates()[axisVal]),
                            point.getCoordinates()[axisVal]));
                });

        metadata.put("amount", (double) amount.get());

        return matrix;
    }

}