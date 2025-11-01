package com.fa.gui.panels;

import com.fa.data.Box;
import com.fa.data.BoxManager;
import com.fa.data.fc.Flashcard;
import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.util.gui.GBC;
import org.apache.log4j.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;

public class BoxesRevisionPanel extends LearningModesPanel {

    private static final Logger LOG = Logger.getLogger(BoxesRevisionPanel.class);

    private final JPanel boxesInfoPanel;

    public BoxesRevisionPanel(AppWindow parent) {
        super(parent);

        boxesInfoPanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout());

        flashcardDisplayPanel.setNoImageMessage("Revision finished!");

        correctAnswerButton.setActionListener(event -> answer(true));
        wrongAnswerButton.setActionListener(event -> answer(false));

        add(exitBrowserButton, new GBC(0, 0,2,1).setAnchor(GBC.EAST).setInsets(10));
        add(boxesInfoPanel, new GBC(0,1,1,2).setWeight(0.2,1).setFill(GBC.BOTH).setInsets(5,50,50,5));
        add(displayPanel, new GBC(1, 1).setWeight(0.8, 1).setFill(GBC.BOTH).setInsets(5,5,5,50));
        add(buttonPanel, new GBC(1, 2).setInsets(5,0,50,0));
    }

    public void reload() {
        loadBoxesInfoPanel();
        startRevision();
    }

    private void loadBoxesInfoPanel() {
        Box[] boxes = BoxManager.getBoxes();

        boxesInfoPanel.removeAll();
        for (int i = 0; i < boxes.length; i++) {
            JLabel boxLabel = ComponentFactory.createAccentJLabel();
            if (boxes[i].equals(BoxManager.getReserveBox())) {
                boxLabel.setText("Reserve: " + boxes[i].size());
            } else if (boxes[i].equals(BoxManager.getLearntBox())) {
                boxLabel.setText("Learnt: " + boxes[i].size());
            } else {
                boxLabel.setText(String.format("Box %d: %d", boxes[i].getBoxId(), boxes[i].size()));
            }
            boxesInfoPanel.add(boxLabel, new GBC(0, i));
        }
        revalidate();
    }

    private void answer(boolean isCorrect) {
        Flashcard flashcard = flashcardDisplayPanel.getFlashcard();
        if (flashcard == null) {
            LOG.error("Cannot answer because flashcard is null.");
            return;
        }
        if (isCorrect) {
            BoxManager.handleSuccessfulAnswer(flashcard.getId());
        } else {
            BoxManager.handleFailedAnswer(flashcard.getId());
        }
        flashcardDisplayPanel.setFlashcard(BoxManager.getNextUnansweredFlashcard());
        loadBoxesInfoPanel();
    }

    private void startRevision() {
        flashcardDisplayPanel.setFlashcard(BoxManager.getNextUnansweredFlashcard());
    }

}
