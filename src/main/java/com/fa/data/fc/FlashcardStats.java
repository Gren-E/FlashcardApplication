package com.fa.data.fc;

import com.fa.data.AnswerStats;
import com.fa.data.AppMode;

import java.time.LocalDate;

public class FlashcardStats extends AnswerStats {

    private LocalDate categoryLastAnswered;
    private LocalDate categoryLastAnsweredCorrectly;
    private LocalDate boxLastAnswered;

    public void setCategoryLastAnswered(LocalDate date) {
        categoryLastAnswered = date;
    }

    public void setCategoryLastAnsweredCorrectly(LocalDate date) {
        categoryLastAnsweredCorrectly = date;
    }

    public void setBoxLastAnswered(LocalDate date) {
        boxLastAnswered = date;
    }

    public LocalDate getCategoryLastAnswered() {
        return categoryLastAnswered;
    }

    public LocalDate getCategoryLastAnsweredCorrectly() {
        return categoryLastAnsweredCorrectly;
    }

    public LocalDate getBoxLastAnswered() {
        return boxLastAnswered;
    }

    public boolean wasAnsweredInCategoryToday() {
        return LocalDate.now().equals(categoryLastAnswered);
    }

    public boolean wasAnsweredCorrectlyToday() {
        return LocalDate.now().equals(categoryLastAnsweredCorrectly);
    }

    public boolean wasAnsweredInBoxToday() {
        return LocalDate.now().equals(boxLastAnswered);
    }

    @Override
    public void updateStats(boolean isAnswerCorrect, AppMode mode) {
        super.updateStats(isAnswerCorrect, mode);
        LocalDate answerDate = LocalDate.now();

        if (mode == AppMode.CATEGORY_BROWSER) {
            categoryLastAnswered = answerDate;
            if (isAnswerCorrect) {
                categoryLastAnsweredCorrectly = answerDate;
            }
        }

        if (mode == AppMode.REVISION) {
            boxLastAnswered = answerDate;
        }
    }

}
