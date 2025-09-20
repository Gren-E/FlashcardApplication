package com.fa.util.gui;

import com.fa.AppEnv;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.File;

public class WindowUtil {

    public static File selectFile(Component parent, String... extensions) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                null, extensions);
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(AppEnv.getDataDirectory());

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }

}
