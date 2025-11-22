package com.fa.gui;

import com.fa.util.gui.GraphicsUtil;
import com.fa.util.gui.HighlightingMouseAdapter;
import com.fa.util.gui.components.EmbellishedStringIcon;
import com.fa.util.gui.components.HighlightedButton;
import com.fa.util.gui.components.ProgressChart;
import com.fa.util.gui.components.RectButton;
import com.fa.util.gui.components.RoundRectButton;
import com.fa.util.gui.components.RoundRectPanel;
import com.fa.util.gui.components.SimpleScrollBarUI;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    public static JLabel createStandardUnderlinedJLabel() {
        return createStandardUnderlinedJLabel("");
    }

    public static JLabel createStandardUnderlinedJLabel(String text) {
        JLabel label = createStandardJLabel(text);
        label.setBorder(new MatteBorder(0,0,2,0, label.getForeground()));
        return label;
    }

    public static JLabel createStandardInteractiveJLabel() {
        return createStandardInteractiveJLabel(null, null);
    }

    public static JLabel createStandardInteractiveJLabel(String text) {
        return createStandardInteractiveJLabel(text, null);
    }

    public static JLabel createStandardInteractiveJLabel(String text, Runnable action) {
        JLabel label = createStandardJLabel(text);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                action.run();
            }
        });
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

    public static JLabel createAccentInteractiveJLabel() {
        return createAccentInteractiveJLabel(null, null);
    }

    public static JLabel createAccentInteractiveJLabel(String text) {
        return createAccentInteractiveJLabel(text, null);
    }

    public static JLabel createAccentInteractiveJLabel(String text, Runnable action) {
        JLabel label = createAccentJLabel(text);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                action.run();
            }
        });
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

    public static JLabel createGigaJLabel() {
        return createGigaJLabel("");
    }

    public static JLabel createGigaJLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(AppFonts.getGigaFont());
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

    public static ProgressChart createStandardProgressChart() {
        ProgressChart chart = new ProgressChart();
        chart.setBackground(AppColorPalette.getContentBackground());
        chart.setForeground(AppColorPalette.getContentForeground());
        chart.setFont(AppFonts.getTitleFont());
        return chart;
    }

    public static ProgressChart createAccentProgressChart() {
        ProgressChart chart = new ProgressChart();
        chart.setBackground(AppColorPalette.getAccentBackground());
        chart.setForeground(AppColorPalette.getAccentForeground());
        chart.setFont(AppFonts.getTitleFont());
        return chart;
    }

    public static JPanel createAccentRoundRecJPanel() {
        return createAccentRoundRecJPanel(null, false);
    }

    public static RoundRectPanel createAccentRoundRecJPanel(LayoutManager layout, boolean framed) {
        RoundRectPanel panel = new RoundRectPanel(layout, framed);
        panel.setBackground(AppColorPalette.getAccentBackground());
        return panel;
    }

    public static JPanel createContentRoundRecJPanel() {
        return createContentRoundRecJPanel(null, false);
    }

    public static JPanel createContentRoundRecJPanel(LayoutManager layout, boolean framed) {
        JPanel panel = new RoundRectPanel(layout, framed);
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

    public static EmbellishedStringIcon createEmbellishedStringIcon(Image iconImage, int width, int height) {
        EmbellishedStringIcon icon = new EmbellishedStringIcon(iconImage, width, height);
        icon.setFont(AppFonts.getGigaFont());
        icon.setHighlight(false);
        return icon;
    }

    private static void enrichHighlightedButton(HighlightedButton button, ActionListener action) {
        button.updateButtonColors(AppColorPalette.getButtonBackground(), AppColorPalette.getAccentForeground(),
                AppColorPalette.getSecondaryForeground());
        button.setFont(AppFonts.getStandardContentFont());
        button.setActionListener(action);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

}
