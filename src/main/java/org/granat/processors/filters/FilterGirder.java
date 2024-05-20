package org.granat.processors.filters;

import org.granat.processors.helpers.HelperHeightMap;
import org.granat.processors.helpers.IHelper;
import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FilterGirder {

    final IHelper helper = HelperHeightMap::generate;

    public static void preprocess(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {

    }

    public static void filter(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {

    }
}
