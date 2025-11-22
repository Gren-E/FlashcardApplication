package com.fa.gui;

import java.awt.Color;

public class AppColorPalette {

    private static Color accentForeground;
    private static Color contentForeground;
    private static Color secondaryForeground;

    private static Color buttonBackground;
    private static Color buttonFailBackground;
    private static Color buttonSuccessBackground;

    private static Color accentBackground;
    private static Color contentBackground;
    private static Color secondaryBackground;

    private static Color transparency;
    private static Color progressColor;
    private static Color achievementColor;
    private static Color failColor;
    private static Color successColor;

    private static Color goldColor;
    private static Color silverColor;
    private static Color bronzeColor;

    public static void initializeColorPalette() {
        accentForeground = Color.WHITE;
        contentForeground = new Color(50,50,50);
        secondaryForeground = Color.GRAY;

        buttonBackground = Color.DARK_GRAY;
        buttonFailBackground = new Color(60,10,20);
        buttonSuccessBackground = new Color(20,50,30);

        accentBackground = Color.BLACK;
        contentBackground = Color.LIGHT_GRAY;
        secondaryBackground = new Color(60, 30, 40);

        transparency = new Color(0, 0, 0, 0);
        progressColor = new Color(20, 120, 100);
        achievementColor = new Color(125, 100, 45);
        failColor = new Color(80,20,20);
        successColor = new Color(20,80,40);

        goldColor = new Color(125, 100, 45);
        silverColor = new Color(105, 105, 120);
        bronzeColor = new Color(55, 36, 16);
    }

    public static Color getAccentForeground() {
        return accentForeground;
    }

    public static Color getContentForeground() {
        return contentForeground;
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

    public static Color getProgressColor() {
        return progressColor;
    }

    public static Color getAchievementColor() {
        return achievementColor;
    }

    public static Color getFailColor() {
        return failColor;
    }

    public static Color getSuccessColor() {
        return successColor;
    }

    public static Color getGoldColor() {
        return goldColor;
    }

    public static Color getSilverColor() {
        return silverColor;
    }

    public static Color getBronzeColor() {
        return bronzeColor;
    }

}
