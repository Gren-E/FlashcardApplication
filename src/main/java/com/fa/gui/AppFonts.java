package com.fa.gui;

import java.awt.Font;

public class AppFonts {

    private static Font standardContentFont;
    private static Font titleFont;

    public static void initializeFonts() {
        standardContentFont = new Font("Bauhaus 93", Font.PLAIN, 16);
        titleFont = new Font("Bauhaus 93", Font.BOLD, 22);
    }

    public static Font getStandardContentFont() {
        return standardContentFont;
    }

    public static Font getTitleFont() {
        return titleFont;
    }
}
