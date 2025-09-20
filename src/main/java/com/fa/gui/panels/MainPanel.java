package com.fa.gui.panels;

import com.fa.gui.AppWindow;
import com.fa.gui.CategoryCreatorDialog;
import com.fa.util.gui.GBC;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

public class MainPanel extends JPanel {

    private JButton newCategoryButton;
    private CategoryListPanel categoryListPanel;

    public MainPanel(AppWindow parent) {
        newCategoryButton = new JButton("New Category");
        newCategoryButton.setFocusPainted(false);
        newCategoryButton.addActionListener((ActionEvent event) -> new CategoryCreatorDialog(parent));

        categoryListPanel = new CategoryListPanel(parent);

        setLayout(new GridBagLayout());
        add(newCategoryButton, new GBC(0,0).setInsets(20));
        add(categoryListPanel, new GBC(0,1));
    }

}

