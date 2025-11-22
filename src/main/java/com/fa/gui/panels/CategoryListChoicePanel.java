package com.fa.gui.panels;

import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.io.XMLWriter;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RoundRectButton;

import javax.swing.JPanel;

public class CategoryListChoicePanel extends ListChoicePanel<String> {

    public CategoryListChoicePanel(AppWindow window) {
        super(window);
        if (DataManager.getCurrentProfile() != null) {
            reloadList();
        }
    }

    @Override
    public JPanel createElementPanel(String categoryName) {
        RoundRectButton browseCategoryButton = ComponentFactory.createStandardAppButton("Browse Category", event -> {
            window.getCardLayout().show(window.getContentPanel(), AppWindow.FLASHCARDS_BROWSER_PANEL);
            window.getFlashcardsBrowserPanel().setCategory(categoryName);
        });
        buttonList.add(browseCategoryButton);

        JPanel categoryPanel = createElementLabelPanel(categoryName, categoryName);
        categoryPanel.add(browseCategoryButton, new GBC(2, 0).setInsets(5, 10, 5, 15));

        return categoryPanel;
    }

    @Override
    public void deleteElement(String category) {
        XMLWriter.deleteCategory(category, DataManager.getCurrentProfile());
        window.getProfileMenuPanel().reloadCategories();
    }

    @Override
    protected String[] loadElements() {
        DataManager.reloadCurrentProfile();
        return DataManager.getAllCategoriesNames();
    }

}
