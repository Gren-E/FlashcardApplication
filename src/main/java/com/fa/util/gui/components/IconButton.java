package com.fa.util.gui.components;

import com.fa.util.gui.GraphicsUtil;
import com.fa.util.gui.ImageUtil;
import com.fa.util.listeners.ConsumerMulticaster;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

public class IconButton extends JPanel {

    private Image icon;
    private Image standardIcon;
    private Image highlightIcon;

    private final int width;
    private final int height;
    private int padx;
    private int pady;

    private Color standardColor;
    private Color highlightColor;
    private boolean doHighlight;
    private boolean highlightOn;

    private final ConsumerMulticaster<MouseEvent> mouseClickActions;

    public IconButton(Image icon, int width, int height) {
        this.icon = icon;
        this.width = width;
        this.height = height;

        this.standardColor = new Color(0, 0, 255);
        this.highlightColor = new Color(255, 255, 255);

        this.mouseClickActions = new ConsumerMulticaster<>();

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                handleMouseClick(e);
            }
            public void mouseEntered(MouseEvent e) {
                if (highlightOn) {
                    highlight(true);
                }
            }
            public void mouseExited(MouseEvent e) {
                highlight(false);
            }
        });

        setOpaque(false);
        build();
    }

    public void build() {
        if(this.icon == null) {
            return;
        }

        this.standardIcon = ImageUtil.replaceColor(this.icon, new Color(0, 0, 255), this.standardColor, 10);
        this.standardIcon = ImageUtil.resize(this.standardIcon, this.width, this.height);

        this.highlightIcon = ImageUtil.replaceColor(this.icon, new Color(0, 0, 255), this.highlightColor, 10);
        this.highlightIcon = ImageUtil.resize(this.highlightIcon, this.width, this.height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon == null) {
            return;
        }

        int x = Math.max((getWidth() - width) / 2, 0);
        int y = Math.max((getHeight() - height) / 2, 0);

        Graphics2D g2 = (Graphics2D) g;
        Image imageToDraw = this.doHighlight ? this.highlightIcon : this.standardIcon;
        GraphicsUtil.drawImage(imageToDraw, new Rectangle(x, y, this.width, this.height), g2);
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        repaint();
    }

    public void setForeground(Color color) {
        this.standardColor = color;
        if(this.icon == null) {
            return;
        }

        build();
        repaint();
    }

    public void setHighlightColor(Color color) {
        this.highlightColor = color;
        if(this.icon == null) {
            return;
        }

        build();
        repaint();
    }

    public void highlight(boolean doHighlight) {
        this.doHighlight = doHighlight;
        repaint();
    }

    public void setHighlight(boolean highlightOn) {
        this.highlightOn = highlightOn;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Image getIcon() {
        return this.standardIcon;
    }

    public void handleMouseClick(MouseEvent e) {
        this.mouseClickActions.alert(e);
    }

    public void addMouseClickAction(Consumer<MouseEvent> action) {
        this.mouseClickActions.addListener(action);
    }

    public boolean removeMouseClickAction(Consumer<MouseEvent> action) {
        return this.mouseClickActions.removeListener(action);
    }

    public Color getStandardColor() {
        return standardColor;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.width + padx * 2, this.height + pady * 2);
    }

    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    public void setPad(int padx, int pady) {
        this.padx = padx;
        this.pady = pady;
        repaint();
    }

}