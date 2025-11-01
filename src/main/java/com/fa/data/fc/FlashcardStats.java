package com.fa.data.fc;

import com.fa.data.AnswerStats;

import java.time.LocalDate;

public class FlashcardStats extends AnswerStats {

    private LocalDate lastAnswered;

    public void setLastAnswered(LocalDate date) {
        this.lastAnswered = date;
    }

    public LocalDate getLastAnsweredDate() {
        return lastAnswered;
    }

    public boolean wasAnsweredToday() {
        return LocalDate.now().equals(lastAnswered);
    }

    @Override
    public void updateStats(boolean isAnswerCorrect) {
        super.updateStats(isAnswerCorrect);
        lastAnswered = LocalDate.now();
    }

}
