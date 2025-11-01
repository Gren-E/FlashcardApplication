package com.fa.mode.exam;

import com.fa.data.fc.Flashcard;
import com.fa.mode.Question;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Exam {

    private Question[] questions;

    private LocalDateTime startTimestamp;

    private int durationInMinutes;

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
        return LocalDateTime.now().isAfter(getFinishTimestamp());
    }

    public Question getQuestion(int index) {
        return questions[index];
    }

    public int countQuestions() {
        return questions.length;
    }

    public void answer(int index, boolean answeredCorrectly) {
        getQuestion(index).answer(answeredCorrectly);
    }

    public Question getNextUnansweredQuestion(int currentIndex) {
        if(!(currentIndex >= 0 && currentIndex < countQuestions())) {
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

    public LocalDateTime getFinishTimestamp() {
        return startTimestamp.plusMinutes(durationInMinutes);
    }
}
