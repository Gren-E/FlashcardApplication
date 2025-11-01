package com.fa.data;

import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Box {

    private final int id;
    private int duration;

    private final LinkedList<Integer> flashcardsIds;

    public Box(int id) {
        this.id = id;
        flashcardsIds = new LinkedList<>();
    }

    public void addLastFlashcard(int flashcardId) {
        flashcardsIds.addLast(flashcardId);
    }

    public void addLastFlashcard(Flashcard flashcard) {
        flashcardsIds.addLast(flashcard.getId());
    }

    public void shuffle() {
        Collections.shuffle(flashcardsIds);
    }

    public Flashcard pollFlashcard() {
        Integer id = flashcardsIds.poll();
        return DataManager.getFlashcard(id);
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
            LocalDate lastAnswered = flashcard.getLastAnsweredDate();
            if (lastAnswered == null || lastAnswered.isBefore(latestAcceptedDate) || lastAnswered.isEqual(latestAcceptedDate)) {
                return flashcard;
            }
        }

        return null;
    }

    public Flashcard pollRandomFlashcard() {
        Random random = new Random();
        int id = !flashcardsIds.isEmpty() ? flashcardsIds.remove(random.nextInt(flashcardsIds.size())) : -1;
        return DataManager.getFlashcard(id);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public int getBoxId() {
        return id;
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
    public boolean equals(Object other) {
        if (other instanceof Box otherBox) {
            return otherBox.getBoxId() == id;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Box{id=" + id + '}';
    }

}
