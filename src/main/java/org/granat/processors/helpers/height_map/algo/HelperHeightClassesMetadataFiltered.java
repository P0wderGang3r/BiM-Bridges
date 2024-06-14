package org.granat.processors.helpers.height_map.algo;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - фильтрация карты классов по количеству точек пространства.
 */
public class HelperHeightClassesMetadataFiltered {

    private static Map<String, Double> buildClassesMetadataMap(
            Map<String, Double> heightMapClasses) {
        Map<String, Double> heightMapClassesFiltered = new HashMap<>();

        //Вычисляем среднее значение минимального и максимального элементов вектора
        AtomicReference<Double> max = new AtomicReference<>(0.0);
        AtomicReference<Double> min = new AtomicReference<>(0.0);
        AtomicReference<Double> med = new AtomicReference<>(0.0);
        AtomicReference<Double> amount = new AtomicReference<>(0.0);
        heightMapClasses.values().stream().findFirst().ifPresent(min::set);
        heightMapClasses.entrySet().stream().filter(entry -> entry.getKey().split("-").length > 2 &&
                entry.getKey().split("-")[1].equals("amount")).forEach(entry -> {
            max.set(Math.max(max.get(), entry.getValue()));
            min.set(Math.min(max.get(), entry.getValue()));
            med.set(med.get() + entry.getValue());
            amount.set(amount.get() + 1.0);
        });
        med.set(med.get() / amount.get());

        //Фильтруем по количеству точек пространства - меньшее, чем среднее отсекается.
        for (int index = 0; index < heightMapClasses.get("classes"); index++) {
            if (heightMapClasses.get("class-amount-" + index) == null) continue;
            if (heightMapClasses.get("class-amount-" + index) <= med.get()) continue;

            heightMapClassesFiltered.put("class-amount-" + index, heightMapClasses.get("class-amount-" + index));
            heightMapClassesFiltered.put("class-max-" + index, heightMapClasses.get("class-max-" + index));
            heightMapClassesFiltered.put("class-min-" + index, heightMapClasses.get("class-min-" + index));
        }

        heightMapClassesFiltered.put("classes", heightMapClasses.get("classes"));

        return heightMapClassesFiltered;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> heightMapClassesMetadata = data.get("height-map-classes-metadata");

        Map<String, Double> heightMapClassesMetadataFiltered = buildClassesMetadataMap(heightMapClassesMetadata);

        return heightMapClassesMetadataFiltered;
    }
}