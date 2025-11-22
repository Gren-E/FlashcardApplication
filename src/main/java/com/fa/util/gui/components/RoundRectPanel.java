package com.fa.util.gui.components;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

public class RoundRectPanel extends JPanel {

    private boolean framed;

    public RoundRectPanel() {
        this(null, false);
    }

    public RoundRectPanel (LayoutManager layout) {
        this(layout, false);
    }

    public RoundRectPanel (boolean framed) {
        this(null, framed);
    }

    public RoundRectPanel (LayoutManager layout, boolean framed) {
        super(layout);
        setOpaque(false);
        setBorder(new EmptyBorder(5,5,7,5));

        this.framed = framed;
    }

    public void setFramed(boolean framed) {
        this.framed = framed;
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

        if (framed) {
            Color shadowFrame = new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue(), 170);
            g2.setColor(shadowFrame);
            g2.fillRoundRect(0, 0, width, height, 40, 40);
        }

        g2.setColor(getBackground());
        g2.fillRoundRect(5, 5, width - 10, height - 12, 40, 40);
    }

}
