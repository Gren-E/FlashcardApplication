package com.fa.gui.panels;

import com.fa.data.Box;
import com.fa.data.BoxManager;
import com.fa.data.fc.Flashcard;
import com.fa.gui.AppColorPalette;
import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.gui.dialogs.DialogUser;
import com.fa.gui.dialogs.RevisionMessageDialog;
import com.fa.io.DataManager;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.ProgressChart;
import org.apache.log4j.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.time.LocalDate;

public class BoxesRevisionPanel extends LearningModesPanel implements DialogUser {

    private static final Logger LOG = Logger.getLogger(BoxesRevisionPanel.class);

    private final JPanel boxesInfoPanel;

    private final ProgressChart dailyProgressChart;
    private final ProgressChart dailyGoalChart;

    public BoxesRevisionPanel(AppWindow parent) {
        super(parent);

        boxesInfoPanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout(), true);
        boxesInfoPanel.setMaximumSize(new Dimension(150, boxesInfoPanel.getHeight()));
        boxesInfoPanel.setPreferredSize(new Dimension(150, boxesInfoPanel.getHeight()));

        dailyProgressChart = ComponentFactory.createAccentProgressChart();
        dailyProgressChart.setProgressColor(AppColorPalette.getProgressColor());
        dailyProgressChart.setDiameter(150);

        dailyGoalChart = ComponentFactory.createAccentProgressChart();
        dailyGoalChart.setProgressColor(AppColorPalette.getAchievementColor());
        dailyGoalChart.setDiameter(150);

        flashcardDisplayPanel.setNoImageMessage("Revision finished!");

        correctAnswerButton.setActionListener(event -> answer(true));
        wrongAnswerButton.setActionListener(event -> answer(false));

