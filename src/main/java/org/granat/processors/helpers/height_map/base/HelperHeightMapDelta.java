package org.granat.processors.helpers.height_map.base;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - вычисление карты высот.
 */
public class HelperHeightMapDelta {

    private static Map<String, Double> buildHeightMapDelta(Map<String, Double> matrix, int rows, int cols) {
        Map<String, Double> heightMapDelta = new HashMap<>();

        //Преобразовываем карту высот в карту изменения высот
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                //Если в матрице есть точка в следующей строке, то ...
                if (matrix.get((row + 1) + "-" + col) != null) {
                    //... присваиваем изменение координаты по оси oY текущей точке.
                    heightMapDelta.put(row + "-" + col,
                            matrix.get(row  + "-" + col) - matrix.get((row + 1) + "-" + col));
                    //Иначе считаем, что изменение равно null.
                }
                //Если в матрице есть точка в следующем столбце, то ...
                if (matrix.get(row + "-" + (col + 1)) != null) {
                    //... присваиваем изменение координаты по оси oX текущей точке.
                    heightMapDelta.put(row + "-" + col,
                            matrix.get(row  + "-" + col) - matrix.get(row + "-" + (col + 1)));
                    //Иначе считаем, что изменение равно null.
                }
            }
        }

        return heightMapDelta;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> metadata = data.get("metadata");
        Map<String, Double> heightMap = data.get("height-map");

        //Количество строк в матрице
        int rows = metadata.get("rows").intValue();
        //Количество колонок в матрице
        int cols = metadata.get("cols").intValue();

        //Преобразовываем карту высот в карту изменения высот
        Map<String, Double> heightMapDelta = buildHeightMapDelta(heightMap, rows, cols);

        return heightMapDelta;
    }
}
