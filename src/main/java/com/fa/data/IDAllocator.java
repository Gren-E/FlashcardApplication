package com.fa.data;

import com.fa.io.XMLReader;

public class IDAllocator {

    private static int nextFlashcardId = -1;
    private static int nextProfileId = -1;

    public static synchronized int getNextFlashcardId(Profile profile) {
        if (nextFlashcardId < 0) {
            nextFlashcardId = XMLReader.getLastFlashcardId(profile) + 1;
        }

        return nextFlashcardId++;
    }

    public static int getNextProfileID() {
        if (nextProfileId < 0) {
            nextProfileId = XMLReader.getLastProfileId() + 1;
        }

        return nextProfileId++;
    }

}
