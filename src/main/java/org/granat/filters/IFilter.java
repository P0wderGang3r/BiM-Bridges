package org.granat.filters;

import org.granat.controller.scene.ControllerScene;
import org.granat.scene.Scene;
import org.granat.scene.objects.Point;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface IFilter {
    void run(Supplier<Stream<Point>> pointsStreams, Map<String, Double> parameters);
}
