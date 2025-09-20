package com.fa.data;

import com.fa.io.XMLReader;

public class IDAllocator {

    private static int nextFlashcardId = -1;

    public static synchronized int getNextFlashcardId() {
        if (nextFlashcardId < 0) {
            nextFlashcardId = XMLReader.getLastId() + 1;
        }

        return nextFlashcardId++;
    }
}
