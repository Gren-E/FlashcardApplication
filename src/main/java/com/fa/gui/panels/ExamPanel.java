package com.fa.gui.panels;

import com.fa.data.AppMode;
import com.fa.gui.AppColorPalette;
import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.mode.Question;
import com.fa.mode.exam.Exam;
import com.fa.util.gui.ComponentUtil;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.ProgressChart;
import com.fa.util.gui.components.RectButton;
import com.fa.util.timer.CountdownTimer;
import com.fa.util.timer.TimerListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

public class ExamPanel extends LearningModesPanel implements TimerListener {

    public static final String DISPLAY_MODE_FLASHCARD = "DISPLAY_MODE_FLASHCARD";
    public static final String DISPLAY_MODE_RESULT = "DISPLAY_MODE_RESULT";

    private final ExamResultPanel examResultPanel;
    private final JPanel flashcardsListPanel;

    private final JLabel timerLabel;

    private final CardLayout displayLayout;

    private final ProgressChart answeredCorrectlyChart;

    private final Map<Integer, RectButton> questionButtons;

    private Exam exam;

    private final CountdownTimer timer;

    private int currentQuestionIndex;

    public ExamPanel(AppWindow parent) {
        super(parent);

        flashcardsListPanel = new JPanel(new GridBagLayout());
        flashcardsListPanel.setOpaque(false);

        answeredCorrectlyChart = ComponentFactory.createAccentProgressChart();
        answeredCorrectlyChart.setProgressColor(AppColorPalette.getProgressColor());
        answeredCorrectlyChart.setDiameter(150);

        questionButtons = new HashMap<>();

        JScrollPane buttonListScrollPane = ComponentFactory.createVerticalJScrollPane(flashcardsListPanel);
        buttonListScrollPane.setMaximumSize(new Dimension(150, buttonListScrollPane.getHeight()));
        buttonListScrollPane.setPreferredSize(new Dimension(150, buttonListScrollPane.getHeight()));

        examResultPanel = new ExamResultPanel();
        displayLayout = new CardLayout();

        displayPanel.removeAll();
        displayPanel.setLayout(displayLayout);
        displayPanel.add(flashcardDisplayPanel, DISPLAY_MODE_FLASHCARD);
        displayPanel.add(examResultPanel, DISPLAY_MODE_RESULT);

        JPanel scrollPanePanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout(), true);
        scrollPanePanel.add(answeredCorrectlyChart, new GBC(0,0).setWeight(0,0).setFill(GBC.BOTH).setInsets(50,50,50,50).setIpad(150,150));
        scrollPanePanel.add(buttonListScrollPane, new GBC(0,1).setWeight(0,1).setFill(GBC.BOTH).setInsets(0, 50, 50, 50).setIpad(150,0));

        correctAnswerButton.setActionListener(event -> answer(true));
        wrongAnswerButton.setActionListener(event -> answer(false));

