package com.fa.data;

import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;
import com.fa.io.XMLReader;
import com.fa.io.XMLWriter;

public class BoxManager {

    private static Box[] boxes;

    public static void reload() {
        Profile profile = DataManager.getCurrentProfile();
        boxes = XMLReader.loadBoxes(profile);
    }

    public static Box findBox(int flashcardId) {
        for (Box box : boxes) {
            if (box.containsFlashcard(flashcardId)) {
                return box;
            }
        }

        return null;
    }

    public static Flashcard getNextUnansweredFlashcard() {
        Flashcard result = null;
        for (int i = boxes.length - 2; i >= 0; i--) {
            result = boxes[i].getNextUnansweredFlashcard();
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    public static void transferTo(int flashcardId, Box targetBox) {
        Box originalBox = findBox(flashcardId);
        if (originalBox != null) {
            originalBox.removeFlashcard(flashcardId);
        }

        targetBox.addLastFlashcard(flashcardId);
    }

    public static void handleSuccessfulAnswer(int flashcardId) {
        Box originalBox = findBox(flashcardId);
        if (originalBox == null) {
            throw new IllegalArgumentException("Could not locate flashcard in boxes.");
        }
        Flashcard flashcard = DataManager.getFlashcard(flashcardId);
        flashcard.answer(true, AppMode.REVISION);
        if (originalBox.equals(getLearntBox())) {
            return;
        } else if (originalBox.equals(getReserveBox())) {
            transferTo(flashcardId, boxes[2]);
        } else {
            transferTo(flashcardId, getNextBox(originalBox));
        }

        XMLWriter.saveBoxData(DataManager.getCurrentProfile());
    }

    public static void handleFailedAnswer(int flashcardId) {
        Box originalBox = findBox(flashcardId);
        if (originalBox == null) {
            throw new IllegalArgumentException("Could not locate flashcard in boxes.");
        }
        Flashcard flashcard = DataManager.getFlashcard(flashcardId);
        flashcard.answer(false, AppMode.REVISION);

        transferTo(flashcardId, boxes[1]);
        XMLWriter.saveBoxData(DataManager.getCurrentProfile());
    }

    public static Box[] getBoxes() {
        return boxes;
    }

    public static Box getBox(int index) {
        return boxes[index];
    }

    public static Box getLearntBox() {
        return boxes[boxes.length - 1];
    }

    public static Box getReserveBox() {
        return boxes[0];
    }

    public static Box getNextBox(Box box) {
        for (int i = 0; i < boxes.length; i++) {
            if (box.equals(boxes[i])) {
                return i != boxes.length - 1 ? boxes[i + 1] : null;
            }
        }
        throw new IllegalArgumentException("Box: " + box + "does not exist in current profile.");
    }

}
