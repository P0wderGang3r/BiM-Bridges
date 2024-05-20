package org.granat.processors.helpers;

/*
2.2.2 Метод – вычисление карты высот
Большая часть алгоритмов в рамках разработанной последовательности решения
поставленной задачи использует вычисляемую карту высот,
отчего было решено унифицировать некоторые вычисления.

2.2.2.1 Входные данные
·	Множество точек
·	Номер оси, по которой проводится вычисление (X / Y / Z)
·	Направление (min -> max / min -> max)

2.2.2.2 Выходные данные
·	Матрица высот

2.2.2.3 Алгоритм
·	Строится матрица MxN, соответствующая координатной плоскости oXY и заполненная значениями -1;
·	выполняя проход по всем точками из входного множества точек,
    записывается максимальное (или минимальное) значение координаты Z в матрицу NxN
    по соответствующим ей координатам X и Y каждой текущей точки.

 */

import org.granat.scene.objects.Point;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HelperHeightMap {

    /**
     * @param pointsStreamSupplier Множество точек
     * @param parameters rows, cols, axis-row, axis-col, axis-val
     */
    public static Map<String, Object> generate(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {
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

        Map<String, Object> matrix = new HashMap<>(rows * cols);

        pointsStreamSupplier.get().forEach(
                point -> {
                    int row = (int) ((point.getCoordinates()[axisRow] + 1) * rows);
                    int col = (int) ((point.getCoordinates()[axisCol] + 1) * cols);
                    matrix.put("" + row + '-' + col, Math.max(
                            matrix.get("" + row + '-' + col) == null ? -1 : (double) matrix.get("" + row + '-' + col),
                            point.getCoordinates()[axisVal]));
                });

        return matrix;
    }

}