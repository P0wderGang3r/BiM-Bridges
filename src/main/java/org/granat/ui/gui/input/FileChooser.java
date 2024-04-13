package org.granat.ui.gui.input;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FileChooser {

    //?-----------------------------------------------------------------------------------------------------------PUBLIC

    /**
     * Извлекает путь до нового файла
     * @return полный путь до файла в файловой системе
     */
    public String getFilePath() {
        JFrame frame = new JFrame();
        FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        fd.setFile("*.e57");
        fd.setVisible(true);
        frame.dispose();
        String filename = fd.getFile();
        System.out.println(
                Objects.requireNonNullElse(filename, "Отказ от выбора файла.")
        );

        return filename;
    }
}
