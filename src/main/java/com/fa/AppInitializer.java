package com.fa;

import com.fa.gui.AppColorPalette;
import com.fa.gui.AppFonts;
import com.fa.gui.AppWindow;

import java.awt.EventQueue;
import java.io.File;

public class AppInitializer {

    public static void main(String[] args) {

        AppEnv.setDataDirectory(new File("C:\\Workspace\\Java\\FlashcardApplication\\src\\main\\resources\\data"));
        AppFonts.initializeFonts();
        AppColorPalette.initializeColorPalette();

        EventQueue.invokeLater(() -> {
            AppWindow window = new AppWindow();
        });
    }

}
