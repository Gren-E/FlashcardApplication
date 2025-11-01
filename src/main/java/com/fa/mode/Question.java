package com.fa.mode;

import com.fa.data.fc.Flashcard;

public class Question {

    private final Flashcard flashcard;

    private Boolean answer;

    public Question(Flashcard flashcard) {
        if (flashcard == null) {
            throw new NullPointerException("Flashcard cannot be null.");
        }
        this.flashcard = flashcard;
    }

    public void answer(boolean answeredCorrectly) {
        answer = answeredCorrectly;
    }

    public boolean wasAnswered() {
        return answer != null;
    }

    public boolean wasAnsweredCorrectly() {
        return Boolean.TRUE.equals(answer);
    }

    public boolean wasAnsweredIncorrectly() {
        return Boolean.FALSE.equals(answer);
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Question question) {
            return question.getFlashcard().getId() == flashcard.getId();
        }
        return false;
    }
}
