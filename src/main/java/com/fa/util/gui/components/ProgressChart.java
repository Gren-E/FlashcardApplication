package com.fa.util.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ProgressChart extends DefaultProgress {

    private int diameter;

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

        diameter = (diameter == 0) ? Math.min(width, height) - 10 : Math.min(Math.min(width, height) - 10, diameter);
        int archAngle = (maxValue != 0) ? (int)(getProgressRatio() * 360) : 0;

        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        g2.setColor(new Color(progressColor.getRed(), progressColor.getGreen(), progressColor.getBlue(), 50));
        g2.fillOval(x, y, diameter, diameter);

        g2.setColor(progressColor);
        g2.fillArc(x, y, diameter, diameter, 90, - archAngle);

        int innerDiameter = diameter * 2/3;
        int innerX = x + diameter / 6;
        int innerY = y + diameter / 6;

        g2.setColor(getBackground());
        g2.fillOval(innerX, innerY, innerDiameter, innerDiameter);

        String value = currentValue + "/" + maxValue;

        g2.setColor(getForeground());
        g2.setFont(getFont());
        int stringWidth = g2.getFontMetrics().stringWidth(value);
        int stringHeight = g2.getFontMetrics().getHeight();
        if (stringWidth < innerDiameter) {
            g2.drawString(value,innerX + (innerDiameter - stringWidth) / 2 , innerY + (innerDiameter / 2) + (stringHeight / 4));
        }
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

}
