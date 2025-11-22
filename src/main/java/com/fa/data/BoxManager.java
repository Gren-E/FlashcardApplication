package com.fa.data;

import com.fa.data.fc.Flashcard;
import com.fa.io.DataManager;
import com.fa.io.XMLReader;
import com.fa.io.XMLWriter;

import java.util.Arrays;

public class BoxManager {

    private static Box[] boxes;
    private static Box activeBox;

    public static void reload() {
        Profile profile = DataManager.getCurrentProfile();
        boxes = XMLReader.loadBoxes(profile);
    }

    public static Box findBox(int flashcardId) {
        return Arrays.stream(boxes).filter(box -> box.containsFlashcard(flashcardId)).findFirst().orElse(null);
    }

    public static Flashcard getNextUnansweredFlashcard() {
        Flashcard result = null;
        activeBox = null;

        for (int i = boxes.length - 2; i >= 0; i--) {
            result = boxes[i].getNextUnansweredFlashcard();
            if (result != null) {
                activeBox = boxes[i];
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

    public static void setBoxes(Box[] boxes) {
        BoxManager.boxes = boxes;
        activeBox = null;
    }

    public static Box[] getBoxes() {
        return boxes;
    }

    public static Box getActiveBox() {
        return activeBox;
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

    public static int getUnansweredFlashcardsCount() {
        return Arrays.stream(boxes).map(Box::getUnansweredFlashcardsCount).reduce(0, Integer::sum);
    }

    public static Flashcard getRandomLearntFlashcard() {
        Flashcard result = getLearntBox().getRandomFlashcard();
        if (result != null) {
            activeBox = getLearntBox();
        }

        return result;
    }

}
