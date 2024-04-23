package org.granat.ui.gui.user;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Camera {

    //?---------------------------------------------------------------------------------------------CAMERA CONFIGURATION

    @Setter
    private double fov = 135;

    private double maxFov = 150;

    private double minFov = 15;

    private double deltaFov = 2;

    //?--------------------------------------------------------------------------------------------------CAMERA POSITION

    private final double[] rotation = new double[] {0, 0, 0};

    private final double[] position = new double[] {0, 0, 0};

    //?-----------------------------------------------------------------------------------------------KEYS CONFIGURATION

    //?----------------------------------------------------------------------------------------------------------SETTERS

    public void setFov(double fov) {
        if (fov < minFov) {
            this.fov = minFov;
            return;
        }
        if (fov > maxFov) {
            this.fov = maxFov;
            return;
        }
        this.fov = fov;
    }
}
