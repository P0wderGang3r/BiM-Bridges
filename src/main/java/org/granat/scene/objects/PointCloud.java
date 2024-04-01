package org.granat.scene.objects;

import lombok.Getter;
import lombok.Setter;
import org.granat.scene.utils.ITransform;
import org.granat.scene.utils.Position;
import org.granat.scene.utils.Rotation;

import java.util.Arrays;

@Getter
public class PointCloud {

    //?-------------------------------------------------------------------------------------------------------------DATA

    @Setter
    int amount = 0;

    double[][] points;

    double[] intensity;

    double[][] colors;

    //?------------------------------------------------------------------------------------------SUB-SCENE CONFIGURATION

    private double[] position = new double[]{0, 0, 0};

    private double[] rotation = new double[]{0, 0, 0};

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public PointCloud() {
    }

    //?----------------------------------------------------------------------------------------------------------GETTERS

    public double[][] getPoints() throws NullPointerException {
        if (points == null)
            throw new NullPointerException("Warning: dots was not correctly initialized.");
        return points;
    }

    public double[][] getColors() {
        if (this.colors == null) {
            this.colors = new double[this.intensity.length][3];
            for (int index = 0; index < this.intensity.length; index++) {
                this.colors[index][0] = this.intensity[index];
                this.colors[index][1] = this.intensity[index];
                this.colors[index][2] = this.intensity[index];
            }
        }
        return this.colors;
    }

    //?----------------------------------------------------------------------------------------------------------SETTERS

    public void setPoints(double[][] data) {
        this.points = new double[data.length][3];
        this.intensity = new double[data.length];
        for (int index = 0; index < data.length; index++) {
            System.arraycopy(data[index], 0, this.points[index], 0, 3);
            this.intensity[index] = data[index][3];
        }
        this.amount = this.points.length;
    }

    public void setPosition(double[] newPosition) {
        ITransform transformation = Position::position;
        Arrays.stream(this.points).parallel().forEach(
                point -> {
                    transformation.transform(point, Arrays.stream(this.position).map(value -> -value).toArray());
                    transformation.transform(point, newPosition);
                });
        System.arraycopy(newPosition, 0, this.position, 0, 3);
    }

    public void setRotation(double[] newRotation) {
        ITransform transformation = Rotation::rotate;
        Arrays.stream(this.points).parallel().forEach(
                point -> {
                    transformation.transform(point, Arrays.stream(this.rotation).map(value -> -value).toArray());
                    transformation.transform(point, newRotation);
                });
        System.arraycopy(newRotation, 0, this.rotation, 0, 3);
    }

    //?------------------------------------------------------------------------------------------------------------UTILS

}