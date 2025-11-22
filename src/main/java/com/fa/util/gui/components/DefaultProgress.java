package com.fa.util.gui.components;

import javax.swing.JPanel;
import java.awt.Color;

public abstract class DefaultProgress extends JPanel {

    protected int maxValue;
    protected int currentValue;

    protected Color progressColor;

    public DefaultProgress() {
        this(0);
    }

    public DefaultProgress(int maxValue) {
        this(0, maxValue);
    }

    public DefaultProgress(int currentValue, int maxValue) {
        setOpaque(false);

        this.currentValue = currentValue;
        this.maxValue = maxValue;
        progressColor = Color.DARK_GRAY;
    }

    public float getProgressRatio() {
        return (float) Math.min(currentValue, maxValue) / maxValue;
    }

    public void setProgressColor(Color color) {
        progressColor = color;
    }

    public void setMaxValue(int value) {
        maxValue = value;
        repaint();
    }

    public void setCurrentValue(int value) {
        currentValue = value;
        repaint();
    }

    public int getCurrentValue() {
        return currentValue;
    }

}
