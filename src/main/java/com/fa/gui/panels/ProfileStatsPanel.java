package com.fa.gui.panels;

import com.fa.AppEnv;
import com.fa.data.BoxManager;
import com.fa.gui.AppColorPalette;
import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.util.gui.ComponentUtil;
import com.fa.util.gui.ImageUtil;
import com.fa.util.gui.components.EmbellishedStringIcon;
import com.fa.util.gui.components.ProgressChart;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;

public class ProfileStatsPanel extends JPanel {

    private final EmbellishedStringIcon dailyGoalStreak;

    private final ProgressChart flashcardsLearntChart;
    private final ProgressChart dailyGoalChart;

    public ProfileStatsPanel() {
        setOpaque(false);
        setMaximumSize(new Dimension(getWidth(), 100));
        setPreferredSize(new Dimension(getWidth(), 100));

        JLabel dailyGoalStreakLabel = ComponentFactory.createTitleJLabel("Daily Goal Streak:");
        JLabel flashcardsLearntLabel = ComponentFactory.createTitleJLabel("Learnt:");
        JLabel dailyGoalLabel = ComponentFactory.createTitleJLabel("Daily Goal:");

        Image streakIcon = ImageUtil.readImage(new File(AppEnv.getImageDirectory(), "embellishment.png"));

        dailyGoalStreak = ComponentFactory.createEmbellishedStringIcon(streakIcon, 145, 160);
        dailyGoalStreak.setForeground(AppColorPalette.getAchievementColor());

        JPanel streakPanel = ComponentUtil.createEmptyPanel(new BorderLayout());
        streakPanel.add(dailyGoalStreakLabel, BorderLayout.NORTH);
        streakPanel.add(dailyGoalStreak, BorderLayout.CENTER);

        dailyGoalChart = ComponentFactory.createStandardProgressChart();
        dailyGoalChart.setDiameter(160);
        dailyGoalChart.setProgressColor(AppColorPalette.getAchievementColor());

        JPanel dailyGoalPanel = ComponentUtil.createEmptyPanel(new BorderLayout());
        dailyGoalPanel.add(dailyGoalLabel, BorderLayout.NORTH);
        dailyGoalPanel.add(dailyGoalChart, BorderLayout.CENTER);

        flashcardsLearntChart = ComponentFactory.createStandardProgressChart();
        flashcardsLearntChart.setDiameter(160);
        flashcardsLearntChart.setProgressColor(AppColorPalette.getProgressColor());

        JPanel flashcardsLearntPanel = ComponentUtil.createEmptyPanel(new BorderLayout());
        flashcardsLearntPanel.add(flashcardsLearntLabel, BorderLayout.NORTH);
        flashcardsLearntPanel.add(flashcardsLearntChart, BorderLayout.CENTER);

        setBorder(new EmptyBorder(20,20,20,20));
        setLayout(new GridLayout(1,3));
        add(flashcardsLearntPanel);
        add(streakPanel);
        add(dailyGoalPanel);
    }

    public void reloadStats() {
        reloadFlashcardsLearntChart();
        reloadDailyGoalChart();
        reloadStreakLabel();
    }

    private void reloadFlashcardsLearntChart() {
        if (DataManager.getCurrentProfile() != null) {
            flashcardsLearntChart.setMaxValue(DataManager.getAllFlashcards().length);
        }

        flashcardsLearntChart.setCurrentValue(BoxManager.getLearntBox().size());
    }

    private void reloadDailyGoalChart() {
        if (DataManager.getCurrentProfile() != null) {
            dailyGoalChart.setMaxValue(DataManager.getCurrentProfile().getDailyGoal());
        }

        dailyGoalChart.setCurrentValue(DataManager.getDailyActivity(LocalDate.now()).getBoxCorrectAnswers());
    }

    private void reloadStreakLabel() {
        if (DataManager.getCurrentProfile() != null) {
            dailyGoalStreak.setText(String.valueOf(DataManager.getDailyActivityStreak()));
        }
    }

}
