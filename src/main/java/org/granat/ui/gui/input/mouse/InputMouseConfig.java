package org.granat.ui.gui.input.mouse;

import lombok.Getter;
import org.granat.ui.gui.input.mouse.InputMouseBinding;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InputMouseConfig {
    //Сопоставление номера элемента управления с номером кнопки
    @Getter
    private final Map<Integer, InputMouseBinding> inputMapping = new HashMap<>();

    @Getter
    private double sensitivity;

    public InputMouseConfig() {
        //перемещение в плоскости oXY
        inputMapping.put(GLFW.GLFW_MOUSE_BUTTON_1, InputMouseBinding.ROTATION);
        //поворот в осях oX и oY
        inputMapping.put(GLFW.GLFW_MOUSE_BUTTON_2, InputMouseBinding.POSITION);
        //Изменение FOV
        inputMapping.put(-1, InputMouseBinding.ZOOM);
        //Чувствительность ввода мыши
        sensitivity = 0.005;
    }
}
