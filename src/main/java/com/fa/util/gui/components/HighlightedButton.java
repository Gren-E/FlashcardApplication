package com.fa.util.gui.components;

import com.fa.util.gui.HighlightingMouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

public abstract class HighlightedButton extends JPanel {

    private final JLabel textLabel;

    private ActionListener mouseClickAction;
    private final HighlightingMouseAdapter mouseAdapter;

    private Color foregroundColor;
    private Color foregroundDisabledColor;

    public HighlightedButton() {
        this("");
    }

    public HighlightedButton(String text) {
        setOpaque(false);

        textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        mouseAdapter = new HighlightingMouseAdapter(this, false, true);
        mouseAdapter.setMouseReleasedAction(event -> handleMouseReleased());
        addMouseListener(mouseAdapter);

        setLayout(new BorderLayout());
        add(textLabel, BorderLayout.CENTER);
    }

    public void setText(String text) {
        textLabel.setText(text);
    }

    public void updateButtonColors(Color background, Color foreground, Color foregroundDisabled) {
        setBackground(background);
        foregroundColor = foreground;
        foregroundDisabledColor = foregroundDisabled;
        updateForeground();
    }

    private void updateForeground() {
        if (textLabel != null) {
            textLabel.setForeground(isEnabled() ? foregroundColor : foregroundDisabledColor);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mouseAdapter.setEnabled(enabled);
        updateForeground();
    }

    public void setActionListener(ActionListener action) {
        this.mouseClickAction = action;
    }

    private void handleMouseReleased() {
        if (mouseClickAction != null && isEnabled()) {
            mouseClickAction.actionPerformed(null);
        }
    }

    @Override
    public void setForeground(Color foreground) {
        foregroundColor = foreground;
        updateForeground();
    }

    @Override
    public void setFont(Font font) {
        if (textLabel != null) {
            textLabel.setFont(font);
        }
    }

}
