package org.granat.processors.helpers.height_map.algo;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - разметка срезов на карте высот.
 */
public class HelperHeightGroupsMetadata {

    private static final String groupMedRegex = "/(group-med-)[0-9]+/gm";

    private static Map<String, Double> buildGroupsMetadataSortedMap(
            Map<String, Double> heightMapGroupsMetadata) {
        Map<String, Double> groupsMetadataSorted = new HashMap<>();

        List<String> order = heightMapGroupsMetadata.entrySet().stream()
                .filter(entry -> Pattern.matches(groupMedRegex, entry.getKey()))
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();

        for (int index = 0; index < order.size(); index++) {
            groupsMetadataSorted.put("group-position-" + order.get(index).split("-")[2], (double) index);
        }

        return groupsMetadataSorted;
    }

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
        heightMapGroupsMetadata.putAll(buildGroupsMetadataSortedMap(heightMapGroupsMetadata));

        return heightMapGroupsMetadata;
    }
}