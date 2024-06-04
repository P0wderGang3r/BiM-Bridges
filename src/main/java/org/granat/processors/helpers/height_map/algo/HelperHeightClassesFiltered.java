package org.granat.processors.helpers.height_map.algo;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - разметка срезов на карте высот.
 */
public class HelperHeightClassesFiltered {

    private static Map<String, Double> buildClassesMetadataMap(
            Map<String, Double> heightMap,
            Map<String, Double> heightMapClasses,
            int rows, int cols) {
        Map<String, Double> classesMetadataMap = new HashMap<>();
        classesMetadataMap.put("classes", 0.0);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (heightMapClasses.get(row + "" + col) != null) {
                    //Устанавливаем максимальное значение класса как количество данных классов
                    classesMetadataMap.put("classes", Math.max(
                            classesMetadataMap.get("classes"), heightMapClasses.get(row + "-" + col)));
                    //Устанавливаем максимальное значение для текущего класса
                    classesMetadataMap.put("classes-max-" + heightMapClasses.get(row + "-" + col),
                            Math.max(heightMap.get(row + "-" + col) == null ? -1.0 : heightMap.get(row + "-" + col),
                                    heightMap.get(row + "-" + col)));
                    //Устанавливаем минимальное значение для текущего класса
                    classesMetadataMap.put("classes-min-" + heightMapClasses.get(row + "-" + col),
                            Math.min(heightMap.get(row + "-" + col) == null ? 1.0 : heightMap.get(row + "-" + col),
                                    heightMap.get(row + "-" + col)));
                }
            }
        }

        //Увеличиваем значение количества классов на 1
        classesMetadataMap.put("classes", classesMetadataMap.get("classes") + 1.0);
        return classesMetadataMap;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> metadata = data.get("metadata");
        Map<String, Double> heightMap = data.get("height-map");
        Map<String, Double> heightMapClasses = data.get("height-map-classes");

        //Количество строк в матрице
        int rows = metadata.get("rows").intValue();
        //Количество колонок в матрице
        int cols = metadata.get("cols").intValue();

        //Заполняем классы в обозначенных границах классов в карте высот
        Map<String, Double> heightMapMetadataClasses = buildClassesMetadataMap(heightMap, heightMapClasses, rows, cols);

        return heightMapMetadataClasses;
    }
}