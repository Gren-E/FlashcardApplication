package com.fa.util.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RoundRectButton extends HighlightedButton {

    public RoundRectButton(String text) {
        super(text);
    }

    public RoundRectButton() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = Math.max(getWidth(), getMinimumSize().width);
        int height = Math.max(getHeight(), getMinimumSize().height);
        if (width <= 10 || height <= 10) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color mainColor = getBackground();
        Color enabledColor = new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 170);
        Color disabledColor = new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 200);

        g2.setColor(isEnabled() ? enabledColor : disabledColor);
        g2.fillRoundRect(0, 0, width, height, 25, 25);

        g2.setColor(mainColor);
        g2.fillRoundRect(5, 0, width - 10, height - 8, 25, 25);
    }

}
