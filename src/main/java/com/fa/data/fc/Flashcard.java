package com.fa.data.fc;

import com.fa.data.AppMode;
import com.fa.data.DailyActivity;
import com.fa.io.DataManager;
import com.fa.io.XMLWriter;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;

public class Flashcard {
    
    private int id;
    private int fileOrdinal;

    private final FlashcardStats flashcardStats;

    private String categoryName;

    private File sourceFile;

    public Flashcard() {
        flashcardStats = new FlashcardStats();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFileOrdinal(int fileOrdinal) {
        this.fileOrdinal = fileOrdinal;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setTotalAnswers(int totalAnswers) {
        flashcardStats.setTotalAnswers(totalAnswers);
    }

    public void setCorrectAnswers(int correctAnswers) {
        flashcardStats.setCorrectAnswers(correctAnswers);
    }

    public void setLastAnswered(LocalDate lastAnsweredDate) {
        flashcardStats.setLastAnswered(lastAnsweredDate);
    }

    public int getId() {
        return id;
    }

    public int getFileOrdinal() {
        return fileOrdinal;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public int getTotalAnswers() {
        return flashcardStats.getTotalAnswers();
    }

    public int getCorrectAnswers() {
        return flashcardStats.getCorrectAnswers();
    }

    public LocalDate getLastAnsweredDate() {
        return flashcardStats.getLastAnsweredDate();
    }

    public double getSuccessRatio() {
        return flashcardStats.getSuccessRatio();
    }

    public void answer(boolean isCorrect, AppMode source) {
        if (wasAnsweredToday()) {
            return;
        }
        flashcardStats.updateStats(isCorrect);
        DailyActivity activity = DataManager.getDailyActivity(flashcardStats.getLastAnsweredDate());
        activity.updateStats(isCorrect);
        if (source == AppMode.REVISION) {
            activity.updateBoxStats(isCorrect);
        }

        XMLWriter.updateFlashcardStats(this, DataManager.getCurrentProfile());
        XMLWriter.updateDailyActivity(activity, DataManager.getCurrentProfile());
    }

    public boolean wasAnsweredToday() {
        return flashcardStats.wasAnsweredToday();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Flashcard flashcard)) {
            return false;
        }

        return id == flashcard.id && fileOrdinal == flashcard.fileOrdinal && Objects.equals(categoryName, flashcard.categoryName) && Objects.equals(sourceFile, flashcard.sourceFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileOrdinal, categoryName, sourceFile);
    }

    @Override
    public String toString() {
        return String.format("Flashcard{id=%d, fileOrdinal=%d, categoryName='%s', sourceFile='%s'}",
                id, fileOrdinal, categoryName, sourceFile);
    }

}
