package org.granat.controller.scene;

import lombok.Getter;
import org.granat.scene.Scene;
import org.granat.scene.objects.PointCloud;
import org.granat.wrapper.IWrapper;
import org.granat.wrapper.e57.WrapperE57;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class ControllerScene {

    private final Scene scene;

    private IWrapper wrapper;

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public ControllerScene(Scene scene) {
        this.scene = scene;
    }

    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    public boolean setWrapper(String filePath) {
        wrapper = new WrapperE57(filePath);
        return wrapper.canBeOpened();
    }

    public void pullData() {
        if (wrapper == null) return;
        System.out.println(wrapper.getPointsNum());
        PointCloud pointCloud = new PointCloud();
        pointCloud.setPoints(this.wrapper.getData());
        pointCloud.setPosition(new double[]{0, 0, -5});
        pointCloud.setRotation(new double[]{0, 0, Math.PI / 2});
        this.scene.getPointClouds().add(pointCloud);
    }

    public void pullBounds() {
        if (wrapper == null) return;
        Arrays.stream(wrapper.getPointsBounds()).forEach(bound -> this.scene.setMaxBound(
                Math.max(this.scene.getMaxBound(), Math.abs(bound))));
    }

    public boolean isEmpty() {
        if (!this.scene.getPointClouds().isEmpty())
            return this.scene.getPointClouds().get(0).getPoints().length != 0;
        return false;
    }

    public List<PointCloud> getPointClouds() {
        return this.scene.getPointClouds();
    }

    public double getMaxBound() {
        return this.scene.getMaxBound();
    }

    public double getMaxBoundDivider() {
        return this.scene.getMaxBoundDivider();
    }

    public void clearPointsData() {
        this.scene.getPointClouds().clear();
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
