package org.granat.ui.gui.runtime;


import org.granat.controller.gui.ControllerInput;
import org.granat.controller.gui.ControllerOutput;
import org.granat.controller.scene.ControllerScene;
import org.granat.ui.gui.render.Window;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Server implements Runnable {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final Window window;

    private final ControllerScene controllerScene;

    private final ControllerInput controllerInput;

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    private final int tickrate = 100;

    private final int delta = 1000 / tickrate;

    //?------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public Server(
            Window window,
            ControllerScene controllerScene,
            ControllerInput controllerInput) {
        this.window = window;
        this.controllerScene = controllerScene;
        this.controllerInput = controllerInput;
    }

    //?----------------------------------------------------------------------------------------------------------RUNTIME

    Runnable nextTick = new Runnable() {
        @Override
        public void run() {
            controllerInput.getKeyboardConfig().forEach((key, value) -> {
                if (controllerInput.getKeyboardKeys().get(key) != null) {
                    //Сделать что-то продолжительное на сервере при нажатии по кнопке
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
