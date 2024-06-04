package org.granat.processors.helpers.density_vector;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Данный класс-помощник по предложенной оси составляет вектор плотностей.
 * Далее с помощью среднего абсолютного отклонения вычисляются выбросы.
 * Все выбросы считаются искомыми поверхностями.
 * На выходе - верхние и нижние границы искомых поверхностей.
 */
public class HelperDensity {

    public static Map<String, Double> run(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Map<String, Double>> data) {
        Map<String, Double> metadata = data.get("metadata");

        if (metadata.get("length") == null ||
                metadata.get("axis") == null) return null;

        //Высота вектора
        int length = metadata.get("length").intValue();
        //Номер измерения, с которого снимаются значения для матрицы
        int axis = metadata.get("axis").intValue();

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
