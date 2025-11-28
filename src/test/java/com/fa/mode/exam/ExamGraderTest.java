package com.fa.mode.exam;

import com.fa.AbstractTest;
import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExamGraderTest extends AbstractTest {

    @Test
    public void examGraderTest() {
        Flashcard[] flashcards = {DataManager.getFlashcard(1), DataManager.getFlashcard(2)};
        Exam exam = new Exam(flashcards, 5);
        exam.start();
        exam.getNextUnansweredQuestion().answer(true);
        exam.getNextUnansweredQuestion().answer(false);
        ExamGrader grader = new ExamGrader(exam);
        Assertions.assertEquals(ExamGrader.GRADE_BRONZE, grader.calculateGrade());
        Assertions.assertTrue(grader.passed());
        Assertions.assertEquals(50, grader.getPercentage());
    }
}
