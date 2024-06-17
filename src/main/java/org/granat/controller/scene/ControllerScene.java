package org.granat.controller.scene;

import org.granat.scene.Scene;
import org.granat.scene.enums.ColorGradation;
import org.granat.scene.objects.Point;
import org.granat.scene.objects.PointCloud;

import java.util.Arrays;
import java.util.stream.Stream;

public record ControllerScene(Scene scene) {

    //?-----------------------------------------------------------------------------------------------------------PUBLIC

    //----------------------------------------------------------------------------------------------------ЗАГРУЗКА СЦЕНЫ

    public void clearScene() {
        this.scene.getPointClouds().clear();
        this.scene.setMaxBound(1.0);
        this.scene.setEmptyFilter();
        this.scene.setColorGradationIntensity();
        this.scene.setDensitySceneParameterValue(1);
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

    //-----------------------------------------------------------------------------------ПОЛУЧЕНИЕ МНОЖЕСТВА ТОЧЕК СЦЕНЫ

    public Stream<Point> getPointsStream() {
        return this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream);
    }

    public Stream<Point[]> getDrawablePointsStream() {
        return this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints);
    }

    //------------------------------------------------------------------------------------------РАБОТА С ГРАНИЦАМИ СЦЕНЫ

    public void setMaxBound(double maxBound) {
        if (maxBound >= 0) this.scene.setMaxBound(maxBound);
    }

    public double getMaxBound() {
        return this.scene.getMaxBound();
    }

    public double getMaxBoundDivider() {
        return this.scene.getMaxBoundDivider();
    }

    //-----------------------------------------------------------------------------------------------------ПОВОРОТ СЦЕНЫ

    public void addRotation(double[] addition, double sensitivity) {
        scene.getRotation()[0] += addition[0] * sensitivity;
        scene.getRotation()[1] += addition[1] * sensitivity;
        scene.getRotation()[2] += addition[2] * sensitivity;
    }

    public double[] getRotation() {
        return scene.getRotation();
    }

    //-----------------------------------------------------------------------------------------------------ПОЗИЦИЯ СЦЕНЫ

    public void addPosition(double[] addition, double sensitivity) {
        scene.getPosition()[0] += addition[0] * sensitivity * 20;
        scene.getPosition()[1] += addition[1] * sensitivity * 20;
        scene.getPosition()[2] += addition[2] * sensitivity * 20;
    }

    public double[] getPosition() {
        return scene.getPosition();
    }

    //!------------------------------------------------------------------------------------------ГРАДАЦИЯ ТОЧЕК ПО ЦВЕТУ

    //-------------------------------------------------------------------------------------------ИНТЕНСИВНОСТЬ ИЗЛУЧЕНИЯ

    public void setColorGradationIntensity() {
        this.scene.setColorGradationIntensity();
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.INTENSITY.setColor(point, null));
    }

    //---------------------------------------------------------------------------------------------------ПЛОТНОСТЬ ТОЧЕК

    public void setColorGradationDensity() {
        this.scene.setColorGradationDensity();
        double[] parameters = new double[] {this.scene.getSceneDensityParameterValue()};
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.DENSITY.setColor(point, parameters));
    }

    public void resetColorGradationDensity() {
        if (this.scene.getCurrentColorGradation() != Scene.getColorGradationDensity()) return;
        double[] parameters = new double[] {this.scene.getSceneDensityParameterValue()};
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.DENSITY.setColor(point, parameters));
    }

    //-----------------------------------------------------------------------------------------ПРОЛЁТЫ МОСТОВЫХ СТРОЕНИЙ

    public void setColorGradationSuperstructures() {
        this.scene.setColorGradationSuperstructures();
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.SUPERSTRUCTURES.setColor(point, null));
    }

    public void resetColorGradationSuperstructures() {
        if (this.scene.getCurrentColorGradation() != Scene.getColorGradationSuperstructures()) return;
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.SUPERSTRUCTURES.setColor(point, null));
    }


    //----------------------------------------------------------------------------------БАЛКИ ПРОЛЁТОВ МОСТОВЫХ СТРОЕНИЙ

    public void setColorGradationGirders() {
        this.scene.setColorGradationGirders();
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.GIRDERS.setColor(point, null));
    }

    public void resetColorGradationGirders() {
        if (this.scene.getCurrentColorGradation() != Scene.getColorGradationGirders()) return;
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.GIRDERS.setColor(point, null));
    }


    //---------------------------------------------------------------------------ВЫСОТА БАЛКИ ПРОЛЁТОВ МОСТОВЫХ СТРОЕНИЙ

    public void setColorGradationGirdersHeight() {
        this.scene.setColorGradationGirdersHeight();
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.GIRDERS_HEIGHT.setColor(point, null));
    }

    public void resetColorGradationGirdersHeight() {
        if (this.scene.getCurrentColorGradation() != Scene.getColorGradationGirdersHeight()) return;
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .forEach(point -> ColorGradation.GIRDERS_HEIGHT.setColor(point, null));
    }


    //---------------------------------------------------------------------------------------------------ПАРАМЕТРЫ СЦЕНЫ

    public void updateDensitySceneParameter() {
        System.out.println("STARTED SETTING MAX DENSITY VALUE: " + this.getClass());
        this.scene.getPointClouds().stream()
                .map(PointCloud::getPoints)
                .flatMap(Arrays::stream)
                .map(Point::getDensityParameterValue)
                .reduce(Math::max)
                .ifPresent(this.scene::setDensitySceneParameterValue);
        System.out.println("ENDED SETTING MAX DENSITY VALUE: " + this.getClass());
    }

    //?------------------------------------------------------------------------------------------ТЕКУЩЕЕ ПОЛЕ ФИЛЬТРАЦИИ

    public void setEmptyFilter() {
        this.scene.setEmptyFilter();
    }

    public void setDensityFilter() {
        this.scene.setDensityFilter();
    }

    public void setGirdersHeightFilter() {
        this.scene.setGirdersHeightFilter();
    }

    public int getCurrentFilter() {
        return this.scene.getCurrentFilter();
    }

    //?-----------------------------------------------------------------------------------------------------------------

}
