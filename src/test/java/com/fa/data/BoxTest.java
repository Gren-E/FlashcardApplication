package com.fa.data;

import com.fa.AbstractTest;
import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class BoxTest extends AbstractTest {

    @Test
    public void boxTest() {
        Box box = new Box(2);
        Assertions.assertEquals(2, box.getIndex());
        Assertions.assertEquals(0, box.getDuration());
        Assertions.assertEquals(0, box.size());
        Assertions.assertArrayEquals(new Flashcard[0], box.getFlashcardsIds());
        Assertions.assertNull(box.getRandomFlashcard());

        Flashcard flashcard = new Flashcard();
        flashcard.setId(3);
        try (MockedStatic<DataManager> dataManager = Mockito.mockStatic(DataManager.class)) {
            dataManager.when(() -> DataManager.getFlashcard(3)).thenReturn(flashcard);

            box.addLastFlashcard(flashcard);
            box.setDuration(3);
            Assertions.assertEquals(1, box.size());
            Assertions.assertEquals(3, box.getDuration());

            box.addLastFlashcard(flashcard);
            Assertions.assertEquals(flashcard, box.getRandomFlashcard());
            Assertions.assertEquals(2, box.size());
        }
    }

    @Test
    public void boxManagerTest() {
        Box box0 = new Box(0);
        Box box1 = new Box(1);
        Box box2 = new Box(2);
        Flashcard flashcard = DataManager.getFlashcard(5);
        box2.addLastFlashcard(flashcard);
        BoxManager.setBoxes(new Box[] {box0, box1, box2});

        Assertions.assertEquals(box0, BoxManager.getReserveBox());
        Assertions.assertEquals(box1, BoxManager.getBoxes()[1]);
        Assertions.assertEquals(box2, BoxManager.getLearntBox());
        Assertions.assertEquals(flashcard, BoxManager.getRandomLearntFlashcard());

        Assertions.assertEquals(0, BoxManager.getBoxes()[1].size());
        BoxManager.handleFailedAnswer(5);
        Assertions.assertEquals(0, BoxManager.getLearntBox().size());
        Assertions.assertEquals(1, BoxManager.getBoxes()[1].size());
    }

}
