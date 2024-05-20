package org.granat.processors.filters;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface IFilter {
    void run(Supplier<Stream<Point>> pointsStreams, Map<String, Double> parameters);
}
