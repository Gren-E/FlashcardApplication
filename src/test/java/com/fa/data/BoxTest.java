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

}
