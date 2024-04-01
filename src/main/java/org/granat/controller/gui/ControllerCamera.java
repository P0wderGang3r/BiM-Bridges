package org.granat.controller.gui;


import org.granat.ui.gui.user.Camera;

public class ControllerCamera {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final Camera camera;

    //?---------------------------------------------------------------------------------------------------------INTERNAL



    //?------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public ControllerCamera(Camera camera) {
        this.camera = camera;
    }

    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    public void addRotation(double[] addition, double sensitivity) {
        camera.getRotation()[0] += addition[0] * sensitivity;
        camera.getRotation()[1] += addition[1] * sensitivity;
        camera.getRotation()[2] += addition[2] * sensitivity;
    }

    public double[] getRotation() {
        return camera.getRotation();
    }

    public void addPosition(double[] addition, double sensitivity) {
        camera.getPosition()[0] += addition[0] * sensitivity * 10;
        camera.getPosition()[1] += addition[1] * sensitivity * 10;
        camera.getPosition()[2] += addition[2] * sensitivity * 10;
    }

    public double[] getPosition() {
        return camera.getPosition();
    }

    public void addFov(double delta) {
        camera.setFov(camera.getFov() + delta * camera.getDeltaFov());
    }

    public double getFov() {
        return camera.getFov();
    }
}
