package com.fa.gui;

import com.fa.gui.panels.CategoryFlashcardsBrowserPanel;
import com.fa.gui.panels.MainPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class AppWindow extends JFrame {

    CardLayout cardLayout;
    JPanel appWindowPanel;

    MainPanel mainPanel;

    CategoryFlashcardsBrowserPanel flashcardsBrowserPanel;

    public AppWindow() {
        setTitle("FlashcardApplication");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setExtendedState(MAXIMIZED_BOTH);

        cardLayout = new CardLayout();
        appWindowPanel = new JPanel(cardLayout);

        mainPanel = new MainPanel(this);
        flashcardsBrowserPanel = new CategoryFlashcardsBrowserPanel();

        appWindowPanel.add(mainPanel, "MAIN_PANEL");
        appWindowPanel.add(flashcardsBrowserPanel, "FLASHCARDS_BROWSER_PANEL");
        cardLayout.show(appWindowPanel, "MAIN_PANEL");

        add(appWindowPanel);

        setVisible(true);
    }

    public JPanel getAppWindowPanel() {
        return appWindowPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public CategoryFlashcardsBrowserPanel getFlashcardsBrowserPanel() {
        return flashcardsBrowserPanel;
    }

}
