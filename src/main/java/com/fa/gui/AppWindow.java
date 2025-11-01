package com.fa.gui;

import com.fa.gui.panels.BoxesRevisionPanel;
import com.fa.gui.panels.CategoryFlashcardsBrowserPanel;
import com.fa.gui.panels.ExamPanel;
import com.fa.gui.panels.ProfileMenuPanel;
import com.fa.gui.panels.ProfileChoicePanel;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RoundRectButton;
import com.fa.util.gui.components.RoundRectPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagLayout;

public class AppWindow extends JFrame {

    public static final String PROFILE_CHOICE_PANEL = "PROFILE_CHOICE_PANEL";
    public static final String CATEGORY_CHOICE_PANEL = "CATEGORY_CHOICE_PANEL";
    public static final String FLASHCARDS_BROWSER_PANEL = "FLASHCARDS_BROWSER_PANEL";
    public static final String BOXES_REVISION_PANEL = "BOXES_REVISION_PANEL";
    public static final String EXAM_PANEL = "EXAM_PANEL";

    private final CardLayout cardLayout;

    private final JPanel contentPanel;

    private final ProfileChoicePanel profileChoicePanel;
    private final ProfileMenuPanel profileMenuPanel;
    private final CategoryFlashcardsBrowserPanel flashcardsBrowserPanel;
    private final BoxesRevisionPanel boxesRevisionPanel;
    private final ExamPanel examPanel;

    public AppWindow() {
        setTitle("FlashcardApplication");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setExtendedState(MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        cardLayout = new CardLayout();
        contentPanel = new RoundRectPanel(cardLayout);
        contentPanel.setBackground(AppColorPalette.getSecondaryBackground());

        profileChoicePanel = new ProfileChoicePanel(this);
        profileMenuPanel = new ProfileMenuPanel(this);
        flashcardsBrowserPanel = new CategoryFlashcardsBrowserPanel(this);
        boxesRevisionPanel = new BoxesRevisionPanel(this);
        examPanel = new ExamPanel(this);

        contentPanel.add(profileChoicePanel, PROFILE_CHOICE_PANEL);
        contentPanel.add(profileMenuPanel, CATEGORY_CHOICE_PANEL);
        contentPanel.add(flashcardsBrowserPanel, FLASHCARDS_BROWSER_PANEL);
        contentPanel.add(boxesRevisionPanel, BOXES_REVISION_PANEL);
        contentPanel.add(examPanel, EXAM_PANEL);
        cardLayout.show(contentPanel, PROFILE_CHOICE_PANEL);

        RoundRectButton exitButton = ComponentFactory.createStandardAppButton("Exit", event -> System.exit(0));

        add(exitButton, new GBC(0,0).setAnchor(GBC.EAST).setInsets(10));
        add(contentPanel, new GBC(0,1).setWeight(1,1).setFill(GBC.BOTH).setInsets(0,10,25,10));

        setVisible(true);
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public CategoryFlashcardsBrowserPanel getFlashcardsBrowserPanel() {
        return flashcardsBrowserPanel;
    }

    public ProfileMenuPanel getProfileMenuPanel() {
        return profileMenuPanel;
    }

    public ProfileChoicePanel getProfileChoicePanel() {
        return profileChoicePanel;
    }

    public BoxesRevisionPanel getBoxesRevisionPanel() {
        return boxesRevisionPanel;
    }

    public ExamPanel getExamPanel() {
        return examPanel;
    }

}
