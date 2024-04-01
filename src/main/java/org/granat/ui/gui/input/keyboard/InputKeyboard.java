package org.granat.ui.gui.input.keyboard;

import lombok.Getter;
import org.granat.ui.gui.render.Window;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InputKeyboard {

    @Getter
    private Map<Integer, Boolean> keys = new HashMap<>();

    public InputKeyboard(Window window) {
        GLFW.glfwSetKeyCallback(window.getWindow(), (windowHandler, key, scancode, action, mods) -> {
            if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
                GLFW.glfwSetWindowShouldClose(windowHandler, true);

            if (action == GLFW.GLFW_PRESS)
                keys.put(key, true);
            if (action == GLFW.GLFW_RELEASE)
                keys.put(key, false);
        });
    }
}