package org.granat.scene;

import lombok.Getter;
import org.granat.scene.objects.PointCloud;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Scene {

    //?-----------------------------------------------------------------------------------------------------------CONSTS

    @Getter
    private final static byte densityPosition = 0;

    @Getter
    private final static byte colorGradationDensity = 0;

    @Getter
    private final static byte superstructuresPosition = 1;

    @Getter
    private final static byte colorGradationSuperstructures = 1;

    @Getter
    private final static byte girdersPosition = 2;

    @Getter
    private final static byte colorGradationGirders = 2;

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    /**
     * [0] - максимальное значение плотности во множестве точек
     */
    private long[] pointParameters;

    private double maxBound = 0;

    private double maxBoundDivider = 1;

    private int currentColorGradation = -1;

    private int currentFilter = -1;

    //?---------------------------------------------------------------------------------------------------SCENE POSITION

    private final double[] rotation = new double[] {0, 0, 0};

    private final double[] position = new double[] {0, 0, 0};

    //?-------------------------------------------------------------------------------------------------------SCENE DATA

    List<PointCloud> pointClouds;

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public Scene() {
        this.pointClouds = new ArrayList<>();
    }

    //?----------------------------------------------------------------------------------------------------------SETTERS

    public void setMaxBound(double maxBound) {
        this.maxBound = maxBound;
        this.maxBoundDivider = 1 / maxBound;
    }

    private boolean notNullSceneParameterValue(int position, long value) {
        if (this.pointParameters == null) {
            this.pointParameters = new long[position + 1];
            this.pointParameters[position] = value;
            return false;
        } else if (this.pointParameters.length < position + 1) {
            long[] newParameters = new long[position + 1];
            System.arraycopy(this.pointParameters, 0, newParameters, 0, this.pointParameters.length);
            newParameters[position] = value;
            this.pointParameters = newParameters;
            return false;
        }

        return true;
    }

    private void setSceneParameterValue(int position, long value) {
        if (this.notNullSceneParameterValue(position, value)) {
            this.pointParameters[position] = value;
        }
    }

    public void setDensitySceneParameterValue(long value) {
        setSceneParameterValue(densityPosition, value);
    }

    public void setEmptyFilter() {
        this.currentFilter = -1;
    }

    public void setDensityFilter() {
        this.currentFilter = densityPosition;
    }

    public void setColorGradationIntensity() {
        this.currentColorGradation = -1;
    }

    public void setColorGradationDensity() {
        this.currentColorGradation = colorGradationDensity;
    }

    public void setColorGradationSuperstructures() {
        this.currentColorGradation = colorGradationSuperstructures;
    }

    public void setColorGradationGirders() {
        this.currentColorGradation = colorGradationGirders;
    }

    //?----------------------------------------------------------------------------------------------------------GETTERS

    public long getSceneDensityParameterValue() {
        if (this.pointParameters != null && this.pointParameters.length > densityPosition)
            return pointParameters[densityPosition];
        return 1;
    }

    public double getSceneSuperstructuresParameterValue() {
        if (this.pointParameters != null && this.pointParameters.length > superstructuresPosition)
            return pointParameters[densityPosition];
        return 10;
    }

    public double getSceneGirdersParameterValue() {
        if (this.pointParameters != null && this.pointParameters.length > girdersPosition)
            return pointParameters[densityPosition];
        return 10;
    }
}
