package com.fa.data.fc;

import com.fa.data.Box;
import com.fa.data.BoxManager;
import com.fa.io.DataManager;
import com.fa.io.XMLWriter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BoxManagerUpdater {

    private static final Logger LOG = Logger.getLogger(BoxManagerUpdater.class);

    private final List<Box> boxes;

    public BoxManagerUpdater() {
        if (DataManager.getCurrentProfile() == null) {
            throw new UnsupportedOperationException("Cannot use updater because profile is null.");
        }

        boxes = new ArrayList<>();
        for (Box box : BoxManager.getBoxes()) {
            try {
                Box copy = box.clone();
                boxes.add(copy);
            } catch (CloneNotSupportedException e) {
                String errorMessage = String.format("Could not clone: %s.", box);
                LOG.error(errorMessage, e);
                throw new RuntimeException(errorMessage, e);
            }
        }
    }

    public boolean addBox(int index, int duration) {
        if (index <= 0 || index >= boxes.size()) {
            LOG.error(String.format("Index out of bounds: %d.", index));
            return false;
        }

        if (duration < 1) {
            LOG.error(String.format("Invalid duration, must be positive: %d.", index));
            return false;
        }

        Box newBox = new Box(index);
        newBox.setDuration(duration);
        boxes.add(index, newBox);

        for (int i = index + 1; i < boxes.size(); i++) {
            boxes.get(i).setIndex(i);
        }

        return true;
    }

    public boolean removeBox(int index) {
        if (index <= 0 || index >= boxes.size() - 1) {
            LOG.error(String.format("Index out of bounds: %d.", index));
            return false;
        }

        Integer[] flashcardIds = boxes.get(index).getFlashcardsIds();
        for (Integer id : flashcardIds) {
            boxes.get(index - 1).addLastFlashcard(id);
        }

        boxes.remove(index);
        for (int i = index; i < boxes.size(); i++) {
            boxes.get(i).setIndex(i);
        }

        return true;
    }

    public boolean setDuration(int index, int duration) {
        if (index <= 0 || index >= boxes.size() - 1) {
            LOG.error(String.format("Index out of bounds: %d.", index));
            return false;
        }

        if (duration < 1) {
            LOG.error(String.format("Invalid duration, must be positive: %d.", duration));
            return false;
        }

        boxes.get(index).setDuration(duration);
        return true;
    }

    public void updateBoxManager() {
        BoxManager.setBoxes(boxes.toArray(new Box[0]));
        XMLWriter.saveBoxData(DataManager.getCurrentProfile());
    }

    public Box[] getBoxes() {
        return boxes.toArray(new Box[0]);
    }

}
