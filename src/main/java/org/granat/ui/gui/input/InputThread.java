package org.granat.ui.gui.input;

import lombok.Getter;
import org.granat.controller.gui.ControllerInput;
import org.granat.controller.gui.ControllerOutput;
import org.granat.ui.gui.input.keyboard.InputKeyboard;
import org.granat.ui.gui.input.mouse.InputMouse;
import org.granat.ui.gui.render.Window;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class InputThread implements Runnable {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final Window window;

    @Getter
    private final ControllerInput controllerInput;

    @Getter
    private final ControllerOutput controllerOutput;

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    private final int tickrate = 250;

    private final int delta = 1000 / tickrate;

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public InputThread(
            Window window,
            ControllerInput controllerInput,
            ControllerOutput controllerOutput
    ) {
        this.window = window;
        this.controllerInput = controllerInput;
        this.controllerOutput = controllerOutput;
    }

    //?----------------------------------------------------------------------------------------------------THREAD WORKER

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
            try {
                Thread.sleep(delta);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            controllerInput.getInputMouse().movement();
            GLFW.glfwPollEvents();
            new Thread(nextTick).start();
        }
    }
}
