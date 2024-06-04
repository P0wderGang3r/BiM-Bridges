package org.granat.processors.helpers.height_map.algo;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - разметка срезов на карте высот.
 */
public class HelperHeightGroupsMetadata {

    private static Map<String, Double> buildClassesMetadataMap(
            Map<String, Double> heightMapClassesMetadata,
            Map<String, Double> heightMapGroups) {
        Map<String, Double> groupsMetadata = new HashMap<>();
        groupsMetadata.put("groups", 0.0);

        //Проходимся по каждой записи о сопоставлении класса и его группы
        heightMapGroups.forEach((groupsKey, groupsValue) -> {
            //Проходимся по каждой записи о сопоставлении классов с их минимальными и максимальными значениями
            heightMapClassesMetadata.forEach((classesKey, classesValue) -> {
                //Если найдено минимальное значение класса, то ...
                if (classesKey.equals("class-min-" + groupsKey.split("-")[1])) {
                    //... добавляем элемент группы и ...
                    String currentGroup = "group-" + groupsValue.intValue();
                    groupsMetadata.putIfAbsent(currentGroup, 0.0);
                    groupsMetadata.put(currentGroup, groupsMetadata.get(currentGroup) + 1.0);

                    //... записываем минимальное значение между значением для группы и этим классом.
                    String currentValue = "group-min-" + groupsValue.intValue();
                    groupsMetadata.putIfAbsent(currentValue, classesValue);
                    groupsMetadata.put(currentValue, Math.min(groupsMetadata.get(currentValue), classesValue));
                } else
                //Если найдено максимальное значение класса, то ...
                if (classesKey.equals("class-max-" + groupsKey.split("-")[1])) {
                    //... добавляем элемент группы и ...
                    String currentGroup = "group-" + groupsValue.intValue();
                    groupsMetadata.putIfAbsent(currentGroup, 0.0);
                    groupsMetadata.put(currentGroup, groupsMetadata.get(currentGroup) + 1.0);

                    //... записываем максимальное значение между значением для группы и этим классом.
                    String currentValue = "group-max-" + groupsValue.intValue();
                    groupsMetadata.putIfAbsent(currentValue, classesValue);
                    groupsMetadata.put(currentValue, Math.max(groupsMetadata.get(currentValue), classesValue));
                }
            });
            //... и записываем максимальную группу в количество существующих групп
            groupsMetadata.put("groups", Math.max(groupsMetadata.get("groups"), groupsValue));
        });

        //Увеличиваем значение количества групп на 1 относительно максимального номера группы
        groupsMetadata.put("groups", groupsMetadata.get("groups") + 1.0);

        for (int index = 0; index < groupsMetadata.get("groups"); index++) {
            groupsMetadata.put("group-med-" + index,
                    (groupsMetadata.get("group-min-" + index) + groupsMetadata.get("group-max-" + index)) / 2);
        }

        return groupsMetadata;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {

        Map<String, Double> heightMapClassesMetadata = data.get("height-map-classes-metadata");
        Map<String, Double> heightMapGroups = data.get("height-map-groups");

        //Заполняем классы в обозначенных границах классов в карте высот
        Map<String, Double> heightMapGroupsMetadata = buildClassesMetadataMap(heightMapClassesMetadata, heightMapGroups);

        return heightMapGroupsMetadata;
    }
}