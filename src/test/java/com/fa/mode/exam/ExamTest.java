package com.fa.mode.exam;

import com.fa.AbstractTest;
import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;
import com.fa.mode.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ExamTest extends AbstractTest {

    @Test
    public void examTest() {
        Flashcard flashcard1 = DataManager.getFlashcard(1);
        Flashcard flashcard2 = DataManager.getFlashcard(2);
        Exam exam = new Exam(new Flashcard[]{flashcard1, flashcard2}, 5);

        Assertions.assertFalse(exam.isTimeUp());
        exam.start();

        Assertions.assertEquals(2, exam.countQuestions());
        Assertions.assertFalse(exam.isTimeUp());
        Assertions.assertFalse(exam.isFinished());
        Assertions.assertEquals(new Question(flashcard1), exam.getQuestion(0));

        Assertions.assertNotEquals(LocalDateTime.now().plusHours(2), exam.getStartTimestamp());

        Question question1 = exam.getNextUnansweredQuestion();
        question1.answer(true);
        Assertions.assertEquals(0, exam.getQuestionIndex(question1));
        Assertions.assertEquals(1, exam.countAnsweredCorrectly());

        Question question2 = exam.getQuestion(1);
        question2.answer(false);
        Assertions.assertEquals(1, exam.countAnsweredIncorrectly());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> exam.getNextUnansweredQuestion(3));
        Assertions.assertTrue(exam.isFinished());
    }
}
