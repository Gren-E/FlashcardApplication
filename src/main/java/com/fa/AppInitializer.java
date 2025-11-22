package com.fa;

import com.fa.gui.AppColorPalette;
import com.fa.gui.AppFonts;
import com.fa.gui.AppWindow;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

public class AppInitializer {

    public static void main(String[] args) throws IOException {
        File resourcesDirectory = new File(System.getProperty("user.dir") + "\\src\\main\\resources");
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            if ("-dir".equals(argument)) {
                resourcesDirectory = new File(args[i + 1]);
                break;
            }
        }

        if (!resourcesDirectory.isDirectory()) {
            throw new IOException(resourcesDirectory + " is not a directory.");
        }
        AppEnv.setDataDirectory(new File(resourcesDirectory, "data"));

        AppFonts.initializeFonts();
        AppColorPalette.initializeColorPalette();

        EventQueue.invokeLater(() -> {
            AppWindow window = new AppWindow();
        });
    }

}
