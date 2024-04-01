package org.granat.ui.gui.input;

import lombok.Getter;
import org.granat.ui.gui.input.keyboard.InputKeyboard;
import org.granat.ui.gui.input.mouse.InputMouse;
import org.granat.ui.gui.render.Window;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class InputThread implements Runnable {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final Window window;

    @Getter
    private final InputKeyboard inputKeyboard;

    @Getter
    private final InputMouse inputMouse;


    //?---------------------------------------------------------------------------------------------------------INTERNAL

    private final int tickrate = 100;

    private final int delta = 1000 / tickrate;

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public InputThread(Window window, InputKeyboard inputKeyboard, InputMouse inputMouse) {
        this.window = window;
        this.inputKeyboard = inputKeyboard;
        this.inputMouse = inputMouse;
    }

    //?----------------------------------------------------------------------------------------------------THREAD WORKER

    @Override
    public void run() {
        while ( !glfwWindowShouldClose(window.getWindow()) ) {
            try {
                Thread.sleep(delta);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            GLFW.glfwPollEvents();
            inputMouse.movement();
        }
    }
}
