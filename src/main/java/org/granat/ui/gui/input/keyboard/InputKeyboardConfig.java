package org.granat.ui.gui.input.keyboard;

import lombok.Getter;
import org.granat.ui.gui.input.keyboard.InputKeyboardBinding;

import java.util.HashMap;
import java.util.Map;

public class InputKeyboardConfig {
    //Сопоставление номера элемента управления с номером кнопки
    @Getter
    private Map<Integer, InputKeyboardBinding> inputMapping = new HashMap<>();

    public InputKeyboardConfig() {

    }
}