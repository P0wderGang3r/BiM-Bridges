package org.granat.filters;

import org.granat.controller.scene.ControllerScene;
import org.granat.scene.objects.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum Filters {
    DENSITY {
        final Map<String, Double> parameters = new HashMap<>();

        final IFilter preprocess = FilterDensity::preprocess;

        final IFilter filter = FilterDensity::filter;

        private void initParameters(ControllerScene controllerScene) {
            parameters.put("bound", controllerScene.getMaxBound());
            parameters.putIfAbsent("delta", 0.5);
            parameters.putIfAbsent("threshold", 1060073.0);
        }

        public void setParameters(Map<String, Double> parameters) {
            if (parameters.get("delta") == null || parameters.get("threshold") == null) return;
            parameters.put("delta", parameters.get("delta"));
            parameters.put("threshold", parameters.get("threshold"));
        }

        @Override
        public void preprocess(ControllerScene controllerScene) {
            initParameters(controllerScene);
            Supplier<Stream<Point>> pointsStreams = controllerScene::getPoints;
            preprocess.run(pointsStreams, parameters);
        }

        @Override
        public void filter(ControllerScene controllerScene) {
            initParameters(controllerScene);
            filter.run(controllerScene::getPoints, parameters);
        }
    };

    public abstract void setParameters(Map<String, Double> parameters);

    public abstract void preprocess(ControllerScene controllerScene);

    public abstract void filter(ControllerScene controllerScene);
}
