package org.granat.processors.helpers.deflection_points;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HelperDeflectionPoints {

    private static Map<String, Double> buildDeflectionPoints(Map<String, Double> heightMap) {
        Map<String, Double> deflectionPoints = new HashMap<>();

        return deflectionPoints;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> heightMap = data.get("height-map");

        Map<String, Double> deflectionPoints = buildDeflectionPoints(heightMap);

        return deflectionPoints;
    }
}
