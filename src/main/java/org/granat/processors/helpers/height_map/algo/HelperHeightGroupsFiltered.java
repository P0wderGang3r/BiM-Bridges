package org.granat.processors.helpers.height_map.algo;

import org.granat.scene.objects.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - разметка срезов на карте высот.
 */
public class HelperHeightGroupsFiltered {

    private static Map<String, Double> buildClassesMetadataMap(
            Map<String, Double> heightMapGroupsMetadata,
            int sequentialNumber, int direction) {
        Map<String, Double> groupsFiltered = new HashMap<>();

        int groups = heightMapGroupsMetadata.get("groups").intValue();

        List<String> sortedGroupsList = new ArrayList<>();
        for (int index = 0; index < groups; index++) {
            if (sortedGroupsList.size() == 0) {
                sortedGroupsList.add("group-med-" + index);
            }
            for (int sortedIndex = 0; sortedIndex < sortedGroupsList.size(); sortedIndex++) {
                if (heightMapGroupsMetadata.get("")) {

                }
                sortedGroupsList.add("");
            }
        }

        sortedGroupsList.sort((val1, val2) -> {
            if (val1.split("-")[2] )
        });

        return groupsFiltered;
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

        //Номер выбираемого класса
        int sequentialNumber = metadata.get("sequential-number").intValue();
        //Направление выбора
        int direction = metadata.get("direction").intValue();

        //Заполняем классы в обозначенных границах классов в карте высот
        Map<String, Double> heightMapMetadataClasses = buildClassesMetadataMap(heightMapGroupsMetadata, sequentialNumber, direction);

        return heightMapMetadataClasses;
    }
}