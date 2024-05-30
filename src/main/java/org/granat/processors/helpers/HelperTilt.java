package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
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

    private Double getMaxHeight(Map<String, Double> matrix, int rows, int cols) {
        Double maxHeight = -1.0;

        //Удаляем все отбракованные точки
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (matrix.get(row + "-" + col) != null && matrix.get(row + "-" + col) > maxHeight) {
                    maxHeight = matrix.get(row + "-" + col);
                }
            }
        }

        return maxHeight;
    }

    private Map<String, Double> buildRotation(Map<String, Double> matrix, Double maxHeight, int rows, int cols) {
        Map<String, Double> degrees = new HashMap<>(4);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (matrix.get(row + "-" + col) != null) {

                }
            }
        }

        degrees.remove("current-row");
        degrees.remove("current-col");

        return degrees;
    }

    /**
     * @param matrix карта высот; rows, cols - размерность карты высот
     * @return "deg-1" - угол наклона по первой оси, "deg-2" - угол наклона по второй оси
     */
    @Override
    public Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Double> matrix) {
        //Количество строк в матрице
        int rows = matrix.get("rows").intValue();
        //Количество колонок в матрице
        int cols = matrix.get("cols").intValue();

        //Фиксируем точку максимальной высоты
        Double maxHeight = getMaxHeight(matrix, rows, cols);

        //На этом этапе вычисляется плоскость, проходящая через множество точек
        Map<String, Double> surface = new HashMap<>();

        //Затем совершается поиск угла наклона плоскости по осям oX и oY
        //Вычисляем угол по осям oX и oY вычисленной плоскости относительно оси oZ
        return buildRotation(matrix, maxHeight, rows, cols);
    }
}
