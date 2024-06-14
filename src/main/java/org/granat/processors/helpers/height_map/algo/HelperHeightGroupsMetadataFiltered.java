package org.granat.processors.helpers.height_map.algo;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - фильтрация групп классов по их высоте - если выше нормы, то срезаем.
 */
public class HelperHeightGroupsMetadataFiltered {

/*
    private static final String groupRegex = "/(group-)[0-9]+/gm";
    private static final String groupDataRegexStart = "/(group-)(.*)(-)";
    private static final String groupDataRegexEnd = "/gm";

    private static Map<String, Double> buildGroupsMetadataFilteredMap(
            Map<String, Double> heightMapGroupsMetadata,
            int sequentialNumber, int direction) {
        Map<String, Double> groupsMetadataFiltered = new HashMap<>();

        int groups = heightMapGroupsMetadata.get("groups").intValue();

        //Вычисляем среднее арифметическое всех существующих количеств классов, сопоставленных с группами.
        //Это будет значением для сравнения.
        double med = heightMapGroupsMetadata.entrySet().stream()
                .filter(entry -> Pattern.matches(groupRegex, entry.getKey()))
                .flatMapToDouble(entry -> DoubleStream.of(entry.getValue()))
                .sum() / (double) groups;

        //Проходимся в одну из двух сторон
        for (int index = direction == 1 ? 0 : groups - 1;
             direction == 1 ? index < groups : index >= 0;
             index += direction) {
            //Если следующего элемента нет, то выходим, думая, что ничего не нашли.
            if (index + direction == (direction == 1 ? groups : -1)) return null;
            //Если мы находим такую пару последовательных групп, у которой количество больше среднего, и...
            if (heightMapGroupsMetadata.get("group-" + index) > med &&
                    heightMapGroupsMetadata.get("group-" + (index + direction)) > med) {
                //... если эта пара групп стоит по соседству друг с другом, то ...
                if ((int) Math.abs(heightMapGroupsMetadata.get("group-position-" + index) -
                        heightMapGroupsMetadata.get("group-position-" + (index + direction))) != 1) continue;

                //... запоминаем текущую группу и ...
                AtomicInteger currentGroup = new AtomicInteger(index + direction * sequentialNumber);

                //... помещаем все значения, сопоставленные с текущей группой, в отфильтрованное множество значений групп, и ...
                groupsMetadataFiltered.putAll(heightMapGroupsMetadata.entrySet().stream()
                        .filter(entry -> Pattern.matches(
                                groupDataRegexStart + currentGroup.get() + groupDataRegexEnd, entry.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

                //... заканчиваем обход.
                break;
            }
        }

        groupsMetadataFiltered.put("groups", (double) groups);
        return groupsMetadataFiltered;
    }
 */

    private static final String groupMedRegex = "(group-med-)[0-9]+";
    private static final String groupMinRegex = "(group-min-)[0-9]+";
    private static final String groupMaxRegex = "(group-max-)[0-9]+";
    private static final String groupDataRegexStart = "(group-)(.*)(-)";

    private static Map<String, Double> buildGroupsMetadataFilteredMapLightweight(
            Map<String, Double> heightMapGroupsMetadata) {
        Map<String, Double> groupsMetadataFiltered = new HashMap<>();

        AtomicReference<Double> max = new AtomicReference<>(null);
        AtomicReference<Double> min = new AtomicReference<>(null);
        AtomicReference<Double> med = new AtomicReference<>(null);

        //Вычисляется самая высокая точек среди всех групп
        heightMapGroupsMetadata.entrySet().stream()
                .filter(entry -> Pattern.matches(groupMaxRegex, entry.getKey()))
                .forEach(entry -> max.set(Math.max(max.get() == null? entry.getValue() : max.get(), entry.getValue())));

        //Вычисляется самая низкая из средних точек групп
        heightMapGroupsMetadata.entrySet().stream()
                .filter(entry -> Pattern.matches(groupMedRegex, entry.getKey()))
                .forEach(entry -> min.set(Math.min(min.get() == null? entry.getValue() : min.get(), entry.getValue())));

        if (max.get() == null || min.get() == null) return groupsMetadataFiltered;

        //Вычисляется пороговое значение
        med.set((max.get() + min.get()) / 2);

        //Выбираются те классы, которые находятся по нижней границе ниже порога
        List<Integer> groupsMatches = heightMapGroupsMetadata.entrySet().stream()
                .filter(entry -> Pattern.matches(groupMinRegex, entry.getKey()))
                .filter(entry -> entry.getValue() <= med.get())
                .map(entry -> Integer.parseInt(entry.getKey().split("-")[2]))
                .toList();

        //Перемещаем выбранные классы в результат
        groupsMatches.forEach(group -> groupsMetadataFiltered.putAll(heightMapGroupsMetadata.entrySet().stream()
                .filter(entry -> Pattern.matches(groupDataRegexStart + group, entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));

        //Срезаем верхи тех классов, у которых наивысшее значение выше нормы
        groupsMetadataFiltered.entrySet().stream()
                .filter(entry -> Pattern.matches(groupMaxRegex, entry.getKey()))
                .forEach(entry -> {
                    if (entry.getValue() > med.get()) entry.setValue(med.get());
                });
        
        groupsMetadataFiltered.put("groups", heightMapGroupsMetadata.get("groups"));
        return groupsMetadataFiltered;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> heightMapGroupsMetadata = data.get("height-map-groups-metadata");

        /*
        Map<String, Double> metadata = data.get("metadata");

        //Номер выбираемого класса - следующий (1) или текущий (0)
        int sequentialNumber = metadata.get("sequential-number").intValue();
        //Направление выбора
        int direction = metadata.get("direction").intValue();

        //Заполняем классы в обозначенных границах классов в карте высот
        Map<String, Double> heightMapGroupsMetadataFiltered =
                buildGroupsMetadataFilteredMap(heightMapGroupsMetadata, sequentialNumber, direction);
         */

        Map<String, Double> heightMapGroupsMetadataFiltered =
                buildGroupsMetadataFilteredMapLightweight(heightMapGroupsMetadata);

        return heightMapGroupsMetadataFiltered;
    }
}