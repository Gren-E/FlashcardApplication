package com.fa.util.gui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class HighlightingMouseAdapter extends MouseAdapter {

    private final JComponent component;
    private Border originalBorder;
    private Color originalColor;

    private boolean isEnabled;
    private final boolean highlightBorders;
    private final boolean highlightBackgrounds;
    private boolean isHighlighted;

    private Consumer<MouseEvent> action;

    public HighlightingMouseAdapter(JComponent component, boolean highlightBorders, boolean highlightBackground) {
        this.component = component;
        this.highlightBorders = highlightBorders;
        this.highlightBackgrounds = highlightBackground;
        this.isEnabled = true;
    }

    public void setMouseReleasedAction(Consumer<MouseEvent> action) {
        this.action = action;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(action != null && isEnabled) {
            action.accept(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(!isEnabled) {
            return;
        }

        if (isHighlighted) {
            return;
        }

        if (highlightBorders) {
            originalBorder = component.getBorder();
            component.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE));
        }

        if (highlightBackgrounds) {
            originalColor = component.getBackground();
            Color highlighted = new Color(
                    Math.min(255, originalColor.getRed() + 30),
                    Math.min(255, originalColor.getGreen() + 30),
                    Math.min(255, originalColor.getBlue() + 30)
            );
            component.setBackground(highlighted);
        }

        isHighlighted = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!isEnabled) {
            return;
        }

        if (!isHighlighted) {
            return;
        }

        if (highlightBorders) {
            originalBorder = originalBorder != null ? originalBorder : BorderFactory.createEmptyBorder(5, 5, 5, 5);
            component.setBorder(originalBorder);
        }

        if (highlightBackgrounds) {
            originalColor = originalColor != null ? originalColor : Color.WHITE;
            component.setBackground(originalColor);
        }

        isHighlighted = false;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
}
