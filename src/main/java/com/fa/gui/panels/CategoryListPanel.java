package com.fa.gui.panels;

import com.fa.gui.AppWindow;
import com.fa.io.XMLReader;
import com.fa.util.gui.GBC;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class CategoryListPanel extends JPanel {

    private AppWindow parent;
    private Set<String> categories;

    public CategoryListPanel(AppWindow parent) {
        this.parent = parent;

        categories = XMLReader.getCategories();

        setLayout(new GridLayout(categories.size(), 1));
        for (String category : categories) {
            JPanel categoryPanel = createCategoryPanel(category);
            add(categoryPanel);
        }
    }

    private JPanel createCategoryPanel(String categoryName) {
        JPanel categoryPanel = new JPanel(new GridBagLayout());
        categoryPanel.setBorder(new MatteBorder(1,1,1,1, Color.BLACK));

        JLabel categoryNameLabel = new JLabel(categoryName);
        JButton browseCategoryButton = new JButton("Browse Category");
        browseCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.getCardLayout().show(parent.getAppWindowPanel(), "FLASHCARDS_BROWSER_PANEL");
                parent.getFlashcardsBrowserPanel().setCategory(categoryName);
            }
        });

        categoryPanel.add(categoryNameLabel, new GBC(0,0).setInsets(5,5,5,10));
        categoryPanel.add(browseCategoryButton, new GBC(1,0).setInsets(5,10,5,5));

        return categoryPanel;
    }
}
