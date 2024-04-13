package org.granat;

import org.granat.controller.gui.ControllerFileChooser;
import org.granat.controller.gui.ControllerInput;
import org.granat.controller.gui.ControllerOutput;
import org.granat.controller.gui.ControllerCamera;
import org.granat.controller.scene.ControllerScene;
import org.granat.scene.Scene;
import org.granat.ui.gui.input.*;
import org.granat.ui.gui.input.keyboard.InputKeyboard;
import org.granat.ui.gui.input.keyboard.InputKeyboardConfig;
import org.granat.ui.gui.input.mouse.InputMouse;
import org.granat.ui.gui.input.mouse.InputMouseConfig;
import org.granat.ui.gui.render.IRender;
import org.granat.ui.gui.render.Window;
import org.granat.ui.gui.render.gl11.GL11Render;
import org.granat.ui.gui.runtime.Server;
import org.granat.ui.gui.user.Camera;

public class GUIApplication {

    //?--------------------------------------------------------------------------------------------------OpenGL Renderer

    private final IRender render;

    //?-----------------------------------------------------------------------------------------------------------Server

    private final Server server;

    //?------------------------------------------------------------------------------------------------------------INPUT

    private final InputThread inputThread;

    //?------------------------------------------------------------------------------------------------------CONTROLLERS

    private final ControllerScene controllerScene;

    private final ControllerCamera controllerCamera;

    private final ControllerInput controllerInput;

    private final ControllerOutput controllerOutput;

    private final ControllerFileChooser controllerFileChooser;

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public GUIApplication() {
        Window window = new Window();

        //Создание контроллера всей сцены
        Scene scene = new Scene();
        this.controllerScene = new ControllerScene(scene);

        //Создание контроллера направления пользовательского взгляда
        Camera camera = new Camera();
        this.controllerCamera = new ControllerCamera(camera);

        //Создание контроллера ввода/вывода для мыши и клавиатуры
        InputKeyboardConfig inputKeyboardConfig = new InputKeyboardConfig();
        InputMouseConfig inputMouseConfig = new InputMouseConfig();
        InputKeyboard inputKeyboard = new InputKeyboard(window);
        InputMouse inputMouse = new InputMouse(window);
        this.controllerInput = new ControllerInput(inputKeyboard, inputMouse, inputKeyboardConfig, inputMouseConfig);
        this.controllerOutput = new ControllerOutput(this.controllerScene, this.controllerCamera);

        //Создание нового обработчика пути до файла в файловой системе
        FileChooser fileChooser = new FileChooser();
        this.controllerFileChooser = new ControllerFileChooser(fileChooser);

        //Создаём новое окно отрисовки
        this.render = new GL11Render(window, controllerScene, controllerCamera);
        //Создаём пользовательский сервер
        this.server = new Server(window, controllerScene, controllerInput);
        //Создаём сервер ввода-вывода
        this.inputThread = new InputThread(window, controllerInput, controllerOutput);

        new Thread(this.inputThread).start();
        new Thread(this.server).start();

        String filename;
        do {
            filename = fileChooser.getFilePath();
        } while (filename == null || filename.isEmpty());

        //this.controllerScene.setWrapper("Хабаровск 51_руч.Чистый.e57");
        //this.controllerScene.setWrapper("Хабаровск 69_Гобилли1.e57");
        //this.controllerScene.setWrapper("Ноябрьск объездная-853.e57");
        //this.controllerScene.setWrapper("Ноябрьск путепровод-779.e57");

        this.controllerScene.setWrapper(filename);
        this.controllerScene.pullData();
        this.controllerScene.pullBounds();
    }

    //?-----------------------------------------------------------------------------------------------------------------

    public void run() {
        render.run();
    }

    //?-----------------------------------------------------------------------------------------------------------------

}
