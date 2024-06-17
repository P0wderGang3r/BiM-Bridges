package org.granat.ui.gui.input.keyboard;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InputKeyboardConfig {
    //Сопоставление номера элемента управления с номером кнопки
    @Getter
    private Map<Integer, InputKeyboardBinding> inputMapping = new HashMap<>();

    public InputKeyboardConfig() {
        inputMapping.put(GLFW.GLFW_KEY_Q, InputKeyboardBinding.LOAD_FILE);

        inputMapping.put(GLFW.GLFW_KEY_W, InputKeyboardBinding.FILTER_DENSITY);
        inputMapping.put(GLFW.GLFW_KEY_E, InputKeyboardBinding.FILTER_SUPERSTRUCTURES);
        inputMapping.put(GLFW.GLFW_KEY_R, InputKeyboardBinding.FILTER_GIRDERS);

        inputMapping.put(GLFW.GLFW_KEY_T, InputKeyboardBinding.ALGO_DEFLECTION);

        inputMapping.put(GLFW.GLFW_KEY_1, InputKeyboardBinding.SHOW_INTENSITY);
        inputMapping.put(GLFW.GLFW_KEY_2, InputKeyboardBinding.SHOW_DENSITY);
        inputMapping.put(GLFW.GLFW_KEY_3, InputKeyboardBinding.SHOW_SUPERSTRUCTURES);
        inputMapping.put(GLFW.GLFW_KEY_4, InputKeyboardBinding.SHOW_GIRDERS);
        inputMapping.put(GLFW.GLFW_KEY_5, InputKeyboardBinding.SHOW_GIRDERS_HEIGHT);

        inputMapping.put(GLFW.GLFW_KEY_Z, InputKeyboardBinding.SHOW_FILTER_EMPTY);
        inputMapping.put(GLFW.GLFW_KEY_X, InputKeyboardBinding.SHOW_FILTER_DENSITY);
        inputMapping.put(GLFW.GLFW_KEY_C, InputKeyboardBinding.SHOW_FILTER_GIRDERS_HEIGHT);
    }
}