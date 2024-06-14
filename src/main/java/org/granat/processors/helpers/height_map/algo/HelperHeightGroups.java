package org.granat.processors.helpers.height_map.algo;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - группировка классов по высоте.
 */
public class HelperHeightGroups {

    private static Map<String, Double> buildClassesGroups(Map<String, Double> classesMetadata, int classes) {
        Map<String, Double> classesGroups = new HashMap<>();

        AtomicInteger nextGroup = new AtomicInteger();

        //Проходясь по всем индексам классов ...
        for (int index = 0; index < classes; index++) {
            if (classesMetadata.get("class-amount-" + index) == null) continue;
            //... и по индексам, которые нас в рамках алгоритма интересуют ...
            for (int indexComp = index + 1; indexComp < classes; indexComp++) {
                if (classesMetadata.get("class-amount-" + indexComp) == null) continue;
                //... если мы находим такое максимальное значение первого класса, которое лежит в пределах минимума и максимума второго класса ...
                if (classesMetadata.get("class-max-" + index) > classesMetadata.get("class-min-" + indexComp) &&
                        classesMetadata.get("class-max-" + index) < classesMetadata.get("class-max-" + indexComp) ||
                        //... или минимальное значение первого класса того же рода, то ...
                        classesMetadata.get("class-min-" + index) > classesMetadata.get("class-min-" + indexComp) &&
                                classesMetadata.get("class-min-" + index) < classesMetadata.get("class-max-" + indexComp)
                ) {
                    //... записываем этот класс и класс, с которым мы его сравнивали, в одну группу.
                    classesGroups.putIfAbsent("class-" + index, (double) nextGroup.getAndIncrement());
                    classesGroups.putIfAbsent("class-" + indexComp, classesGroups.get("class-" + index));
                }
            }
        }

        return classesGroups;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> heightMapClassesMetadata = data.get("height-map-classes-metadata");

        //Количество переданных классов
        int classes = heightMapClassesMetadata.get("classes").intValue();

        //Вычисляем группы классов близкой "высоты" на карте высот
        Map<String, Double> classesGroups = buildClassesGroups(heightMapClassesMetadata, classes);

        return classesGroups;
    }
}
