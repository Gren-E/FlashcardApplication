package com.fa.util.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class GraphicsUtil {

    public final static int CENTER = 0;
    public final static int LEFT = 1;
    public final static int RIGHT = 2;

    public static void drawString(String text, Rectangle rect, Font font, int horizontalAlign, int padx, Graphics2D g) {
        FontMetrics metrics = g.getFontMetrics(font);

        int x = switch (horizontalAlign) {
            case CENTER -> rect.x + (rect.width - metrics.stringWidth(text)) / 2;
            case LEFT -> rect.x + padx;
            case RIGHT -> rect.width - metrics.stringWidth(text) - padx;
            default -> throw new IllegalArgumentException("Wrong horizontalAlign parameter value.");
        };

        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    public static void drawRect(Rectangle rect, Graphics2D g2) {
        g2.drawRect(rect.x, rect.y ,rect.width, rect.height);
    }

    public static void fillRect(Rectangle rect, Graphics2D g2) {
        g2.fillRect(rect.x, rect.y ,rect.width, rect.height);
    }

    public static void drawImage(Image image, Rectangle rect, Graphics2D g2) {
        g2.drawImage(image, rect.x, rect.y, null);
    }

}
