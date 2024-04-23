package org.granat.controller.gui;


import lombok.Getter;
import org.granat.ui.gui.input.*;
import org.granat.ui.gui.input.keyboard.InputKeyboard;
import org.granat.ui.gui.input.keyboard.InputKeyboardBinding;
import org.granat.ui.gui.input.keyboard.InputKeyboardConfig;
import org.granat.ui.gui.input.mouse.InputMouse;
import org.granat.ui.gui.input.mouse.InputMouseBinding;
import org.granat.ui.gui.input.mouse.InputMouseConfig;

import java.util.Map;

public record ControllerInput(
        @Getter InputKeyboard inputKeyboard,
        @Getter InputMouse inputMouse,
        InputKeyboardConfig inputKeyboardConfig,
        InputMouseConfig inputMouseConfig
) {

    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    //----------------------------------------------------------------------------------------------------------KEYBOARD

    public Map<Integer, InputKeyboardBinding> getKeyboardConfig() {
        return this.inputKeyboardConfig.getInputMapping();
    }

    public Map<Integer, Boolean> getKeyboardKeys() {
        return this.inputKeyboard.getKeys();
    }

    //-------------------------------------------------------------------------------------------------------------MOUSE

    public Map<Integer, Boolean> getMouseKeys() {
        return this.inputMouse.getKeys();
    }

    public Map<Integer, InputMouseBinding> getMouseConfig() {
        return this.inputMouseConfig.getInputMapping();
    }

    public double getMouseSensitivity() {
        return this.inputMouseConfig.getSensitivity();
    }

    public double[] getMouseCursorMovement() {
        return this.inputMouse.getDeltaCursorPos();
    }

    public double[] getMouseScrollMovement() {
        return this.inputMouse.getDeltaScrollPos();
    }
}
