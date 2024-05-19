package org.granat.controller.wrapper;

import org.granat.controller.gui.ControllerFileChooser;
import org.granat.controller.scene.ControllerScene;
import org.granat.scene.objects.PointCloud;
import org.granat.wrapper.IWrapper;
import org.granat.wrapper.e57.WrapperE57;

import java.util.Arrays;

public class ControllerWrapper {

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final ControllerScene controllerScene;

    private final ControllerFileChooser controllerFileChooser;

    private IWrapper wrapper;

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public ControllerWrapper(ControllerScene controllerScene, ControllerFileChooser controllerFileChooser) {
        this.controllerScene = controllerScene;
        this.controllerFileChooser = controllerFileChooser;
    }

    //?----------------------------------------------------------------------------------------------------------PRIVATE

    private boolean loadWrapperE57(String filePath) {
        IWrapper wrapper = new WrapperE57(filePath);
        if (wrapper.canBeOpened()) this.wrapper = wrapper;
        return wrapper.canBeOpened();
    }

    private void pullData() {
        if (wrapper == null) return;
        System.out.println(wrapper.getPointsNum());
        PointCloud pointCloud = new PointCloud();
        pointCloud.setPoints(this.wrapper.getData());
        pointCloud.setPosition(new double[]{0, 0, 0});
        pointCloud.setRotation(new double[]{0, 0, Math.PI / 2});
        this.controllerScene.addPointCloud(pointCloud);
    }

    private void pullBounds() {
        if (wrapper == null) return;
        this.controllerScene.setMaxBound(
                Arrays.stream(wrapper.getPointsBounds())
                .map(Math::abs)
                .reduce(Math::max)
                .orElse(1.0));
    }

    //?-----------------------------------------------------------------------------------------------------------PUBLIC

    public void loadSingleScene() {
        String filename = controllerFileChooser.getFilePath();
        if (filename == null || filename.isEmpty() || filename.equals("nullnull")) return;
        if (!this.loadWrapperE57(filename)) return;

        this.controllerScene.clearScene();
        this.pullData();
        this.pullBounds();
    }

    //?-----------------------------------------------------------------------------------------------------------------

}
