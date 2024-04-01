package org.granat.controller.gui;

import org.granat.controller.scene.ControllerScene;
import org.granat.ui.gui.input.keyboard.InputKeyboardBinding;
import org.granat.ui.gui.input.mouse.InputMouseBinding;

public class ControllerOutput {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final ControllerScene controllerScene;
    private final ControllerCamera controllerCamera;

    //?---------------------------------------------------------------------------------------------------------INTERNAL


    //?------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public ControllerOutput(
            ControllerScene controllerScene,
            ControllerCamera controllerCamera
    ) {
        this.controllerScene = controllerScene;
        this.controllerCamera = controllerCamera;
    }

    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    //----------------------------------------------------------------------------------------------------------KEYBOARD

    public void doKeyboardInput(InputKeyboardBinding binding) {
    }

    //-------------------------------------------------------------------------------------------------------------MOUSE

    public void doMouseCursorInput(InputMouseBinding binding, double[] deltaPos, double sensitivity) {
        switch (binding) {
            case ROTATION: controllerScene.addRotation(
                    InputMouseBinding.ROTATION.interpretMouseInput(deltaPos), sensitivity); break;
            case POSITION: controllerScene.addPosition(
                    InputMouseBinding.POSITION.interpretMouseInput(deltaPos), sensitivity); break;
            default: break;
        }
    }

    public void doMouseScrollInput(InputMouseBinding binding, double[] deltaPos) {
        switch (binding) {
            case ZOOM: controllerCamera.addFov(
                    InputMouseBinding.ZOOM.interpretMouseInput(deltaPos)[1]); break;
            default: break;
        }
    }
}
