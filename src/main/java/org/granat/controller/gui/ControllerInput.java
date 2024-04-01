package org.granat.controller.gui;


import org.granat.ui.gui.input.*;
import org.granat.ui.gui.input.keyboard.InputKeyboardBinding;
import org.granat.ui.gui.input.keyboard.InputKeyboardConfig;
import org.granat.ui.gui.input.mouse.InputMouseBinding;
import org.granat.ui.gui.input.mouse.InputMouseConfig;

import java.util.Map;

public class ControllerInput {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final InputThread inputThread;

    private final InputKeyboardConfig inputKeyboardConfig;

    private final InputMouseConfig inputMouseConfig;

    //?---------------------------------------------------------------------------------------------------------INTERNAL


    //?------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public ControllerInput(
            InputThread inputThread,
            InputKeyboardConfig inputKeyboardConfig,
            InputMouseConfig inputMouseConfig
    ) {
        this.inputThread = inputThread;
        this.inputKeyboardConfig = inputKeyboardConfig;
        this.inputMouseConfig = inputMouseConfig;
    }

    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    //----------------------------------------------------------------------------------------------------------KEYBOARD

    public Map<Integer, InputKeyboardBinding> getKeyboardConfig() {
        return this.inputKeyboardConfig.getInputMapping();
    }

    public Map<Integer, Boolean> getKeyboardKeys() {
        return this.inputThread.getInputKeyboard().getKeys();
    }

    //-------------------------------------------------------------------------------------------------------------MOUSE

    public Map<Integer, Boolean> getMouseKeys() {
        return this.inputThread.getInputMouse().getKeys();
    }

    public Map<Integer, InputMouseBinding> getMouseConfig() {
        return this.inputMouseConfig.getInputMapping();
    }

    public double getMouseSensitivity() {
        return this.inputMouseConfig.getSensitivity();
    }

    public double[] getMouseCursorMovement() {
        return this.inputThread.getInputMouse().getDeltaCursorPos();
    }

    public double[] getMouseScrollMovement() {
        return  this.inputThread.getInputMouse().getDeltaScrollPos();
    }
}
