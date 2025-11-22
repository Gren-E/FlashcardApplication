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

    public void setCategoryLastAnswered(LocalDate date) {
        flashcardStats.setCategoryLastAnswered(date);
    }

    public void setCategoryLastAnsweredCorrectly(LocalDate date) {
        flashcardStats.setCategoryLastAnsweredCorrectly(date);
    }

    public void setBoxLastAnswered(LocalDate date) {
        flashcardStats.setBoxLastAnswered(date);
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

    public LocalDate getCategoryLastAnswered() {
        return flashcardStats.getCategoryLastAnswered();
    }

    public LocalDate getCategoryLastAnsweredCorrectly() {
        return flashcardStats.getCategoryLastAnsweredCorrectly();
    }

    public LocalDate getBoxLastAnswered() {
        return flashcardStats.getBoxLastAnswered();
    }

    public double getSuccessRatio() {
        return flashcardStats.getSuccessRatio();
    }

    public void answer(boolean isCorrect, AppMode source) {
        if (wasAnsweredInCategoryToday() && AppMode.CATEGORY_BROWSER.equals(source)) {
            return;
        }

        flashcardStats.updateStats(isCorrect, source);
        if (source == AppMode.REVISION) {
            DailyActivity activity = DataManager.getDailyActivity(getBoxLastAnswered());
            activity.updateStats(isCorrect, source);
            activity.updateBoxStats(isCorrect);
            XMLWriter.updateDailyActivity(activity, DataManager.getCurrentProfile());
        }

        XMLWriter.updateFlashcardStats(this, DataManager.getCurrentProfile());
    }

    public boolean wasAnsweredInCategoryToday() {
        return flashcardStats.wasAnsweredInCategoryToday();
    }

    public boolean wasAnsweredCorrectlyToday() {
        return flashcardStats.wasAnsweredCorrectlyToday();
    }

    public boolean wasAnsweredInBoxToday() {
        return flashcardStats.wasAnsweredInBoxToday();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Flashcard flashcard) {
            return id == flashcard.id && fileOrdinal == flashcard.fileOrdinal && Objects.equals(categoryName, flashcard.categoryName)
                    && Objects.equals(sourceFile, flashcard.sourceFile);
        }

        return false;
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
