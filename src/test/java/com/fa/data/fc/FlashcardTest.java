package com.fa.data.fc;

import com.fa.AbstractTest;
import com.fa.data.AppMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class FlashcardTest extends AbstractTest {

    @Test
    public void answeredFlashcardTest() {
        Flashcard flashcard = new Flashcard();
        Assertions.assertFalse(flashcard.wasAnsweredToday());
        Assertions.assertNull(flashcard.getLastAnsweredDate());
        Assertions.assertEquals(0, flashcard.getTotalAnswers());
        Assertions.assertEquals(0, flashcard.getCorrectAnswers());
        Assertions.assertEquals(Double.NaN, flashcard.getSuccessRatio());

        flashcard.answer(true, AppMode.CATEGORY_BROWSER);
        Assertions.assertTrue(flashcard.wasAnsweredToday());
        Assertions.assertEquals(LocalDate.now(), flashcard.getLastAnsweredDate());
        Assertions.assertEquals(1, flashcard.getTotalAnswers());
        Assertions.assertEquals(1, flashcard.getCorrectAnswers());
        Assertions.assertEquals(1.0, flashcard.getSuccessRatio());
    }
}
