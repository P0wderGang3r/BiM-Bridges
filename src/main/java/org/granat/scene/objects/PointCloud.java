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

    Point[] points;

    //?------------------------------------------------------------------------------------------SUB-SCENE CONFIGURATION

    private double[] position = new double[]{0, 0, 0};

    private double[] rotation = new double[]{0, 0, 0};

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public PointCloud() {
    }

    //?----------------------------------------------------------------------------------------------------------GETTERS

    public Point[] getPoints() throws NullPointerException {
        if (points == null)
            throw new NullPointerException("Warning: dots was not correctly initialized.");
        return points;
    }

    //?----------------------------------------------------------------------------------------------------------SETTERS

    public void setPoints(double[][] data) {
        this.points = new Point[data.length];
        for (int index = 0; index < data.length; index++) {
            points[index] = new Point();
            points[index].setCoordinates(data[index].clone());
            points[index].setIntensity(data[index][3]);
        }
        this.amount = this.points.length;
    }

    public void setPosition(double[] newPosition) {
        ITransform transformation = Position::position;
        Arrays.stream(this.points).parallel().forEach(
                point -> {
                    transformation.transform(point.getCoordinates(), Arrays.stream(this.position).map(value -> -value).toArray());
                    transformation.transform(point.getCoordinates(), newPosition);
                });
        System.arraycopy(newPosition, 0, this.position, 0, 3);
    }

    public void setRotation(double[] newRotation) {
        ITransform transformation = Rotation::rotate;
        Arrays.stream(this.points).parallel().forEach(
                point -> {
                    transformation.transform(point.getCoordinates(), Arrays.stream(this.rotation).map(value -> -value).toArray());
                    transformation.transform(point.getCoordinates(), newRotation);
                });
        System.arraycopy(newRotation, 0, this.rotation, 0, 3);
    }

    //?------------------------------------------------------------------------------------------------------------UTILS

}