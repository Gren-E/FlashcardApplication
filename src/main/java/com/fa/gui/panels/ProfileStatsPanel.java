package com.fa.gui.panels;

import com.fa.data.BoxManager;
import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.util.gui.GBC;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.time.LocalDate;

public class ProfileStatsPanel extends JPanel {

    private final JLabel flashcardsLearntLabel;
    private final JLabel dailyGoalLabel;

    public ProfileStatsPanel() {
        setOpaque(false);

        flashcardsLearntLabel = ComponentFactory.createTitleJLabel();
        dailyGoalLabel = ComponentFactory.createTitleJLabel();

        setLayout(new GridBagLayout());
        add(flashcardsLearntLabel, new GBC(0,0).setWeight(1,1).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5,50,5,10));
        add(dailyGoalLabel, new GBC(1,0).setWeight(1,1).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(5,10,5,50));
    }

    public void reloadStats() {
        reloadFlashcardsLearntLabel();
        reloadDailyGoalLabel();
    }

    private void reloadFlashcardsLearntLabel() {
        flashcardsLearntLabel.setText(String.format("Learnt: %d/%d", BoxManager.getLearntBox().size(), DataManager.getAllFlashcards().length));
    }

    private void reloadDailyGoalLabel() {
        dailyGoalLabel.setText(String.format("Daily Goal: %d/%d", DataManager.getDailyActivity(LocalDate.now()).getBoxTotalAnswers(), DataManager.getCurrentProfile().getDailyGoal()));
    }

}
