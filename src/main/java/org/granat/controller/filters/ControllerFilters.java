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

    public void runFilterSuperstructures() {
        System.out.println("STARTED SUPERSTRUCTURES FILTER: " + this.getClass());
        Processors.SUPERSTRUCTURES.process(controllerScene);
        System.out.println("ENDED SUPERSTRUCTURES FILTER: " + this.getClass());
    }

    public void runFilterGirders() {
        System.out.println("STARTED GIRDERS FILTER: " + this.getClass());
        Processors.GIRDERS.process(controllerScene);
        System.out.println("ENDED GIRDERS FILTER: " + this.getClass());
    }

    public void runAlgoDeflection() {
        System.out.println("STARTED DEFLECTION ALGO: " + this.getClass());
        Processors.DEFLECTION.process(controllerScene);
        System.out.println("ENDED DEFLECTION ALGO: " + this.getClass());
    }
}
