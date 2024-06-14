package org.granat.processors.helpers.height_map.base;

import org.granat.scene.objects.Point;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - разметка срезов на карте высот.
 */
public class HelperHeightMapClasses {

    /**
     * Помечаем соседей в ближайшей окрестности.
     */
    private static List<String> markNeighbors(
            Map<String, Double> classesMap,
            Map<String, Double> heightMap,
            Map<String, Double> borderMap,
            int row, int col) {
        List<String> markedNeighbors = new ArrayList<>();


        for (int iterRow = row - 1; iterRow <= row + 1; iterRow++) {
            for (int iterCol = col - 1; iterCol <= col + 1; iterCol++) {
                if (iterRow == row && iterCol == col) continue;
                /*
                if (iterRow == row - 1 && iterCol == col - 1) continue;
                if (iterRow == row + 1 && iterCol == col + 1) continue;
                if (iterRow == row - 1 && iterCol == col + 1) continue;
                if (iterRow == row + 1 && iterCol == col - 1) continue;
                */
                
                if (heightMap.get(iterRow + "-" + iterCol) != null &&
                        borderMap.get(iterRow + "-" + iterCol) == null &&
                        classesMap.get(iterRow + "-" + iterCol) == null) {
                    markedNeighbors.add(iterRow + "-" + iterCol);
                }
            }
        }

        return markedNeighbors;
    }

    private static boolean fillClass(
            Map<String, Double> classesMap,
            Map<String, Double> heightMap,
            Map<String, Double> borderMap,
            double currentClass, int row, int col) {
        if (classesMap.get(row + "-" + col) != null) return false;
        if (heightMap.get(row + "-" + col) == null) return false;
        if (borderMap.get(row + "-" + col) != null) return false;

        Stack<String> classesFilled = new Stack<>();
        classesFilled.add(row + "-" + col);

        String currentElement;
        int iterRow, iterCol;
        while (!classesFilled.isEmpty()) {
            currentElement = classesFilled.pop();
            iterRow = Integer.parseInt(currentElement.split("-")[0]);
            iterCol = Integer.parseInt(currentElement.split("-")[1]);
            classesMap.put(iterRow + "-" + iterCol, currentClass);

            classesFilled.addAll(markNeighbors(classesMap, heightMap, borderMap, iterRow, iterCol));
        }

        return true;
    }

    private static Map<String, Double> buildClassesMap(
            Map<String, Double> heightMap,
            Map<String, Double> bordersMap,
            int rows, int cols) {
        Map<String, Double> classesMap = new HashMap<>();

        int currentClass = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (fillClass(classesMap, heightMap, bordersMap, currentClass, row, col)) currentClass++;
            }
        }

        classesMap.put("classes", (double) currentClass);
        return classesMap;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> metadata = data.get("metadata");
        Map<String, Double> heightMap = data.get("height-map");
        Map<String, Double> heightMapBorders = data.get("height-map-borders");

        //Количество строк в матрице
        int rows = metadata.get("rows").intValue();
        //Количество колонок в матрице
        int cols = metadata.get("cols").intValue();

        //Заполняем классы в обозначенных границах классов в карте высот
        Map<String, Double> heightMapClasses = buildClassesMap(heightMap, heightMapBorders, rows, cols);

        return heightMapClasses;
    }
}