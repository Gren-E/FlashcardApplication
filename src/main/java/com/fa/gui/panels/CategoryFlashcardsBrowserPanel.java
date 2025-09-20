package com.fa.gui.panels;

import com.fa.data.fc.Flashcard;
import com.fa.io.XMLReader;
import com.fa.io.XMLWriter;
import com.fa.util.gui.GBC;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class CategoryFlashcardsBrowserPanel extends JPanel {

    private Flashcard[] flashcards;

    private FlashcardDisplayPanel flashcardDisplayPanel;

    private JPanel flashcardsListPanel;

    private JButton correctAnswerButton;
    private JButton wrongAnswerButton;

    public CategoryFlashcardsBrowserPanel() {
        setLayout(new GridBagLayout());

        flashcardDisplayPanel = new FlashcardDisplayPanel();

        flashcardsListPanel = new JPanel();
        flashcardsListPanel.setLayout(new GridBagLayout());

        flashcards = XMLReader.loadFlashcards();

        JScrollPane buttonListScrollPane = new JScrollPane(flashcardsListPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buttonListScrollPane.setOpaque(false);
        buttonListScrollPane.getViewport().setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        correctAnswerButton = new JButton("Correct");
        correctAnswerButton.addActionListener(event -> answer(true));

        wrongAnswerButton = new JButton("Wrong");
        wrongAnswerButton.addActionListener(event -> answer(false));

        buttonPanel.add(correctAnswerButton);
        buttonPanel.add(wrongAnswerButton);

        add(buttonListScrollPane, new GBC(0,0,1,2).setWeight(0.2,1).setFill(GBC.BOTH));
        add(flashcardDisplayPanel, new GBC(1,0).setWeight(0.8,1).setFill(GBC.BOTH));
        add(buttonPanel, new GBC(1,1).setWeight(0.8, 0).setFill(GBC.HORIZONTAL));
    }

    private void answer(boolean isCorrect) {
        if (flashcardDisplayPanel.getFlashcard() == null) {
            return;
        }
        flashcardDisplayPanel.getFlashcard().answer(isCorrect);
        XMLWriter.updateStats(flashcardDisplayPanel.getFlashcard());
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

    public void setCategory(String categoryName) {
        int flashcardCount = 0;
        for (Flashcard flashcard : flashcards) {
            if (categoryName.equals(flashcard.getCategoryName())) {
                JButton flashcardButton = new JButton(flashcard.getCategoryName() + ": " + flashcard.getFileOrdinal());
                flashcardButton.addActionListener(event -> setFlashcard(flashcard));
                flashcardsListPanel.add(flashcardButton, new GBC(0,flashcardCount));
                flashcardCount++;
            }
        }

    }

    public void setFlashcard(Flashcard flashcard) {
        flashcardDisplayPanel.setFlashcard(flashcard);
        correctAnswerButton.setEnabled(flashcard != null && !flashcard.wasAnsweredToday());
        wrongAnswerButton.setEnabled(flashcard != null && !flashcard.wasAnsweredToday());
    }
}
