package com.fa.data.fc;

import java.time.LocalDate;


public class FlashcardStats {

    private int totalAnswers;
    private int correctAnswers;

    private LocalDate lastAnswered;

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public void setLastAnswered(LocalDate date) {
        this.lastAnswered = date;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public double getSuccessRatio() {
        if (totalAnswers == 0) {
            return Double.NaN;
        }
        return (double) correctAnswers / totalAnswers;
    }

    public void answer(boolean isCorrect) {
        if (wasAnsweredToday()) {
            return;
        }
        correctAnswers += (isCorrect) ? 1 : 0;
        totalAnswers++;
        lastAnswered = LocalDate.now();
    }

    public boolean wasAnsweredToday() {
        return LocalDate.now().equals(lastAnswered);
    }

    public LocalDate getLastAnsweredDate() {
        return lastAnswered;
    }
}
