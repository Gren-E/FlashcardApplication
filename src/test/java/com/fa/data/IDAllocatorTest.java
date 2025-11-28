package com.fa.data;

import com.fa.AbstractTest;
import com.fa.io.DataManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IDAllocatorTest extends AbstractTest {

    @Test
    public void idAllocatorTest() {
        Assertions.assertEquals(7, IDAllocator.getNextFlashcardId(DataManager.getProfile(1)));
        Assertions.assertEquals(4, IDAllocator.getNextProfileID());
    }

}
