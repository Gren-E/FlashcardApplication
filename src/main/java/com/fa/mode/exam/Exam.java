package com.fa.mode.exam;

import com.fa.data.fc.Flashcard;
import com.fa.mode.Question;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Exam {

    private final Question[] questions;

    private LocalDateTime startTimestamp;

    private final int durationInMinutes;

    public Exam(Flashcard[] flashcards, int durationInMinutes) {
        questions = Arrays.stream(flashcards).map((Question::new)).toArray(Question[]::new);
        this.durationInMinutes = durationInMinutes;
    }

    public void start() {
        startTimestamp = LocalDateTime.now();
    }

    public boolean isFinished() {
        return isTimeUp() || Arrays.stream(questions).allMatch(Question::wasAnswered);
    }

    public boolean isTimeUp() {
        if (startTimestamp == null) {
            return false;
        }

        return LocalDateTime.now().isAfter(getFinishTimestamp());
    }

    public Question getQuestion(int index) {
        return questions[index];
    }

    public int countQuestions() {
        return questions.length;
    }

    public Question getNextUnansweredQuestion(int currentIndex) {
        if(currentIndex < 0 || currentIndex >= countQuestions()) {
            throw new ArrayIndexOutOfBoundsException(String.format("Current index: %d out of bounds (%d questions).", currentIndex, countQuestions()));
        }

        int nextQuestionIndex = currentIndex;
        do {
            if (nextQuestionIndex >= countQuestions()) {
                if (currentIndex == 0) {
                    return null;
                }
                nextQuestionIndex = 0;
            }

            if (!getQuestion(nextQuestionIndex).wasAnswered()) {
                return getQuestion(nextQuestionIndex);
            }

            nextQuestionIndex++;
        } while (nextQuestionIndex != currentIndex);

        return null;
    }

    public Question getNextUnansweredQuestion() {
        return getNextUnansweredQuestion(0);
    }

    public int getQuestionIndex(Question question) {
        for (int i = 0; i < questions.length; i++) {
            if (questions[i].equals(question)) {
                return i;
            }
        }
        return -1;
    }

    public long countAnsweredCorrectly() {
        return Arrays.stream(questions).filter(Question::wasAnsweredCorrectly).count();
    }

    public long countAnsweredIncorrectly() {
        return Arrays.stream(questions).filter(Question::wasAnsweredIncorrectly).count();
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public LocalDateTime getFinishTimestamp() {
        return startTimestamp.plusMinutes(durationInMinutes);
    }

}
