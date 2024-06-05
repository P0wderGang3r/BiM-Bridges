package org.granat.processors;

import org.granat.controller.scene.ControllerScene;
import org.granat.processors.algo.AlgoDeflection;
import org.granat.processors.filters.FilterDensity;
import org.granat.processors.filters.FilterGirders;
import org.granat.processors.filters.FilterSuperstructures;
import org.granat.processors.filters.IFilter;
import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum Processors {
    EMPTY {

        @Override
        public void setParameters(Map<String, Double> parameters) { }

        @Override
        public void preprocess(ControllerScene controllerScene) { }

        @Override
        public void process(ControllerScene controllerScene) { }
    },
    DENSITY {
        final Map<String, Double> parameters = new HashMap<>();

        final IFilter preprocessor = FilterDensity::preprocess;

        final IFilter processor = FilterDensity::filter;

        private void initParameters(ControllerScene controllerScene) {
            parameters.put("bound", controllerScene.getMaxBound());
            parameters.putIfAbsent("epsilon-0", 0.33 * controllerScene.getMaxBound());
            parameters.putIfAbsent("epsilon-1", 0.33 * controllerScene.getMaxBound());
            parameters.putIfAbsent("epsilon-2", 0.01 * controllerScene.getMaxBound());
            parameters.putIfAbsent("delta", 0.05 * controllerScene.getMaxBound());
            parameters.putIfAbsent("threshold", 24 * 1000000.0);
        }

        public void setParameters(Map<String, Double> parameters) {
            if (parameters.get("threshold") == null ||
                    parameters.get("epsilon-0") == null ||
                    parameters.get("epsilon-1") == null ||
                    parameters.get("epsilon-2") == null) return;
            this.parameters.put("epsilon-0", parameters.get("epsilon-0"));
            this.parameters.put("epsilon-1", parameters.get("epsilon-1"));
            this.parameters.put("epsilon-2", parameters.get("epsilon-2"));
            this.parameters.put("delta", parameters.get("delta"));
            this.parameters.put("threshold", parameters.get("threshold"));
        }

        @Override
        public void preprocess(ControllerScene controllerScene) {
            initParameters(controllerScene);
            Supplier<Stream<Point>> pointsStreams = controllerScene::getPointsStream;
            preprocessor.run(pointsStreams, parameters);
        }

        @Override
        public void process(ControllerScene controllerScene) {
            initParameters(controllerScene);
            processor.run(controllerScene::getPointsStream, parameters);
        }
    },
    SUPERSTRUCTURES {
        final Map<String, Double> parameters = new HashMap<>();

        final IFilter processor = FilterSuperstructures::run;

        private void initParameters(ControllerScene controllerScene) {
            parameters.put("length", 100.0);
            parameters.putIfAbsent("axis", 2.0);
        }

        public void setParameters(Map<String, Double> parameters) { }

        @Override
        public void preprocess(ControllerScene controllerScene) { }

        @Override
        public void process(ControllerScene controllerScene) {
            initParameters(controllerScene);
            Supplier<Stream<Point>> pointsStreams = () ->
                    controllerScene.getPointsStream().filter(Point::getDensityFilterValue);
            processor.run(pointsStreams, parameters);
        }
    },
    GIRDERS {
        final Map<String, Double> parameters = new HashMap<>();

        final IFilter processor = FilterGirders::run;

        private void initParameters(ControllerScene controllerScene) {
            parameters.put("rows", 100.0);
            parameters.put("cols", 100.0);
            parameters.putIfAbsent("axis-row", 0.0);
            parameters.putIfAbsent("axis-col", 1.0);
            parameters.putIfAbsent("axis-val", 2.0);
            parameters.putIfAbsent("sequential-number", 1.0);
            parameters.putIfAbsent("direction", -1.0);
        }

        public void setParameters(Map<String, Double> parameters) { }

        @Override
        public void preprocess(ControllerScene controllerScene) { }

        @Override
        public void process(ControllerScene controllerScene) {
            initParameters(controllerScene);
            Supplier<Stream<Point>> pointsStreams = () ->
                    controllerScene.getPointsStream().filter(point ->
                            point.getParameterValue(Point.getSuperstructuresPosition()) == 0);
            processor.run(pointsStreams, parameters);
        }
    },
    DEFLECTION {
        final Map<String, Double> parameters = new HashMap<>();

        final IFilter processor = AlgoDeflection::run;

        private void initParameters(ControllerScene controllerScene) {
            //TODO: ВАЖНО!!
        }

        public void setParameters(Map<String, Double> parameters) { }

        @Override
        public void preprocess(ControllerScene controllerScene) { }

        @Override
        public void process(ControllerScene controllerScene) {
            initParameters(controllerScene);
            processor.run(controllerScene::getPointsStream, parameters);
        }
    };

    public abstract void setParameters(Map<String, Double> parameters);

    public abstract void preprocess(ControllerScene controllerScene);

    public abstract void process(ControllerScene controllerScene);
}
