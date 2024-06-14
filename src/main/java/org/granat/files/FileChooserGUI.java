package org.granat.files;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FileChooserGUI implements IFileChooser {

    //?-----------------------------------------------------------------------------------------------------------PUBLIC

    /**
     * Извлекает путь до нового файла
     * @return полный путь до файла в файловой системе
     */
    @Override
    public String getFilePath() {
        JFrame frame = new JFrame();
        FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        fd.setFile("*.e57");
        fd.setVisible(true);
        frame.dispose();
        String filename = fd.getDirectory() + fd.getFile();
        System.out.println(
                Objects.requireNonNullElse(filename, "Отказ от выбора файла.")
        );
        return filename;
    }
}
