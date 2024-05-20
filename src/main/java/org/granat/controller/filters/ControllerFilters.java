package org.granat.controller.filters;

import org.granat.controller.scene.ControllerScene;
import org.granat.processors.Processors;

import java.util.Map;

public record ControllerFilters(
        ControllerScene controllerScene
) {
    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    public void runEmptyFilter() {
        System.out.println("STARTED EMPTY FILTER: " + this.getClass());
        Processors.EMPTY.process(controllerScene);
        System.out.println("ENDED EMPTY FILTER: " + this.getClass());
    }

    /**
     *
     * @param parameters "position" - parameter position in {@link org.granat.scene.objects.Point Point},
     *                   "delta" - slice "thickness"
     */
    public void setDensityFilterParameters(Map<String, Double> parameters) {
        Processors.DENSITY.setParameters(parameters);
    }

    public void runDensityPreprocess() {
        System.out.println("STARTED DENSITY PREPROCESS: " + this.getClass());
        Processors.DENSITY.preprocess(controllerScene);
        System.out.println("ENDED DENSITY PREPROCESS: " + this.getClass());
    }

    public void runDensityFilter() {
        System.out.println("STARTED DENSITY FILTER: " + this.getClass());
        Processors.DENSITY.process(controllerScene);
        System.out.println("ENDED DENSITY FILTER: " + this.getClass());
    }
}
