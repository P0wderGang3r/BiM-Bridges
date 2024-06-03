package org.granat.processors.helpers;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface IHelper {
    Map<String, Double> run(Supplier<Stream<Point>> pointsStreams, Map<String, Map<String, Double>> data);
}
