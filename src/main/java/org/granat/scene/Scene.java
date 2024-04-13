package org.granat.scene;

import lombok.Getter;
import lombok.Setter;
import org.granat.scene.objects.PointCloud;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Scene {

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    private double maxBound = 0;

    private double maxBoundDivider = 1;

    //?---------------------------------------------------------------------------------------------------SCENE POSITION

    private final double[] rotation = new double[] {0, 0, 0};

    private final double[] position = new double[] {0, 0, 0};

    //?-------------------------------------------------------------------------------------------------------SCENE DATA

    List<PointCloud> pointClouds;
    List<PointCloud> filteredPointClouds;

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public Scene() {
        this.pointClouds = new ArrayList<>();
        this.filteredPointClouds = new ArrayList<>();
    }

    //?----------------------------------------------------------------------------------------------------------SETTERS

    public void setMaxBound(double maxBound) {
        this.maxBound = maxBound;
        this.maxBoundDivider = 1 / maxBound;
    }
}
