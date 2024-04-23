package org.granat.ui.gui.input.keyboard;

import lombok.Getter;
import org.granat.ui.gui.input.keyboard.InputKeyboardBinding;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InputKeyboardConfig {
    //Сопоставление номера элемента управления с номером кнопки
    @Getter
    private Map<Integer, InputKeyboardBinding> inputMapping = new HashMap<>();

    public InputKeyboardConfig() {
        inputMapping.put(GLFW.GLFW_KEY_1, InputKeyboardBinding.LOAD_FILE);
        inputMapping.put(GLFW.GLFW_KEY_2, InputKeyboardBinding.FILTER_DENSITY);
    }
}