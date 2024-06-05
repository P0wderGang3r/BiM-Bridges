package org.granat.processors.helpers.height_map.algo;

import org.granat.scene.objects.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - разметка срезов на карте высот.
 */
public class HelperHeightGroupsMetadataFiltered {

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

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    /* TODO:
        выбрать максимальный класс такой, который идёт сразу после следующего максимального класса
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> metadata = data.get("metadata");
        Map<String, Double> heightMapGroupsMetadata = data.get("height-map-groups-metadata");

        //Номер выбираемого класса - следующий (1) или текущий (0)
        int sequentialNumber = metadata.get("sequential-number").intValue();
        //Направление выбора
        int direction = metadata.get("direction").intValue();

        //Заполняем классы в обозначенных границах классов в карте высот
        Map<String, Double> heightMapGroupsMetadataFiltered = buildGroupsMetadataFilteredMap(heightMapGroupsMetadata, sequentialNumber, direction);

        return heightMapGroupsMetadataFiltered;
    }
}