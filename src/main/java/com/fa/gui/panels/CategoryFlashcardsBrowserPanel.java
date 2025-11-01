package com.fa.gui.panels;

import com.fa.data.AppMode;
import com.fa.data.fc.Flashcard;
import com.fa.gui.AppColorPalette;
import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RectButton;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFlashcardsBrowserPanel extends LearningModesPanel {

    private final JPanel flashcardsListPanel;

    private final List<Flashcard> flashcards;
    private final Map<Flashcard, RectButton> flashcardButtons;

    public CategoryFlashcardsBrowserPanel(AppWindow parent) {
        super(parent);

        flashcardsListPanel = new JPanel(new GridBagLayout());
        flashcardsListPanel.setOpaque(false);

        flashcards = new ArrayList<>();
        flashcardButtons = new HashMap<>();

        JScrollPane buttonListScrollPane = ComponentFactory.createVerticalJScrollPane(flashcardsListPanel);

        JPanel scrollPanePanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout());
        scrollPanePanel.add(buttonListScrollPane, new GBC(0,0).setWeight(1,1).setFill(GBC.BOTH).setInsets(20, 50, 20, 50));

        correctAnswerButton.setActionListener(event -> answer(true));
        wrongAnswerButton.setActionListener(event -> answer(false));

        add(exitBrowserButton,  new GBC(0, 0,2,1).setAnchor(GBC.EAST).setInsets(10));
        add(scrollPanePanel, new GBC(0,1,1,2).setWeight(0,1).setFill(GBC.BOTH).setInsets(5,50,50,5).setIpad(250,0));
        add(displayPanel, new GBC(1, 1).setWeight(0.8, 1).setFill(GBC.BOTH).setInsets(5,5,5,50));
        add(buttonPanel, new GBC(1, 2).setWeight(0.8,0).setInsets(5,0,50,50));
    }

    public void setCategory(String categoryName) {
        flashcards.clear();
        flashcardsListPanel.removeAll();
        Flashcard[] categoryFlashcards = DataManager.getAllFlashcardsFromCategory(categoryName);
        for (int i = 0; i < categoryFlashcards.length; i++) {
            Flashcard flashcard = categoryFlashcards[i];
            addFlashcardButton(flashcard, i);
        }
        flashcards.sort(Comparator.comparingInt(Flashcard::getFileOrdinal));

        setFlashcard(flashcards.get(0));
        activeButton = flashcardButtons.get(flashcards.get(0));
    }

    private void answer(boolean isCorrect) {
        if (flashcardDisplayPanel.getFlashcard() == null) {
            return;
        }
        flashcardDisplayPanel.getFlashcard().answer(isCorrect, AppMode.CATEGORY_BROWSER);
        activeButton.setBackground(isCorrect ? AppColorPalette.getButtonSuccessBackground() : AppColorPalette.getButtonFailBackground());
        setNextUnansweredFlashcard();
    }

    private void setNextUnansweredFlashcard() {
        for (Flashcard flashcard : flashcards) {
            if (!flashcard.wasAnsweredToday()) {
                setFlashcard(flashcard);
                return;
            }
        }
        setFlashcard(null);
    }

    private void setFlashcard(Flashcard flashcard) {
        flashcardDisplayPanel.setFlashcard(flashcard);
        activeButton = flashcardButtons.get(flashcard);
        correctAnswerButton.setEnabled(flashcard != null && !flashcard.wasAnsweredToday());
        wrongAnswerButton.setEnabled(flashcard != null && !flashcard.wasAnsweredToday());
    }

    private void addFlashcardButton(Flashcard flashcard, int index) {
        RectButton flashcardButton = ComponentFactory.createFlashcardListButton(flashcard.getCategoryName() + ": " + flashcard.getFileOrdinal());
        flashcardButton.setActionListener(event -> {
            setFlashcard(flashcard);
            activeButton = flashcardButton;
        });
        flashcardsListPanel.add(flashcardButton, new GBC(0, index).setWeight(1,0).setFill(GBC.HORIZONTAL).setInsets(0,0,0,5));
        flashcards.add(flashcard);
        flashcardButtons.put(flashcard, flashcardButton);
    }

}
