package org.granat.ui.gui.render.gl11;

import org.granat.controller.gui.ControllerCamera;
import org.granat.controller.scene.ControllerScene;
import org.granat.controller.server.ControllerServer;
import org.granat.ui.gui.render.IRender;
import org.granat.ui.gui.render.Window;
import org.granat.ui.gui.render.gl11.utils.*;
import org.granat.ui.gui.runtime.State;
import org.lwjgl.opengl.*;

import java.util.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

public class GL11Render implements IRender {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final Window window;

    private final ControllerScene controllerScene;

    private final ControllerCamera controllerCamera;

    private final ControllerServer controllerServer;

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    private final IGL11Transform rotation = GL11Rotation::rotate;

    private final IGL11Transform position = GL11Position::position;

    private final IGL11Transform normalize = GL11Normalize::normalize;

    private final IGL11Transform perspective = GL11Perspective::perspective;

    GL11Point GL11Point = new GL11Point();

    private final double near = -5;

    private final double far = 5;

    //?------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public GL11Render(
            Window window,
            ControllerScene controllerScene,
            ControllerCamera controllerCamera,
            ControllerServer controllerServer)
    {
        this.window = window;
        this.controllerScene = controllerScene;
        this.controllerCamera = controllerCamera;
        this.controllerServer = controllerServer;

        GL.createCapabilities();

        GL11.glColor3d(1.0, 1.0, 1.0);

        GL11.glClearColor(0.05f, 0.04f, 0.05f, 0.0f);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LESS);
    }

    //?-----------------------------------------------------------------------------------------------------------------

    public void run() {
        loop();
        stop();
    }

    private void loop() {
        double[] sceneCoords = new double[3];
        double[] resultCoords = new double[3];
        double[] color = new double[3];
        double[][] pointModel = GL11Point.getTriangle();

        GL11Optimization optimization = new GL11Optimization();

        var fixedParameters = new Object() {
            final double[] currentSceneRotation = new double[3];
            final double[] currentScenePosition = new double[3];
            double fov = 90;
        };

        while ( !glfwWindowShouldClose(window.getWindow()) ) {
            if (!controllerScene.isEmptyScene()) continue;
            if (controllerServer.getServer().getCurrentState() == State.READING) continue;

            for (int index = 0; index < 3; index++) {
                fixedParameters.currentSceneRotation[index] = controllerScene.getRotation()[index];
                fixedParameters.currentScenePosition[index] = controllerScene.getPosition()[index];
            }

            fixedParameters.fov = controllerCamera.getFov();

            window.getSizeHandler().handle();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            controllerScene.getDrawablePointsStream().forEach(points -> {
                for (int index = 0; index < points.length; index += optimization.getDelta()) {
                    if (!points[index].getFilterValue(controllerScene.getCurrentFilter())) continue;

                    sceneCoords[0] = points[index].getCoordinates()[0];
                    sceneCoords[1] = points[index].getCoordinates()[1];
                    sceneCoords[2] = points[index].getCoordinates()[2];

                    color[0] = points[index].getColor()[0];
                    color[1] = points[index].getColor()[1];
                    color[2] = points[index].getColor()[2];

                    rotation.transform(sceneCoords, fixedParameters.currentSceneRotation);
                    position.transform(sceneCoords, fixedParameters.currentScenePosition);
                    normalize.transform(sceneCoords, new double[] {controllerScene.getMaxBoundDivider()});
                    perspective.transform(sceneCoords, new double[] {
                            window.getWidth(),
                            window.getHeight(),
                            near, far, fixedParameters.fov
                    });

                    GL11.glBegin(GL11.GL_TRIANGLES);
                    GL11.glColor3d(color[0], color[1], color[2]);
                    for (int vertex = 0; vertex < 3; vertex++) {
                        resultCoords[0] = pointModel[vertex][0] + sceneCoords[0];
                        resultCoords[1] = pointModel[vertex][1] + sceneCoords[1];
                        resultCoords[2] = pointModel[vertex][2] + sceneCoords[2];

                        //System.out.println(data[0] + " " + data[1] + " " + data[2] + " " + data[3]);
                        GL11.glVertex3d(
                                resultCoords[0],
                                resultCoords[1],
                                resultCoords[2]
                        );
                    }
                    GL11.glEnd();
                }
            });

            optimization.nextTick();
            glfwSwapBuffers(window.getWindow());
        }
    }

    private void stop() {
        glfwFreeCallbacks(window.getWindow());
        glfwDestroyWindow(window.getWindow());

        glfwTerminate();
        try {
            Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        } catch (NullPointerException ignored) { }
    }
}
