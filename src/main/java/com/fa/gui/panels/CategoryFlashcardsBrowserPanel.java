package com.fa.gui.panels;

import com.fa.data.AppMode;
import com.fa.data.fc.Flashcard;
import com.fa.gui.AppColorPalette;
import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.util.gui.ComponentUtil;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.ProgressChart;
import com.fa.util.gui.components.RectButton;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFlashcardsBrowserPanel extends LearningModesPanel {

    private final JPanel flashcardsListPanel;

    private final List<Flashcard> flashcards;
    private final Map<Flashcard, RectButton> flashcardButtons;

    private final ProgressChart answeredCorrectlyChart;

    public CategoryFlashcardsBrowserPanel(AppWindow parent) {
        super(parent);

        flashcardsListPanel = new JPanel(new GridBagLayout());
        flashcardsListPanel.setOpaque(false);

        answeredCorrectlyChart = ComponentFactory.createAccentProgressChart();
        answeredCorrectlyChart.setProgressColor(AppColorPalette.getProgressColor());
        answeredCorrectlyChart.setDiameter(150);

        flashcards = new ArrayList<>();
        flashcardButtons = new HashMap<>();

        JScrollPane buttonListScrollPane = ComponentFactory.createVerticalJScrollPane(flashcardsListPanel);
        buttonListScrollPane.setMaximumSize(new Dimension(150, buttonListScrollPane.getHeight()));
        buttonListScrollPane.setPreferredSize(new Dimension(150, buttonListScrollPane.getHeight()));

        JPanel scrollPanePanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout(), true);
        scrollPanePanel.add(answeredCorrectlyChart, new GBC(0,0).setWeight(0,0).setFill(GBC.BOTH).setInsets(50,50,50,50).setIpad(150,150));
        scrollPanePanel.add(buttonListScrollPane, new GBC(0,1).setWeight(0,1).setFill(GBC.BOTH).setInsets(0, 50, 50, 50).setIpad(150,0));

        correctAnswerButton.setActionListener(event -> answer(true));
        wrongAnswerButton.setActionListener(event -> answer(false));

        add(exitBrowserButton,  new GBC(0, 0,2,1).setAnchor(GBC.EAST).setInsets(10));
        add(scrollPanePanel, new GBC(0,1,1,2).setWeight(0,1).setFill(GBC.VERTICAL).setInsets(5,50,50,5));
        add(displayPanel, new GBC(1, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5,5,5,50));
        add(buttonPanel, new GBC(1, 2).setWeight(1,0).setInsets(5,0,50,50));
    }

    public void setCategory(String categoryName) {
        flashcards.clear();
        flashcardsListPanel.removeAll();

        Flashcard[] categoryFlashcards = DataManager.getAllFlashcardsFromCategory(categoryName);

        answeredCorrectlyChart.setMaxValue(categoryFlashcards.length);
        answeredCorrectlyChart.setCurrentValue((int) Arrays.stream(categoryFlashcards).filter(Flashcard::wasAnsweredCorrectlyToday).count());

        for (int i = 0; i < categoryFlashcards.length; i++) {
            Flashcard flashcard = categoryFlashcards[i];
            addFlashcardButton(flashcard, i);
        }

        flashcardsListPanel.add(ComponentUtil.createEmptyPanel(), new GBC(0, categoryFlashcards.length + 1).setWeight(1,1).setFill(GBC.BOTH));
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
        answeredCorrectlyChart.setCurrentValue(isCorrect ? answeredCorrectlyChart.getCurrentValue() + 1 : answeredCorrectlyChart.getCurrentValue());
        setNextUnansweredFlashcard();
    }

    private void setNextUnansweredFlashcard() {
        setFlashcard(flashcards.stream().filter(flashcard -> !flashcard.wasAnsweredInCategoryToday()).findFirst().orElse(null));
    }

    private void setFlashcard(Flashcard flashcard) {
        flashcardDisplayPanel.setFlashcard(flashcard);
        activeButton = flashcardButtons.get(flashcard);
        correctAnswerButton.setEnabled(flashcard != null && !flashcard.wasAnsweredInCategoryToday());
        wrongAnswerButton.setEnabled(flashcard != null && !flashcard.wasAnsweredInCategoryToday());
    }

    private void addFlashcardButton(Flashcard flashcard, int index) {
        RectButton flashcardButton = ComponentFactory.createFlashcardListButton(flashcard.getCategoryName() + ": " + flashcard.getFileOrdinal());
        if (flashcard.wasAnsweredInCategoryToday()) {
            flashcardButton.setBackground(flashcard.wasAnsweredCorrectlyToday() ? AppColorPalette.getButtonSuccessBackground() : AppColorPalette.getButtonFailBackground());
        }

        flashcardButton.setActionListener(event -> {
            setFlashcard(flashcard);
            activeButton = flashcardButton;
        });
        flashcardsListPanel.add(flashcardButton, new GBC(0, index).setWeight(1,0).setFill(GBC.HORIZONTAL).setInsets(0,0,0,5));
        flashcards.add(flashcard);
        flashcardButtons.put(flashcard, flashcardButton);
    }

}
