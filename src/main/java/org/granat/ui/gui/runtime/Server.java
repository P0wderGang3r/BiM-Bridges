package org.granat.ui.gui.runtime;


import org.granat.controller.gui.ControllerInput;
import org.granat.controller.gui.ControllerOutput;
import org.granat.ui.gui.render.Window;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Server implements Runnable {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final Window window;

    private final ControllerInput controllerInput;

    private final ControllerOutput controllerOutput;

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    private final int tickrate = 100;

    private final int delta = 1000 / tickrate;

    //?------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public Server(
            Window window,
            ControllerInput controllerInput,
            ControllerOutput controllerOutput) {
        this.window = window;
        this.controllerInput = controllerInput;
        this.controllerOutput = controllerOutput;
    }

    //?----------------------------------------------------------------------------------------------------------RUNTIME

    Runnable nextTick = new Runnable() {
        @Override
        public void run() {
            controllerInput.getKeyboardConfig().forEach((key, value) -> {
                if (controllerInput.getKeyboardKeys().get(key) != null) {
                    if (controllerInput.getKeyboardKeys().get(key)) {
                        controllerOutput.doKeyboardInput(value);
                    }
                }
            });
            controllerInput.getMouseConfig().forEach((key, value) -> {
                if (controllerInput.getMouseKeys().get(key) != null) {
                    if (controllerInput.getMouseKeys().get(key)) {
                        controllerOutput.doMouseCursorInput(
                                value, controllerInput.getMouseCursorMovement(), controllerInput.getMouseSensitivity()
                        );
                    }
                }
                switch (key) {
                    case -1 -> controllerOutput.doMouseScrollInput(value, controllerInput.getMouseScrollMovement());
                    default -> { }
                }
            });
        }
    };

    @Override
    public void run() {
        while ( !glfwWindowShouldClose(window.getWindow()) ) {
            try { Thread.sleep(delta); }
            catch (Exception ignored) { }
            new Thread(nextTick).start();
        }
    }
}
