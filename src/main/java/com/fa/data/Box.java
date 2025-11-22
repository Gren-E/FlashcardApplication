package com.fa.data;

import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Box {

    private int index;
    private int duration;

    private LinkedList<Integer> flashcardsIds;

    public Box(int index) {
        this.index = index;
        flashcardsIds = new LinkedList<>();
    }

    public void addLastFlashcard(int flashcardId) {
        flashcardsIds.addLast(flashcardId);
    }

    public void addLastFlashcard(Flashcard flashcard) {
        flashcardsIds.addLast(flashcard.getId());
    }

    public Integer[] getFlashcardsIds() {
        return flashcardsIds.toArray(new Integer[0]);
    }

    public Flashcard[] getFlashcards() {
        return flashcardsIds.stream().map(DataManager::getFlashcard).toArray(Flashcard[]::new);
    }

    public Flashcard getNextUnansweredFlashcard() {
        LocalDate latestAcceptedDate = LocalDate.now().minusDays(getDuration());

        for (Flashcard flashcard : getFlashcards()) {
            LocalDate lastAnswered = flashcard.getBoxLastAnswered();
            if (lastAnswered == null || lastAnswered.isBefore(latestAcceptedDate) || lastAnswered.isEqual(latestAcceptedDate)) {
                return flashcard;
            }
        }

        return null;
    }

    public int getUnansweredFlashcardsCount() {
        LocalDate latestAcceptedDate = LocalDate.now().minusDays(getDuration());
        return (int) Arrays.stream(getFlashcards()).filter(flashcard -> canFlashcardBeAnswered(flashcard, latestAcceptedDate)).count();
    }

    private static boolean canFlashcardBeAnswered(Flashcard flashcard, LocalDate latestAcceptedDate) {
        LocalDate lastAnswered = flashcard.getBoxLastAnswered();
        return lastAnswered == null || lastAnswered.isBefore(latestAcceptedDate) || lastAnswered.isEqual(latestAcceptedDate);
    }

    public Flashcard getRandomFlashcard() {
        if (flashcardsIds.isEmpty()) {
            return null;
        }

        List<Integer> flashcardsIdsCopy = new ArrayList<>(flashcardsIds);
        Collections.shuffle(flashcardsIdsCopy);
        for (Integer flashcardId : flashcardsIdsCopy) {
            Flashcard result = DataManager.getFlashcard(flashcardId);
            if (!result.wasAnsweredInBoxToday()) {
                return result;
            }
        }

        return null;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getDuration() {
        return duration;
    }

    public int getIndex() {
        return index;
    }

    public int size() {
        return flashcardsIds.size();
    }

    public boolean containsFlashcard(int flashcardId) {
        return flashcardsIds.contains(flashcardId);
    }

    public void removeFlashcard(int flashcardId) {
        flashcardsIds.remove(Integer.valueOf(flashcardId));
    }

    @Override
    public Box clone() throws CloneNotSupportedException {
        Box box = new Box(index);
        box.duration = duration;
        box.flashcardsIds = new LinkedList<>(flashcardsIds);
        return box;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Box otherBox) {
            return otherBox.getIndex() == index;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Box{id=" + index + '}';
    }

}
