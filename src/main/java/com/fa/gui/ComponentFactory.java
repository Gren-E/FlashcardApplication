package com.fa.gui;

import com.fa.util.gui.HighlightingMouseAdapter;
import com.fa.util.gui.components.HighlightedButton;
import com.fa.util.gui.components.RectButton;
import com.fa.util.gui.components.RoundRectButton;
import com.fa.util.gui.components.RoundRectPanel;
import com.fa.util.gui.components.SimpleScrollBarUI;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

public class ComponentFactory {

    public static RoundRectButton createStandardAppButton() {
        return createStandardAppButton("");
    }

    public static RoundRectButton createStandardAppButton(String text) {
        return createStandardAppButton(text, null);
    }

    public static RoundRectButton createStandardAppButton(String text, ActionListener action) {
        RoundRectButton button = new RoundRectButton(text);
        enrichHighlightedButton(button, action);
        return button;
    }

    public static RectButton createFlashcardListButton() {
        return createFlashcardListButton("");
    }

    public static RectButton createFlashcardListButton(String text) {
        return createFlashcardListButton(text, null);
    }

    public static RectButton createFlashcardListButton(String text, ActionListener action) {
        RectButton button = new RectButton(text);
        enrichHighlightedButton(button, action);
        return button;
    }

    public static JLabel createStandardJLabel() {
        return createStandardJLabel("");
    }

    public static JLabel createStandardJLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppFonts.getStandardContentFont());
        return label;
    }

    public static JLabel createAccentJLabel() {
        return createAccentJLabel("");
    }

    public static JLabel createAccentJLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppFonts.getStandardContentFont());
        label.setForeground(AppColorPalette.getAccentForeground());
        return label;
    }

    public static JLabel createTitleJLabel() {
        return createTitleJLabel("");
    }

    public static JLabel createTitleJLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(AppFonts.getTitleFont());
        return label;
    }

    public static JLabel createAccentTitleJLabel() {
        return createAccentTitleJLabel("");
    }

    public static JLabel createAccentTitleJLabel(String text) {
        JLabel label = createTitleJLabel(text);
        label.setForeground(AppColorPalette.getAccentForeground());
        return label;
    }

    public static JTextField createStandardJTextField() {
        return createStandardJTextField("");
    }

    public static JTextField createStandardJTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setBorder(null);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(AppFonts.getStandardContentFont());
        return textField;
    }

    public static JPanel createAccentRoundRecJPanel() {
        return createAccentRoundRecJPanel(null);
    }

    public static JPanel createAccentRoundRecJPanel(LayoutManager layout) {
        JPanel panel = new RoundRectPanel(layout);
        panel.setBackground(AppColorPalette.getAccentBackground());
        return panel;
    }

    public static JPanel createContentRoundRecJPanel() {
        return createContentRoundRecJPanel(null);
    }

    public static JPanel createContentRoundRecJPanel(LayoutManager layout) {
        JPanel panel = new RoundRectPanel(layout);
        panel.setBackground(AppColorPalette.getContentBackground());
        return panel;
    }

    public static JScrollPane createVerticalJScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUI(new SimpleScrollBarUI());
        scrollPane.getVerticalScrollBar().setForeground(AppColorPalette.getSecondaryForeground());
        scrollPane.getVerticalScrollBar().setBackground(AppColorPalette.getAccentBackground());
        scrollPane.getVerticalScrollBar().addMouseListener(new HighlightingMouseAdapter(scrollPane.getVerticalScrollBar(), false, true));

        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        return scrollPane;
    }

    private static void enrichHighlightedButton(HighlightedButton button, ActionListener action) {
        button.updateButtonColors(AppColorPalette.getButtonBackground(), AppColorPalette.getAccentForeground(),
                AppColorPalette.getSecondaryForeground());
        button.setFont(AppFonts.getStandardContentFont());
        button.setActionListener(action);
    }

}
