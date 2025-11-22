package com.fa.util.gui;

import javax.swing.JPanel;
import java.awt.LayoutManager;

public class ComponentUtil {

    public static JPanel createEmptyPanel() {
        return createEmptyPanel(null);
    }

    public static JPanel createEmptyPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

}
