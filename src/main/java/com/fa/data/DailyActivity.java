package com.fa.data;

import com.fa.io.DataManager;

import java.time.LocalDate;

public class DailyActivity extends AnswerStats {

    private final LocalDate date;

    private int boxTotalAnswers;
    private int boxCorrectAnswers;

    public DailyActivity(LocalDate date) {
        this.date = date;
    }

    public void setBoxTotalAnswers(int totalAnswers) {
        this.boxTotalAnswers = totalAnswers;
    }

    public void setBoxCorrectAnswers(int correctAnswers) {
        this.boxCorrectAnswers = correctAnswers;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getBoxTotalAnswers() {
        return boxTotalAnswers;
    }

    public int getBoxCorrectAnswers() {
        return boxCorrectAnswers;
    }

    public boolean isDailyGoalAchieved() {
        if (DataManager.getCurrentProfile() != null) {
            return boxCorrectAnswers >= DataManager.getCurrentProfile().getDailyGoal();
        }

        return false;
    }

    public void updateBoxStats(boolean isCorrect) {
        boxCorrectAnswers += (isCorrect) ? 1 : 0;
        boxTotalAnswers++;
    }

}