        timerLabel = ComponentFactory.createAccentTitleJLabel("00:00");
        JPanel timerPanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout(), false);
        timerPanel.add(timerLabel, new GBC(0,0).setInsets(10,15,5,15));

        timer = new CountdownTimer();
        timer.setTimerListener(this);

        add(exitBrowserButton,  new GBC(0, 0,3,1).setAnchor(GBC.EAST).setInsets(10));
        add(scrollPanePanel, new GBC(0,1,1,2).setWeight(0,1).setFill(GBC.VERTICAL).setInsets(5,50,50,5));
        add(displayPanel, new GBC(1, 1,2,1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5,5,5,50));
        add(buttonPanel, new GBC(1, 2).setWeight(1,0).setAnchor(GBC.CENTER).setInsets(5,0,50,0));
        add(timerPanel, new GBC(2,2).setWeight(0,0).setAnchor(GBC.EAST).setInsets(5,50,50,50));
    }

    public void loadExam(Exam exam) {
        this.exam = exam;

        answeredCorrectlyChart.setMaxValue(exam.countQuestions());
        answeredCorrectlyChart.setCurrentValue(0);

        flashcardsListPanel.removeAll();
        for (int i = 0; i < exam.countQuestions(); i++) {
            addQuestionButton(i);
        }
        flashcardsListPanel.add(ComponentUtil.createEmptyPanel(), new GBC(0, exam.countQuestions() + 1).setWeight(1,0.7).setFill(GBC.BOTH));

        currentQuestionIndex = 0;
        activeButton = questionButtons.get(0);
        setFlashcard(exam.getNextUnansweredQuestion());

        displayLayout.show(displayPanel, DISPLAY_MODE_FLASHCARD);
        exam.start();
        timer.countUntil(exam.getFinishTimestamp());
        timer.resume();
        timerLabel.setVisible(true);

    }

    private void answer(boolean isCorrect) {
        if (flashcardDisplayPanel.getFlashcard() == null) {
            return;
        }
        flashcardDisplayPanel.getFlashcard().answer(isCorrect, AppMode.EXAM);
        exam.getQuestion(currentQuestionIndex).answer(isCorrect);
        activeButton.setBackground(isCorrect ? AppColorPalette.getButtonSuccessBackground() : AppColorPalette.getButtonFailBackground());
        answeredCorrectlyChart.setCurrentValue(isCorrect ? answeredCorrectlyChart.getCurrentValue() + 1 : answeredCorrectlyChart.getCurrentValue());

        if (!exam.isFinished()) {
            setFlashcard(exam.getNextUnansweredQuestion(currentQuestionIndex));
        } else {
            endExam(ExamResultPanel.EXAM_COMPLETED);
        }
    }

    private void setFlashcard(Question question) {
        flashcardDisplayPanel.setFlashcard(question.getFlashcard());
        currentQuestionIndex = exam.getQuestionIndex(question);
        activeButton = questionButtons.get(currentQuestionIndex);
        enableAnswerButtons(!question.wasAnswered() && !exam.isFinished());
    }

    private void addQuestionButton(int index) {
        RectButton flashcardButton = ComponentFactory.createFlashcardListButton(String.format("Question: %d", index + 1));
        flashcardButton.setActionListener(event -> {
            displayLayout.show(displayPanel, DISPLAY_MODE_FLASHCARD);
            setFlashcard(exam.getQuestion(index));
            activeButton = flashcardButton;
        });
        flashcardsListPanel.add(flashcardButton, new GBC(0, index).setWeight(1,0).setFill(GBC.HORIZONTAL).setInsets(0,0,0,5));
        questionButtons.put(index, flashcardButton);
    }

    private void endExam(String titleMessage) {
        timer.stop();
        timerLabel.setVisible(false);

        examResultPanel.loadResults(titleMessage, exam, timer.getTimerStoppedTimestamp());
        displayLayout.show(displayPanel, DISPLAY_MODE_RESULT);

        enableAnswerButtons(false);

        RectButton resultsButton = ComponentFactory.createFlashcardListButton("Results");
        resultsButton.setActionListener(event -> displayLayout.show(displayPanel, DISPLAY_MODE_RESULT));

        flashcardsListPanel.add(resultsButton, new GBC(0, questionButtons.size()).setWeight(1,0).setFill(GBC.HORIZONTAL).setInsets(0,0,0,5));
    }

    private void enableAnswerButtons(boolean enabled) {
        correctAnswerButton.setEnabled(enabled);
        wrongAnswerButton.setEnabled(enabled);
    }

    @Override
    public void updateTime(long timeInSeconds) {
        if (timeInSeconds == 0 || timeInSeconds < -1) {
            endExam(ExamResultPanel.TIME_IS_UP);
            timerLabel.setText("00:00");
            return;
        }
        long minutes = timeInSeconds / 60;
        long seconds = timeInSeconds % 60;
        timerLabel.setText(String.format("%d:%02d", minutes, seconds));
    }

}
