package com.fa.mode.exam;

import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExamBuilder {

    private final Set<String> categories;

    private int durationInMinutes;
    private int numberOfQuestions;

    public ExamBuilder() {
        categories = new HashSet<>();
    }

    public ExamBuilder setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public ExamBuilder setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
        return this;
    }

    public ExamBuilder setCategories(String[] categories) {
        this.categories.clear();
        this.categories.addAll(Arrays.asList(categories));
        return this;
    }

    public boolean isValid(List<String> errors) {
        if (durationInMinutes <= 0) {
            errors.add("Duration must be a positive number.");
        }

        if (numberOfQuestions <= 0) {
            errors.add("Number of questions must be a positive number.");
        }

        if (categories.isEmpty()) {
            errors.add("Number of categories must be a positive number.");
        }

        Set<String> allCategories = Set.of(DataManager.getAllCategoriesNames());
        if (!allCategories.containsAll(categories)) {
            errors.add("Not all categories are valid.");
        }

        return errors.isEmpty();
    }

    public Exam build() {
        if (!isValid(new ArrayList<>())) {
            throw new UnsupportedOperationException("Cannot build an exam because parameters are not valid.");
        }

        List<Flashcard> flashcards = new ArrayList<>();
        for (String category : categories) {
            flashcards.addAll(Arrays.asList(DataManager.getAllFlashcardsFromCategory(category)));
        }

        if (numberOfQuestions > flashcards.size()) {
            throw new UnsupportedOperationException("Cannot build an exam because there is not enough flashcards in given categories.");
        }

        Collections.shuffle(flashcards);
        Flashcard[] examFlashcards = new Flashcard[numberOfQuestions];

        for (int i = 0; i < numberOfQuestions; i++) {
            examFlashcards[i] = flashcards.get(i);
        }

        return new Exam(examFlashcards, durationInMinutes);
    }

}
