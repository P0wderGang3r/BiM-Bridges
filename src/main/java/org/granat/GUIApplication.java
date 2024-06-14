package org.granat;

import org.granat.controller.filters.ControllerFilters;
import org.granat.controller.gui.ControllerCamera;
import org.granat.controller.gui.ControllerFileChooser;
import org.granat.controller.gui.ControllerInput;
import org.granat.controller.gui.ControllerOutput;
import org.granat.controller.scene.ControllerScene;
import org.granat.controller.server.ControllerServer;
import org.granat.controller.wrapper.ControllerWrapper;
import org.granat.files.FileChooserGUI;
import org.granat.files.IFileChooser;
import org.granat.scene.Scene;
import org.granat.ui.gui.input.InputThread;
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

    //?-----------------------------------------------------------------------------------------------------CONSTRUCTORS

    public GUIApplication() {
        //Инициализация нового окна отрисовки
        Window window = new Window();

        //Инициализация контроллера пути до файла в файловой системе
        IFileChooser fileChooser = new FileChooserGUI();
        ControllerFileChooser controllerFileChooser = new ControllerFileChooser(fileChooser);

        //Инициализация контроллера всей сцены
        Scene scene = new Scene();
        ControllerScene controllerScene = new ControllerScene(scene);

        //Инициализация контроллера оболочки вокруг файла
        ControllerWrapper controllerWrapper = new ControllerWrapper(controllerScene, controllerFileChooser);

        //Инициализация контроллера фильтрации во множестве точек
        ControllerFilters controllerFilters = new ControllerFilters(controllerScene);

        //Инициализация контроллера направления пользовательского взгляда
        Camera camera = new Camera();
        ControllerCamera controllerCamera = new ControllerCamera(camera);

        //Инициализация подсистемы обработки длительных событий
        this.server = new Server(window);
        ControllerServer controllerServer = new ControllerServer(this.server);

        //Инициализация подсистемы ввода-вывода для клавиатуры и мыши
        InputKeyboardConfig inputKeyboardConfig = new InputKeyboardConfig();
        InputMouseConfig inputMouseConfig = new InputMouseConfig();
        InputKeyboard inputKeyboard = new InputKeyboard(window);
        InputMouse inputMouse = new InputMouse(window);
        ControllerInput controllerInput = new ControllerInput(inputKeyboard, inputMouse, inputKeyboardConfig, inputMouseConfig);
        ControllerOutput controllerOutput = new ControllerOutput(controllerScene, controllerWrapper, controllerCamera, controllerFilters, controllerServer);
        this.inputThread = new InputThread(window, controllerInput, controllerOutput);

        //Инициализация подсистемы графического вывода
        this.render = new GL11Render(window, controllerScene, controllerCamera, controllerServer);

        //Извлекаем сцену из одного файла
        new Thread(controllerWrapper::loadSingleScene).start();
    }

    //?-----------------------------------------------------------------------------------------------------------------

    public void run() {
        //Запуск сервера обработки длительных событий
        Thread server = new Thread(this.server);
        server.start();
        //Запуск подсистемы ввода-вывода
        Thread inputThread = new Thread(this.inputThread);
        inputThread.start();
        //Запуск подсистемы графического вывода
        render.run();

        server.interrupt();
        inputThread.interrupt();
    }

    //?-----------------------------------------------------------------------------------------------------------------
}
