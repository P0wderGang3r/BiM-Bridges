package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

/* TODO:
    Данный класс-помощник по предложенной оси составляет вектор плотностей.
    Далее с помощью среднего абсолютного отклонения вычисляются выбросы.
    Все выбросы считаются искомыми поверхностями.
    На выходе - верхние и нижние границы искомых поверхностей.
 */
public class HelperDensityVectorSlice {

    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Double> vector) {
        //Высота вектора
        int length = vector.get("length").intValue();

        if (length > 0) {
            vector.remove("length");
        } else {
            return null;
        }

        Map<String, Double> classes = new HashMap<>();
        int currentClass = 1;
        boolean isInClass = false;

        //Вычисляем среднее значение минимального и максимального элементов вектора
        AtomicReference<Double> max = new AtomicReference<>(0.0);
        AtomicReference<Double> min = new AtomicReference<>();
        vector.values().stream().findFirst().ifPresent(min::set);
        vector.forEach((key, value) -> {
            max.set(Math.max(max.get(), value));
            min.set(Math.min(max.get(), value));
        });
        AtomicReference<Double> med = new AtomicReference<>(0.0);
        med.set((max.get() + min.get()) / 2);

        //Размечаем классы. Для этого проходимся по вектору.
        for (int index = 0; index < length; index++) {
            //Если текущее значение существует ...
            if (vector.get("" + index) != null) {
                //... и если текущее значение больше среднего ...
                if (vector.get("" + index) > med.get()) {
                    if (!isInClass) isInClass = true;
                    //... то записываем текущее в текущий класс.
                    classes.put("" + index, (double) currentClass);
                } else {
                    //Иначе переходим к следующему классу.
                    if (isInClass) currentClass++;
                    //И запоминаем, что ходим не в рамках текущего класса.
                    isInClass = false;
                }
            }
        }

        //Восстанавливаем значение предполагаемой длины вектора.
        vector.put("length", (double) length);
        return classes;
    }
}
