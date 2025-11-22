package com.fa.gui;

import com.fa.AppEnv;
import org.apache.log4j.Logger;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class AppFonts {

    private static final Logger LOG = Logger.getLogger(AppFonts.class);

    private static Font standardContentFont;
    private static Font titleFont;
    private static Font gigaFont;
    private static Font symbolFont;

    public static void initializeFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(AppEnv.getFontDirectory(), "Genaminto-Regular.otf")));
        } catch (IOException | FontFormatException e) {
            LOG.error("Could not create font.");
        }
        standardContentFont = new Font("Genaminto", Font.PLAIN, 14);
        titleFont = new Font("Genaminto", Font.BOLD, 22);
        gigaFont = new Font("Genaminto", Font.BOLD, 56);
        symbolFont = new Font("Dialog", Font.PLAIN, 12);
    }

    public static Font getStandardContentFont() {
        return standardContentFont;
    }

    public static Font getTitleFont() {
        return titleFont;
    }

    public static Font getGigaFont() {
        return gigaFont;
    }

    public static Font getSymbolFont() {
        return symbolFont;
    }

}