        add(exitBrowserButton, new GBC(0, 0,2,1).setAnchor(GBC.EAST).setInsets(10));
        add(boxesInfoPanel, new GBC(0,1,1,2).setWeight(0.2,1).setFill(GBC.BOTH).setInsets(5,50,50,5));
        add(displayPanel, new GBC(1, 1).setWeight(0.8, 1).setFill(GBC.BOTH).setInsets(5,5,5,50));
        add(buttonPanel, new GBC(1, 2).setInsets(5,0,50,0));
    }

    public void reload() {
        startRevision();
        loadBoxesInfoPanel();
    }

    private void loadBoxesInfoPanel() {
        Box[] boxes = BoxManager.getBoxes();
        int totalAnswers = DataManager.getDailyActivity(LocalDate.now()).getBoxTotalAnswers();
        dailyProgressChart.setMaxValue(BoxManager.getUnansweredFlashcardsCount() - BoxManager.getReserveBox().getUnansweredFlashcardsCount() + totalAnswers);
        dailyProgressChart.setCurrentValue(DataManager.getDailyActivity(LocalDate.now()).getBoxCorrectAnswers());

        boxesInfoPanel.removeAll();
        boxesInfoPanel.add(dailyProgressChart, new GBC(0,0 ,3, 1).setWeight(1,0.5).setFill(GBC.BOTH).setInsets(100,0,50,0));

        for (int i = 0; i < boxes.length; i++) {
            JLabel boxLabel = ComponentFactory.createAccentTitleJLabel();
            JLabel sizeLabel = ComponentFactory.createAccentJLabel();
            sizeLabel.setText(String.valueOf(boxes[i].size()));
            JLabel unansweredLabel = ComponentFactory.createAccentJLabel();
            boxLabel.setHorizontalAlignment(SwingConstants.LEFT);
            if (boxes[i].equals(BoxManager.getReserveBox())) {
                boxLabel.setText("Reserve: ");
            } else if (boxes[i].equals(BoxManager.getLearntBox())) {
                boxLabel.setText("Learnt: ");
            } else {
                boxLabel.setText(String.format("Box %d: ", boxes[i].getIndex()));
                unansweredLabel.setText(String.format("(%d)", boxes[i].getUnansweredFlashcardsCount()));
            }
            if (boxes[i].equals(BoxManager.getActiveBox())) {
                boxLabel.setForeground(AppColorPalette.getProgressColor());
            }
            boxesInfoPanel.add(boxLabel, new GBC(0, i + 1).setWeight(0.4,0).setFill(GBC.HORIZONTAL).setInsets(0,50,0,0));
            boxesInfoPanel.add(sizeLabel, new GBC(1, i + 1).setWeight(0.3,0).setFill(GBC.HORIZONTAL));
            boxesInfoPanel.add(unansweredLabel, new GBC(2, i + 1).setWeight(0.3,0).setFill(GBC.HORIZONTAL).setInsets(0,0,0,50));
        }

        reloadDailyGoalChart();
        boxesInfoPanel.add(dailyGoalChart, new GBC(0, boxes.length + 1, 3, 1).setWeight(1,0.5).setFill(GBC.BOTH).setInsets(50,0,100,0));
        dailyGoalChart.repaint();
        revalidate();
    }

    private void answer(boolean isCorrect) {
        Flashcard flashcard = flashcardDisplayPanel.getFlashcard();
        if (flashcard == null) {
            LOG.error("Cannot answer because flashcard is null.");
            return;
        }
        if (isCorrect) {
            BoxManager.handleSuccessfulAnswer(flashcard.getId());
        } else {
            BoxManager.handleFailedAnswer(flashcard.getId());
        }

        flashcardDisplayPanel.setFlashcard(getNextFlashcard());

        loadBoxesInfoPanel();
    }

    private void startRevision() {
        flashcardDisplayPanel.setFlashcard(getNextFlashcard());
        int learntRefresher = DataManager.getCurrentProfile().getDailyRelearningGoal();
        int totalAnswers = DataManager.getDailyActivity(LocalDate.now()).getBoxTotalAnswers();
        if (totalAnswers == learntRefresher) {
            return;
        }

        int learntSize = BoxManager.getLearntBox().size();
        new RevisionMessageDialog(this, totalAnswers >= learntRefresher || learntSize <= learntRefresher - totalAnswers
                ?  RevisionMessageDialog.BOXES : RevisionMessageDialog.RANDOM);
    }

    private void reloadDailyGoalChart() {
        if (DataManager.getCurrentProfile() != null) {
            dailyGoalChart.setMaxValue(DataManager.getCurrentProfile().getDailyGoal());
        }
        dailyGoalChart.setCurrentValue(DataManager.getDailyActivity(LocalDate.now()).getBoxCorrectAnswers());
    }

    private Flashcard getNextFlashcard() {
        Flashcard result;
        int learntRefresher = DataManager.getCurrentProfile().getDailyRelearningGoal();
        int boxTotalAnswers = DataManager.getDailyActivity(LocalDate.now()).getBoxTotalAnswers();
        if (boxTotalAnswers < learntRefresher && boxTotalAnswers < BoxManager.getLearntBox().size()) {
            result = BoxManager.getRandomLearntFlashcard();
        } else {
            result = BoxManager.getNextUnansweredFlashcard();
        }
        if (DataManager.getDailyActivity(LocalDate.now()).getBoxTotalAnswers() == learntRefresher) {
            new RevisionMessageDialog(this, RevisionMessageDialog.BOXES);
        }
        return result;
    }

    @Override
    public void openDialog() {
        correctAnswerButton.setEnabled(false);
        wrongAnswerButton.setEnabled(false);
        exitBrowserButton.setEnabled(false);
        flashcardDisplayPanel.setEnabled(false);
    }

    @Override
    public void disposeDialog() {
        correctAnswerButton.setEnabled(true);
        wrongAnswerButton.setEnabled(true);
        exitBrowserButton.setEnabled(true);
        flashcardDisplayPanel.setEnabled(true);
    }

    @Override
    public JComponent getJComponent() {
        return this;
    }
}
