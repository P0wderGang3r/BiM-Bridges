package org.granat.controller.scene;

import org.granat.scene.Scene;
import org.granat.scene.objects.Point;
import org.granat.scene.objects.PointCloud;

import java.util.Arrays;
import java.util.stream.Stream;

public record ControllerScene(Scene scene) {

    //?-----------------------------------------------------------------------------------------------------------PUBLIC

    public void clearScene() {
        this.scene.getPointClouds().clear();
        this.scene.setMaxBound(1.0);
    }

    public boolean isEmptyScene() {
        if (!this.scene.getPointClouds().isEmpty())
            return this.scene.getPointClouds().get(0).getPoints().length != 0;
        return false;
    }

    public void addPointCloud(PointCloud pointCloud) {
        if (pointCloud != null && pointCloud.getPoints().length > 0)
            this.scene.getPointClouds().add(pointCloud);
    }

    public Stream<Point> getPoints() {
        return this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream);
    }

    public Stream<Point[]> getPointArrays() {
        return this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints);
    }

    public void setMaxBound(double maxBound) {
        if (maxBound >= 0) this.scene.setMaxBound(maxBound);
    }

    public double getMaxBound() {
        return this.scene.getMaxBound();
    }

    public double getMaxBoundDivider() {
        return this.scene.getMaxBoundDivider();
    }

    public void addRotation(double[] addition, double sensitivity) {
        scene.getRotation()[0] += addition[0] * sensitivity;
        scene.getRotation()[1] += addition[1] * sensitivity;
        scene.getRotation()[2] += addition[2] * sensitivity;
    }

    public double[] getRotation() {
        return scene.getRotation();
    }

    public void addPosition(double[] addition, double sensitivity) {
        scene.getPosition()[0] += addition[0] * sensitivity * 20;
        scene.getPosition()[1] += addition[1] * sensitivity * 20;
        scene.getPosition()[2] += addition[2] * sensitivity * 20;
    }

    public double[] getPosition() {
        return scene.getPosition();
    }

    //?-----------------------------------------------------------------------------------------------------------------

}
