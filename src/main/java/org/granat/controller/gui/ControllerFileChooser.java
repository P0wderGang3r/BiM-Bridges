package org.granat.controller.gui;

import lombok.Getter;
import org.granat.ui.gui.input.FileChooser;

public class ControllerFileChooser {

    //?---------------------------------------------------------------------------------------------------------EXTERNAL

    @Getter
    private final FileChooser fileChooser;

    //?---------------------------------------------------------------------------------------------------------INTERNAL


    //?------------------------------------------------------------------------------------------------------CONSTRUCTOR


    public ControllerFileChooser(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    public String getFilePath() {
        return fileChooser.getFilePath();
    }
}
