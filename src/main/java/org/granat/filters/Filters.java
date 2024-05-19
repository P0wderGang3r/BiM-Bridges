package org.granat.filters;

import org.granat.controller.scene.ControllerScene;
import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum Filters {
    EMPTY {

        @Override
        public void setParameters(Map<String, Double> parameters) { }

        @Override
        public void preprocess(ControllerScene controllerScene) { }

        @Override
        public void filter(ControllerScene controllerScene) { }
    },
    DENSITY {
        final Map<String, Double> parameters = new HashMap<>();

        final IFilter preprocess = FilterDensity::preprocess;

        final IFilter filter = FilterDensity::filter;

        private void initParameters(ControllerScene controllerScene) {
            parameters.put("bound", controllerScene.getMaxBound());
            parameters.putIfAbsent("delta-0", 0.3 * controllerScene.getMaxBound());
            parameters.putIfAbsent("delta-1", 0.3 * controllerScene.getMaxBound());
            parameters.putIfAbsent("delta-2", 0.1 * controllerScene.getMaxBound());
            parameters.putIfAbsent("threshold", 2.5 * 1060073.0);
        }

        public void setParameters(Map<String, Double> parameters) {
            if (parameters.get("threshold") == null ||
                    parameters.get("delta-0") == null ||
                    parameters.get("delta-1") == null ||
                    parameters.get("delta-2") == null) return;
            this.parameters.put("delta-0", parameters.get("delta-0"));
            this.parameters.put("delta-1", parameters.get("delta-1"));
            this.parameters.put("delta-2", parameters.get("delta-2"));
            this.parameters.put("threshold", parameters.get("threshold"));
        }

        @Override
        public void preprocess(ControllerScene controllerScene) {
            initParameters(controllerScene);
            Supplier<Stream<Point>> pointsStreams = controllerScene::getPointsStream;
            preprocess.run(pointsStreams, parameters);
        }

        @Override
        public void filter(ControllerScene controllerScene) {
            initParameters(controllerScene);
            filter.run(controllerScene::getPointsStream, parameters);
        }
    };

    public abstract void setParameters(Map<String, Double> parameters);

    public abstract void preprocess(ControllerScene controllerScene);

    public abstract void filter(ControllerScene controllerScene);
}
