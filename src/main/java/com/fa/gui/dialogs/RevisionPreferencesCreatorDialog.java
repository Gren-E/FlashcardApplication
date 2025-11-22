package com.fa.gui.dialogs;

import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.util.gui.GBC;
import com.fa.util.listeners.TextFieldListener;
import org.apache.log4j.Logger;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class RevisionPreferencesCreatorDialog extends CreatorDialog {

    private static final Logger LOG = Logger.getLogger(RevisionPreferencesCreatorDialog.class);

    private final JTextField dailyGoalField;
    private final JTextField dailyRelearningGoalField;

    public RevisionPreferencesCreatorDialog(DialogUser dialogUser) {
        super(dialogUser);
        setSize(450, 300);
        setLocationRelativeTo(dialogUser.getJComponent());

        JLabel dailyGoalLabel = ComponentFactory.createAccentJLabel("Set revision daily goal:");
        JLabel dailyRelearningGoalLabel = ComponentFactory.createAccentJLabel("Set the amount of learnt flashcards to revise: ");

        dailyGoalField = ComponentFactory.createStandardJTextField(String.valueOf(DataManager.getCurrentProfile().getDailyGoal()));
        dailyGoalField.getDocument().addDocumentListener(new TextFieldListener(() -> acceptButton.setEnabled(isInputValid())));

        dailyRelearningGoalField = ComponentFactory.createStandardJTextField(String.valueOf(DataManager.getCurrentProfile().getDailyRelearningGoal()));
        dailyRelearningGoalField.getDocument().addDocumentListener(new TextFieldListener(() -> acceptButton.setEnabled(isInputValid())));

        acceptButton.setEnabled(isInputValid());

        parametersPanel.add(dailyGoalLabel, new GBC(0,0).setWeight(0.7,0.2).setFill(GBC.HORIZONTAL).setInsets(10, 20, 0,0));
        parametersPanel.add(dailyGoalField, new GBC(1,0).setWeight(0.3, 0.2).setFill(GBC.HORIZONTAL).setInsets(10, 0, 0,20));
        parametersPanel.add(dailyRelearningGoalLabel, new GBC(0,1).setWeight(0.7,0.2).setFill(GBC.HORIZONTAL).setInsets(0, 20, 10,0));
        parametersPanel.add(dailyRelearningGoalField, new GBC(1,1).setWeight(0.3, 0.2).setFill(GBC.HORIZONTAL).setInsets(0, 0, 10,20));

        titleLabel.setText("Revision preferences.");

        mainPanel.add(titleLabel, new GBC(0, 0).setWeight(1,0.2).setInsets(10,10,5,10));
        mainPanel.add(parametersPanel, new GBC(0, 1).setWeight(1,0.6).setFill(GBC.BOTH).setInsets(5,10,5,10));
        mainPanel.add(buttonPanel, new GBC(0,2).setWeight(1,0.2).setInsets(5,10,20,10));
    }

    private boolean isInputValid() {
        try {
            int dailyGoal = Integer.parseInt(dailyGoalField.getText());
            int dailyLearntRefresher = Integer.parseInt(dailyRelearningGoalField.getText());

            return dailyGoal >= 0 && dailyLearntRefresher >= 0;
        } catch (NumberFormatException e) {
            LOG.warn("Input must be a positive number.");
            return false;
        }
    }

    @Override
    public void handleAccept() {
        DataManager.getCurrentProfile().setDailyGoal(Integer.parseInt(dailyGoalField.getText()));
        DataManager.getCurrentProfile().setDailyRelearningGoal(Integer.parseInt(dailyRelearningGoalField.getText()));
        super.handleAccept();
    }

    @Override
    public DialogMode getDialogMode() {
        return DialogMode.REVISION_PREFERENCES;
    }

}
