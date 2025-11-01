package com.fa.gui;

import java.awt.Color;

public class AppColorPalette {

    private static Color accentForeground;
    private static Color secondaryForeground;

    private static Color buttonBackground;
    private static Color buttonFailBackground;
    private static Color buttonSuccessBackground;

    private static Color accentBackground;
    private static Color contentBackground;
    private static Color secondaryBackground;

    private static Color transparency;

    public static void initializeColorPalette() {
        accentForeground = Color.WHITE;
        secondaryForeground = Color.GRAY;

        buttonBackground = Color.DARK_GRAY;
        buttonFailBackground = new Color(60,0,0);
        buttonSuccessBackground = new Color(0,50,0);

        accentBackground = Color.BLACK;
        contentBackground = Color.LIGHT_GRAY;
        secondaryBackground = Color.GRAY;
        transparency = new Color(0, 0, 0, 0);
    }

    public static Color getAccentForeground() {
        return accentForeground;
    }

    public static Color getSecondaryForeground() {
        return secondaryForeground;
    }

    public static Color getButtonBackground() {
        return buttonBackground;
    }

    public static Color getButtonFailBackground() {
        return buttonFailBackground;
    }

    public static Color getButtonSuccessBackground() {
        return buttonSuccessBackground;
    }

    public static Color getAccentBackground() {
        return accentBackground;
    }

    public static Color getContentBackground() {
        return contentBackground;
    }

    public static Color getSecondaryBackground() {
        return secondaryBackground;
    }

    public static Color getTransparency() {
        return transparency;
    }

}
