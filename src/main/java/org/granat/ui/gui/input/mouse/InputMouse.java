package org.granat.ui.gui.input.mouse;

import lombok.Getter;
import org.granat.ui.gui.render.Window;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InputMouse {
    private final double[] previousCursorPos, currentCursorPos;

    @Getter
    private final double[] deltaCursorPos;

    private final double[] currentScrollPos;

    private final double[] deltaScrollPos;

    private boolean inWindow;

    @Getter
    private Map<Integer, Boolean> keys = new HashMap<>();

    public InputMouse(Window window) {
        this.previousCursorPos = new double[] {-1, -1};
        this.currentCursorPos = new double[] {0, 0};
        this.deltaCursorPos = new double[] {0, 0};
        this.currentScrollPos = new double[] {0, 0};
        this.deltaScrollPos = new double[] {0, 0};

        GLFW.glfwSetCursorPosCallback(window.getWindow(), (windowHandler, posX, posY) -> {
            currentCursorPos[0] = posX;
            currentCursorPos[1] = posY;
        });

        GLFW.glfwSetCursorEnterCallback(window.getWindow(), (windowHandler, entered) -> {
            inWindow = entered;
        });

        GLFW.glfwSetMouseButtonCallback(window.getWindow(), (windowHandler, key, action, mods) -> {
            if (action == GLFW.GLFW_PRESS)
                keys.put(key, true);
            if (action == GLFW.GLFW_RELEASE)
                keys.put(key, false);
        });

        GLFW.glfwSetScrollCallback(window.getWindow(), (windowHandler, xRoll, yRoll) -> {
            this.currentScrollPos[0] += xRoll;
            this.currentScrollPos[1] += yRoll;
        });
    }

    public void movement() {
        deltaCursorPos[0] = 0;
        deltaCursorPos[1] = 0;
        if (previousCursorPos[0] > 0 && previousCursorPos[1] > 0 && inWindow) {
            deltaCursorPos[0] = currentCursorPos[0] - previousCursorPos[0];
            deltaCursorPos[1] = -(currentCursorPos[1] - previousCursorPos[1]);
        }
        previousCursorPos[0] = currentCursorPos[0];
        previousCursorPos[1] = currentCursorPos[1];
    }

    public double[] getDeltaScrollPos() {
        this.deltaScrollPos[0] = this.currentScrollPos[0];
        this.deltaScrollPos[1] = this.currentScrollPos[1];
        this.currentScrollPos[0] = 0;
        this.currentScrollPos[1] = 0;

        return this.deltaScrollPos;
    }
}

