package org.granat.controller.gui;

import org.granat.files.IFileChooser;

public record ControllerFileChooser(
        IFileChooser fileChooser
) {

    //?--------------------------------------------------------------------------------------------------------FUNCTIONS

    public String getFilePath() {
        return fileChooser.getFilePath();
    }
}
