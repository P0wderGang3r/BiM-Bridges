package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
2.2.3 Метод – вычисление угла наклона по двум осям
Результаты сканирования могут содержать наклонённое по различным причинам облако точек, ввиду чего есть потребность в его выравнивании для дальнейшего анализа облака точек.

2.2.3.1 Дополнительные требования
·	Выполнена фильтрация – окрестности моста

2.2.3.2 Входные данные
·	Множество точек

2.2.3.3 Выходные данные
·	Множество точек

2.2.3.4 Алгоритм
·	Вычисляется карта высот (см. метод – вычисление карты высот): на вход подаётся отрицательное направление по оси Z;
·	для полученной матрицы вычисляется среднее абсолютное отклонение (игнорируя пустые значения),
·	на основании среднего абсолютного отклонения которого выполняется фильтрация выбросов;
·	вычисляется угол наклона по оставшимся значениям матрицы.

 */
public class HelperTilt implements IHelper {

    /**
     * Вычисление
     * @param matrix Карта высот
     * @return "deg-1" - угол наклона по первой оси, "deg-2" - угол наклона по второй оси
     */
    @Override
    public Map<String, Object> run(Supplier<Stream<Point>> ignored, Map<String, Double> matrix) {
        //Количество строк в матрице
        int rows = matrix.get("rows").intValue();
        //Количество колонок в матрице
        int cols = matrix.get("cols").intValue();
        //Количество существующих точек в матрице
        int amount = matrix.size();

        //Вычисляем центральную тенденцию всех значений матрицы на основе среднего арифметическое
        AtomicReference<Double> tendency = new AtomicReference<>(0.0);
        matrix.forEach((key, value) -> tendency.getAndAccumulate(value, Double::sum));
        tendency.set(tendency.get() / amount);

        //Вычисляем среднее абсолютное отклонение для всех значений матрицы
        AtomicReference<Double> med = new AtomicReference<>(0.0);
        matrix.forEach((key, value) -> med.set(Math.abs(value - tendency.get())));
        med.set(med.get() / amount);

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

        //Преобразовываем карту высот в карту изменения высот
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

        //Удаляем все отбракованные точки

        //Вычисляем плоскость, проходящую через множество точек

        //Вычисляем угол по осям oX и oY вычисленной плоскости относительно оси oZ

        return Map.of();
    }
}
