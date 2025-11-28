package com.fa.mode;

import com.fa.AbstractTest;
import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionTest extends AbstractTest {

    @Test
    public void questionTest() {
        Flashcard flashcard1 = DataManager.getFlashcard(1);
        Question question1 = new Question(flashcard1);
        Assertions.assertFalse(question1.wasAnswered());

        question1.answer(true);
        Assertions.assertTrue(question1.wasAnswered());
        Assertions.assertTrue(question1.wasAnsweredCorrectly());
        Assertions.assertFalse(question1.wasAnsweredIncorrectly());
        Assertions.assertEquals(flashcard1, question1.getFlashcard());

        Question question2 = new Question(flashcard1);
        Assertions.assertEquals(question1, question2);

        Assertions.assertThrows(NullPointerException.class, () -> new Question(null));
        Question question3 = new Question(DataManager.getFlashcard(2));
        Assertions.assertNotEquals(question1, question3);
    }
}
