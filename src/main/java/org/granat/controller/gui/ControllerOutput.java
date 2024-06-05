package org.granat.controller.gui;

import org.granat.controller.filters.ControllerFilters;
import org.granat.controller.scene.ControllerScene;
import org.granat.controller.server.ControllerServer;
import org.granat.controller.wrapper.ControllerWrapper;
import org.granat.ui.gui.input.keyboard.InputKeyboardBinding;
import org.granat.ui.gui.input.mouse.InputMouseBinding;
import org.granat.ui.gui.runtime.State;

import java.util.List;

public record ControllerOutput(
        ControllerScene controllerScene,
        ControllerWrapper controllerWrapper,
        ControllerCamera controllerCamera,
        ControllerFilters controllerFilters,
        ControllerServer controllerServer
) {

    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    //----------------------------------------------------------------------------------------------------------KEYBOARD

    public void doKeyboardInput(InputKeyboardBinding binding) {
        switch (binding) {
            case LOAD_FILE:
                controllerServer.setPendingOperation(
                        State.READING,
                        List.of(controllerWrapper::loadSingleScene));
                break;

            case FILTER_EMPTY:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerFilters::runEmptyFilter));
                break;
            case FILTER_DENSITY:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerFilters::runDensityPreprocess,
                                controllerFilters::runDensityFilter,
                                controllerScene::updateDensitySceneParameter,
                                controllerScene::resetColorGradationDensity));
                break;
            case FILTER_SUPERSTRUCTURES:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerFilters::runFilterSuperstructures));
                break;
            case FILTER_GIRDERS:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerFilters::runFilterGirders));
                break;

            case SHOW_INTENSITY:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerScene::setColorGradationIntensity));
                break;
            case SHOW_DENSITY:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerScene::setColorGradationDensity));
                break;
            case SHOW_SUPERSTRUCTURES:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerScene::setColorGradationSuperstructures));
                break;
            case SHOW_GIRDERS:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerScene::setColorGradationGirders));
                break;

            case SHOW_FILTER_EMPTY:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerScene::setEmptyFilter));
                break;
            case SHOW_FILTER_DENSITY:
                controllerServer.setPendingOperation(
                        State.RUNNING,
                        List.of(controllerScene::setDensityFilter));
                break;
            default:
                break;
        }
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
