package com.fa.util.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class EmbellishedStringIcon extends IconButton {

    private String text;
    private Color textColor;

    public EmbellishedStringIcon(Image icon, int width, int height) {
        super(icon, width, height);
        textColor = getForeground();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColor(Color color) {
        textColor = color;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(getFont());
        g2.setColor(textColor);

        int width = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (getWidth() - width) / 2, getHeight() / 2);
    }

}
