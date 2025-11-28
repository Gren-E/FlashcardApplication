package com.fa.gui.dialogs;

import com.fa.data.Box;
import com.fa.data.fc.BoxManagerUpdater;
import com.fa.gui.AppFonts;
import com.fa.gui.ComponentFactory;
import com.fa.util.gui.GBC;
import com.fa.util.listeners.TextFieldListener;
import org.apache.log4j.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

public class BoxesCreatorDialog extends CreatorDialog {

    private static final Logger LOG = Logger.getLogger(BoxesCreatorDialog.class);

    private final JPanel boxesPanel;

    private final List<JTextField> durationTextFields;

    private final BoxManagerUpdater boxManagerUpdater;

    public BoxesCreatorDialog(DialogUser dialogUser) {
        super(dialogUser);
        setSize(450, 420);
        setLocationRelativeTo(dialogUser.getJComponent());

        boxManagerUpdater = new BoxManagerUpdater();

        durationTextFields = new ArrayList<>();

        acceptButton.setEnabled(true);

        boxesPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout(), false);
        parametersPanel.add(boxesPanel, new GBC(0,0).setWeight(1,1).setFill(GBC.BOTH).setInsets(10,15,10,15));
        JScrollPane parametersScrollPane = ComponentFactory.createVerticalJScrollPane(parametersPanel);

        titleLabel.setText("Configure boxes.");

        JLabel warningLabel = ComponentFactory.createAccentJLabel("* Removing a box will move it's contents to the box above.");

        mainPanel.add(titleLabel, new GBC(0, 0).setWeight(1,0).setInsets(10,10,5,10));
        mainPanel.add(parametersScrollPane, new GBC(0, 1).setWeight(1,1).setFill(GBC.BOTH).setInsets(5,10,5,10));
        mainPanel.add(warningLabel, new GBC(0,2).setWeight(1,0).setAnchor(GBC.CENTER).setInsets(5,0,5,0));
        mainPanel.add(buttonPanel, new GBC(0,3).setWeight(1,0).setInsets(5,10,20,10));

        loadBoxes();
    }

    private void loadBoxes() {
        durationTextFields.clear();
        boxesPanel.removeAll();

        JLabel boxTitle = ComponentFactory.createStandardUnderlinedJLabel("Box:");
        JLabel flashcardsTitle = ComponentFactory.createStandardUnderlinedJLabel("Flashcards:");
        JLabel durationTitle = ComponentFactory.createStandardUnderlinedJLabel("Duration:");

        boxesPanel.add(boxTitle, new GBC(1,0).setAnchor(GBC.WEST).setInsets(0,5,5,0));
        boxesPanel.add(flashcardsTitle, new GBC(3,0).setAnchor(GBC.WEST).setInsets(0,0,5,10));
        boxesPanel.add(durationTitle, new GBC(4,0).setAnchor(GBC.WEST).setInsets(0,0,5,10));

        Box[] boxes = boxManagerUpdater.getBoxes();
        for (int i = 0; i < boxes.length; i++) {
            loadBoxSpecs(i, boxes);
        }
    }

    private void loadBoxSpecs(int index, Box[] boxes) {
        if (index < 0 || index >= boxes.length) {
            throw new IndexOutOfBoundsException(String.format("Index: %d out of bounds.", index));
        }

        JLabel boxLabel = ComponentFactory.createStandardJLabel();
        boxLabel.setText(index == 0 ? "Reserve" : (index == boxes.length - 1 ? "Learnt" : "Box: " + index));

        JLabel flashcardsLabel = ComponentFactory.createStandardJLabel(String.format("(%d)", boxes[index].size()));

        JLabel addBoxLabel = ComponentFactory.createStandardInteractiveJLabel("✚", () -> addBox(index));
        JLabel deleteBoxLabel = ComponentFactory.createStandardInteractiveJLabel("✘", () -> removeBox(index));
        addBoxLabel.setFont(AppFonts.getSymbolFont());
        deleteBoxLabel.setFont(AppFonts.getSymbolFont());

        JTextField boxDurationField = ComponentFactory.createStandardJTextField(String.valueOf(boxes[index].getDuration()));
        boxDurationField.getDocument().addDocumentListener(new TextFieldListener(() -> acceptButton.setEnabled(updateDurationFields())));
        boxDurationField.setHorizontalAlignment(SwingConstants.LEFT);
        durationTextFields.add(boxDurationField);

        if (index == 0 || index == boxes.length - 1) {
            deleteBoxLabel.setVisible(false);
            boxDurationField.setVisible(false);
        }

        if (index == boxes.length - 1) {
            addBoxLabel.setVisible(false);
        }

        boxesPanel.add(deleteBoxLabel, new GBC(0, index + 1).setWeight(0, 0).setAnchor(GBC.WEST).setInsets(0, 10, 0, 5));
        boxesPanel.add(boxLabel, new GBC(1, index + 1).setWeight(0, 0).setAnchor(GBC.WEST).setInsets(0, 5, 0, 0));
        boxesPanel.add(addBoxLabel, new GBC(2, index + 1).setAnchor(GBC.WEST).setInsets(0, 5, 0, 60));
        boxesPanel.add(flashcardsLabel, new GBC(3, index + 1).setWeight(1, 0).setFill(GBC.HORIZONTAL).setInsets(0, 0, 0, 10));
        boxesPanel.add(boxDurationField, new GBC(4, index + 1).setWeight(1, 1).setAnchor(GBC.WEST).setInsets(0, 0, 0, 20).setIpad(15, 0));
    }

    public boolean updateDurationFields() {
        for (int i = 0; i < durationTextFields.size(); i++) {
            try {
                int duration = Integer.parseInt(durationTextFields.get(i).getText());
                if (!boxManagerUpdater.setDuration(i + 1, duration)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                LOG.warn("Input must be a number.");
                return false;
            }
        }

        return true;
    }

    private void addBox(int index) {
        if (!boxManagerUpdater.addBox(index + 1, 1)) {
            LOG.error("Could not add box.");
            return;
        }
        loadBoxes();
        revalidate();
    }

    private void removeBox(int index) {
        if (!boxManagerUpdater.removeBox(index)) {
            LOG.error("Could not remove box.");
            return;
        }
        loadBoxes();
        revalidate();
    }

    @Override
    public void handleAccept() {
        boxManagerUpdater.updateBoxManager();
        super.handleAccept();
    }

    @Override
    protected DialogMode getDialogMode() {
        return DialogMode.BOXES;
    }

}
