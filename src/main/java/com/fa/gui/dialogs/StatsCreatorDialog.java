package com.fa.gui.dialogs;

import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.util.gui.GBC;
import com.fa.util.listeners.TextFieldListener;
import org.apache.log4j.Logger;

import javax.swing.JTextField;

public class StatsCreatorDialog extends CreatorDialog {

    private static final Logger LOG = Logger.getLogger(StatsCreatorDialog.class);

    private final JTextField dailyGoalField;

    public StatsCreatorDialog(DialogUser dialogUser) {
        super(dialogUser);
        setSize(300, 200);
        setLocationRelativeTo(dialogUser.getJComponent());

        dailyGoalField = ComponentFactory.createStandardJTextField();
        dailyGoalField.getDocument().addDocumentListener(new TextFieldListener(() -> acceptButton.setEnabled(isInputValid())));

        parametersPanel.add(dailyGoalField, new GBC(0,0).setWeight(1,1).setFill(GBC.HORIZONTAL).setInsets(0, 20, 10,20));

        titleLabel.setText("Set daily goal.");

        mainPanel.add(titleLabel, new GBC(0, 0).setWeight(1,0.2).setInsets(10,10,5,10));
        mainPanel.add(parametersPanel, new GBC(0, 1).setWeight(1,0.6).setFill(GBC.BOTH).setInsets(5,10,5,10));
        mainPanel.add(buttonPanel, new GBC(0,2).setWeight(1,0.2).setInsets(5,10,20,10));
    }

    private boolean isInputValid() {
        try {
            int dailyGoal = Integer.parseInt(dailyGoalField.getText());
            return dailyGoal >= 0;
        } catch (NumberFormatException e) {
            LOG.warn("Input must be a number.");
            return false;
        }
    }

    @Override
    public void handleAccept() {
        DataManager.getCurrentProfile().setDailyGoal(Integer.parseInt(dailyGoalField.getText()));
        super.handleAccept();
    }

    @Override
    public DialogMode getDialogMode() {
        return DialogMode.STATS;
    }

}
