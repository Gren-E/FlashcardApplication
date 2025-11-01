package com.fa.gui.panels;

import com.fa.gui.AppColorPalette;
import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RectButton;
import com.fa.util.gui.components.RoundRectButton;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

public class LearningModesPanel extends JPanel {

    protected FlashcardDisplayPanel flashcardDisplayPanel;

    protected JPanel displayPanel;
    protected JPanel buttonPanel;

    protected RoundRectButton correctAnswerButton;
    protected RoundRectButton wrongAnswerButton;
    protected RoundRectButton exitBrowserButton;

    protected RectButton activeButton;

    public LearningModesPanel(AppWindow parent) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        flashcardDisplayPanel = new FlashcardDisplayPanel();

        displayPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout());
        displayPanel.add(flashcardDisplayPanel, new GBC(0,0).setWeight(1,1).setFill(GBC.BOTH).setInsets(20, 50, 20, 50));

        buttonPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout());

        correctAnswerButton = ComponentFactory.createStandardAppButton("Correct");
        correctAnswerButton.setBackground(AppColorPalette.getButtonSuccessBackground());

        wrongAnswerButton = ComponentFactory.createStandardAppButton("Wrong");
        wrongAnswerButton.setBackground(AppColorPalette.getButtonFailBackground());

        buttonPanel.add(correctAnswerButton, new GBC(0,0).setInsets(5,15,5,5));
        buttonPanel.add(wrongAnswerButton, new GBC(1,0).setInsets(5,5,5,15));

        exitBrowserButton = ComponentFactory.createStandardAppButton("Back",
                event -> parent.getCardLayout().show(parent.getContentPanel(), AppWindow.CATEGORY_CHOICE_PANEL));
    }

}
