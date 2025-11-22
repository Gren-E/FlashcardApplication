package com.fa.data;

public class AnswerStats {

    private int totalAnswers;
    private int correctAnswers;

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
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

    public void updateStats(boolean isCorrect, AppMode mode) {
        correctAnswers += (isCorrect) ? 1 : 0;
        totalAnswers++;
    }

}
