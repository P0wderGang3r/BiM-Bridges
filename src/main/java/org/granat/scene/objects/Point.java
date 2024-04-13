package org.granat.scene.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Point {

    //?-------------------------------------------------------------------------------------------------------------DATA

    private double[] coordinates;

    private double intensity;

    private double[] color;

    private boolean[] flags;

    //?----------------------------------------------------------------------------------------------------------GETTERS

    public double[] getCoordinates() throws NullPointerException {
        if (coordinates == null)
            throw new NullPointerException("Warning: dots was not correctly initialized.");
        return coordinates;
    }

    public double[] getColor() {
        if (this.color == null) {
            this.color = new double[3];
            this.color[0] = this.intensity;
            this.color[1] = this.intensity;
            this.color[2] = this.intensity;
        }
        return this.color;
    }
}
