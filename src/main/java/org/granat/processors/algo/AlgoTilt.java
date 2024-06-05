package org.granat.processors.algo;

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
public class AlgoTilt {

    private static Double getMaxHeight(Map<String, Double> matrix, int rows, int cols) {
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

    private static Map<String, Double> buildRotation(Map<String, Double> matrix, Double maxHeight, int rows, int cols) {
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
     * @param pointsStreamSupplier потоки точек пространства.
     * @param parameters "epsilon" - разброс точек от центра среза; "delta" - изменение координаты среза; "bound" - граничное значение пространства.
     */
    public static void run(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {
        //Количество строк в матрице
        int rows = parameters.get("rows").intValue();
        //Количество колонок в матрице
        int cols = parameters.get("cols").intValue();

        Map<String, Double> matrix = new HashMap<>();

        //Фиксируем точку максимальной высоты
        Double maxHeight = getMaxHeight(matrix, rows, cols);

        //Создаётся карта высот
        //Создаётся карта изменения высот
        //Создаётся карта классов высот
        //Выбирается первый выброс сверху из всех классов карты высот

        //На этом этапе вычисляется плоскость, проходящая через множество точек
        Map<String, Double> surface = new HashMap<>();

        //Затем совершается поиск угла наклона плоскости по осям oX и oY
        //Вычисляем угол по осям oX и oY вычисленной плоскости относительно оси oZ
        buildRotation(matrix, maxHeight, rows, cols);

        //Ну и наконец проводится поворот каждой точки на новый угол
    }
}
