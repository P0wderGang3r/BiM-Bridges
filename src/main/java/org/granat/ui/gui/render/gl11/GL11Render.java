package org.granat.ui.gui.render.gl11;

import org.granat.controller.gui.ControllerCamera;
import org.granat.controller.scene.ControllerScene;
import org.granat.ui.gui.render.IRender;
import org.granat.ui.gui.render.Window;
import org.granat.ui.gui.render.gl11.utils.*;
import org.lwjgl.opengl.*;

import java.util.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

public class GL11Render implements IRender {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    private final Window window;

    private final ControllerScene controllerScene;

    private final ControllerCamera controllerCamera;

    private final IGL11Transform rotation = GL11Rotation::rotate;

    private final IGL11Transform position = GL11Position::position;

    private final IGL11Transform normalize = GL11Normalize::normalize;

    private final IGL11Transform perspective = GL11Perspective::perspective;

    //?---------------------------------------------------------------------------------------------------------INTERNAL

    GL11Point GL11Point = new GL11Point();

    private final double near = -5;

    private final double far = 5;

    //?-----------------------------------------------------------------------------------------------------------------

    public GL11Render(Window window, ControllerScene controllerScene, ControllerCamera controllerCamera) {

        this.window = window;
        this.controllerScene = controllerScene;
        this.controllerCamera = controllerCamera;

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
        double startTime = System.currentTimeMillis() / 1000.0;
        double currentTime;
        int indexDelta = 2;
        int count = 0;

        double[] sceneCoords = new double[3];
        double[] resultCoords = new double[3];
        double[][] pointModel = GL11Point.getTriangle();

        var fixedParameters = new Object() {
            final double[] currentSceneRotation = new double[3];
            final double[] currentScenePosition = new double[3];
            double fov = 90;
        };

        while ( !glfwWindowShouldClose(window.getWindow()) ) {
            if (!controllerScene.isEmpty()) continue;

            for (int index = 0; index < 3; index++) {
                fixedParameters.currentSceneRotation[index] = controllerScene.getRotation()[index];
                fixedParameters.currentScenePosition[index] = controllerScene.getPosition()[index];
            }

            fixedParameters.fov = controllerCamera.getFov();

            window.getSizeHandler().handle();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            int finalIndexDelta = indexDelta;
            controllerScene.getPointClouds().forEach(pointCloud -> {
                double[][] points = pointCloud.getPoints();
                double[][] colors = pointCloud.getColors();
                for (int index = 0; index < pointCloud.getAmount(); index += finalIndexDelta) {
                    sceneCoords[0] = points[index][0];
                    sceneCoords[1] = points[index][1];
                    sceneCoords[2] = points[index][2];

                    rotation.transform(sceneCoords, fixedParameters.currentSceneRotation);
                    position.transform(sceneCoords, fixedParameters.currentScenePosition);
                    normalize.transform(sceneCoords, new double[] {controllerScene.getMaxBoundDivider()});
                    perspective.transform(sceneCoords, new double[] {
                            window.getWidth(),
                            window.getHeight(),
                            near, far, fixedParameters.fov
                    });

                    GL11.glBegin(GL11.GL_TRIANGLES);
                    for (int vertex = 0; vertex < 3; vertex++) {
                        resultCoords[0] = pointModel[vertex][0] + sceneCoords[0];
                        resultCoords[1] = pointModel[vertex][1] + sceneCoords[1];
                        resultCoords[2] = pointModel[vertex][2] + sceneCoords[2];

                        //System.out.println(data[0] + " " + data[1] + " " + data[2] + " " + data[3]);
                        GL11.glColor3d(colors[index][0], colors[index][1], colors[index][2]);
                        GL11.glVertex3d(
                                resultCoords[0],
                                resultCoords[1],
                                resultCoords[2]
                        );
                    }
                    GL11.glEnd();
                }
            });

            count += 1;
            currentTime = System.currentTimeMillis() / 1000.0;
            if (currentTime >= startTime + 0.1) {
                if (count < 4) indexDelta *= 2;
                if (count > 6) indexDelta /= 2;
                startTime = currentTime;
                count = 0;
            }

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
