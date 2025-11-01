package com.fa.gui.panels;

import com.fa.data.AppMode;
import com.fa.gui.AppColorPalette;
import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.mode.Question;
import com.fa.mode.exam.Exam;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RectButton;
import com.fa.util.timer.CountdownTimer;
import com.fa.util.timer.TimerListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

public class ExamPanel extends LearningModesPanel implements TimerListener {

    private final JPanel flashcardsListPanel;

    private final JLabel timerLabel;

    private final Map<Integer, RectButton> questionButtons;

    private Exam exam;

    private final CountdownTimer timer;

    private int currentQuestionIndex;

    public ExamPanel(AppWindow parent) {
        super(parent);

        flashcardsListPanel = new JPanel(new GridBagLayout());
        flashcardsListPanel.setOpaque(false);

        questionButtons = new HashMap<>();

        JScrollPane buttonListScrollPane = ComponentFactory.createVerticalJScrollPane(flashcardsListPanel);

        JPanel scrollPanePanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout());
        scrollPanePanel.add(buttonListScrollPane, new GBC(0,0).setWeight(1,1).setFill(GBC.BOTH).setInsets(20, 50, 20, 40));

        correctAnswerButton.setActionListener(event -> answer(true));
        wrongAnswerButton.setActionListener(event -> answer(false));

        timerLabel = ComponentFactory.createAccentTitleJLabel("00:00");
        JPanel timerPanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout());
        timerPanel.add(timerLabel, new GBC(0,0).setInsets(10,15,5,15));

        timer = new CountdownTimer();
        timer.setTimerListener(this);

        add(exitBrowserButton,  new GBC(0, 0,3,1).setAnchor(GBC.EAST).setInsets(10));
        add(scrollPanePanel, new GBC(0,1,1,2).setWeight(0,1).setFill(GBC.BOTH).setInsets(5,50,50,5).setIpad(250,0));
        add(displayPanel, new GBC(1, 1,2,1).setWeight(0.8, 1).setFill(GBC.BOTH).setInsets(5,5,5,50));
        add(buttonPanel, new GBC(1, 2).setInsets(5,0,50,0).setWeight(0.8,0).setAnchor(GBC.CENTER));
        add(timerPanel, new GBC(2,2).setInsets(5,50,50,50).setWeight(0,0).setAnchor(GBC.EAST));
    }

    public void loadExam(Exam exam) {
        this.exam = exam;
        flashcardsListPanel.removeAll();
        for (int i = 0; i < exam.countQuestions(); i++) {
            addQuestionButton(i);
        }
        currentQuestionIndex = 0;
        setFlashcard(exam.getNextUnansweredQuestion());
        activeButton = questionButtons.get(0);
        exam.start();
        timer.countUntil(exam.getFinishTimestamp());
    }

    private void answer(boolean isCorrect) {
        if (flashcardDisplayPanel.getFlashcard() == null) {
            return;
        }
        flashcardDisplayPanel.getFlashcard().answer(isCorrect, AppMode.EXAM);
        exam.getQuestion(currentQuestionIndex).answer(isCorrect);
        activeButton.setBackground(isCorrect ? AppColorPalette.getButtonSuccessBackground() : AppColorPalette.getButtonFailBackground());
        setFlashcard(exam.getNextUnansweredQuestion(currentQuestionIndex));
    }

    private void setFlashcard(Question question) {
        flashcardDisplayPanel.setFlashcard(question.getFlashcard());
        currentQuestionIndex = exam.getQuestionIndex(question);
        activeButton = questionButtons.get(currentQuestionIndex);
        correctAnswerButton.setEnabled(!question.wasAnswered());
        wrongAnswerButton.setEnabled(!question.wasAnswered());
    }

    private void addQuestionButton(int index) {
        RectButton flashcardButton = ComponentFactory.createFlashcardListButton(String.format("Question: %d", index + 1));
        flashcardButton.setActionListener(event -> {
            setFlashcard(exam.getQuestion(index));
            activeButton = flashcardButton;
        });
        flashcardsListPanel.add(flashcardButton, new GBC(0, index).setWeight(1,0).setFill(GBC.HORIZONTAL).setInsets(0,0,0,5));
        questionButtons.put(index, flashcardButton);
    }

    @Override
    public void updateTime(long timeInSeconds) {
        long minutes = timeInSeconds / 60;
        long seconds = timeInSeconds % 60;
        timerLabel.setText(String.format("%d:%02d", minutes, seconds));
    }

}
