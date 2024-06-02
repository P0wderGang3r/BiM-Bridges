package org.granat.processors.helpers;

import org.granat.processors.helpers.HelperHeightMap;
import org.granat.processors.helpers.IHelper;
import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
2.2.4 Метод  – поиск области нахождения балок пролётов моста
Балки пролётных соединений моста располагаются в том месте, где находится наибольшая концентрация точек в горизонтальном срезе, ввиду чего можно обеспечить как ускорение дальнейших вычислений, так и отсечь не релевантные точки пространства путём поиска самого верхнего среза наибольшего веса по оси Z.

2.2.4.1 Дополнительные требования
·	Выполнено преобразование – поворот облака точек

2.2.4.2 Входные данные
·	Множество точек
·	Шаг перемещения срезов в пространстве
·	Допустимый разброс точек в срезе пространства

2.2.4.3 Выходные данные
·	Размеченное множество точек

2.2.4.4 Алгоритм
·	Совершается проход по оси пространства Z:
o	совершается проход по множеству точек пространства:
§	если точка лежит в области допустимого разброса точек в срезе пространства, вес текущего среза увеличивается на единицу;
·	вычисляем среднее абсолютное отклонение для полученного множества весов срезов пространства;
·	самое верхнее отклонение будет являться искомым множеством, в котором располагаются балки мостового сооружения.
 */
/* TODO:
    Данный класс-помощник по предложенной оси составляет вектор плотностей.
    Далее с помощью среднего абсолютного отклонения вычисляются выбросы.
    Все выбросы считаются искомыми поверхностями.
    На выходе - верхние и нижние границы искомых поверхностей.
 */
public class HelperDensity implements IHelper {

    @Override
    public Map<String, Double> run(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {
        if (parameters.get("length") == null ||
                parameters.get("axis") == null ||
                parameters.get("axis-col") == null ||
                parameters.get("axis-row") == null) return null;

        //Высота вектора
        int length = parameters.get("length").intValue();
        //Номер измерения, с которого снимаются значения для матрицы
        int axis = parameters.get("axis").intValue();

        Map<String, Double> vector = new HashMap<>();

        pointsStreamSupplier.get().forEach(
                point -> {
                    //Ставим в соответствие координате соответствующее место в векторе по оси axis от 0 до length
                    int current = (int) ((point.getCoordinates()[axis] + 1) * length);
                    vector.putIfAbsent("" + current, 0.0);
                    //Увеличиваем вес на единицу
                    vector.put("" + current, vector.get("" + current) + 1.0);
                });

        return vector;
    }
}