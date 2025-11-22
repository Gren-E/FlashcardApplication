package com.fa.gui.dialogs;

import com.fa.AppEnv;
import com.fa.data.IDAllocator;
import com.fa.data.Profile;
import com.fa.gui.ComponentFactory;
import com.fa.io.XMLWriter;
import com.fa.util.listeners.TextFieldListener;
import com.fa.util.gui.GBC;
import org.apache.log4j.Logger;

import javax.swing.JTextField;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProfileCreatorDialog extends CreatorDialog {

    private static final Logger LOG = Logger.getLogger(ProfileCreatorDialog.class);

    private final JTextField profileNameField;

    public ProfileCreatorDialog(DialogUser dialogUser) {
        super(dialogUser);
        setSize(300, 250);
        setLocationRelativeTo(dialogUser.getJComponent());

        profileNameField = ComponentFactory.createStandardJTextField();
        profileNameField.getDocument().addDocumentListener(new TextFieldListener(() -> acceptButton.setEnabled(isInputValid())));

        parametersPanel.add(profileNameField, new GBC(0,0).setWeight(1,1).setFill(GBC.HORIZONTAL).setInsets(10, 20, 10,20));

        titleLabel.setText("Choose profile name.");

        mainPanel.add(titleLabel, new GBC(0, 0).setWeight(1,0.2).setInsets(10,10,5,10));
        mainPanel.add(parametersPanel, new GBC(0, 1).setWeight(1,0.6).setFill(GBC.BOTH).setInsets(5,10,5,10));
        mainPanel.add(buttonPanel, new GBC(0,2).setWeight(1,0.2).setInsets(5,10,20,10));
    }

    private boolean isInputValid() {
        return !"".equals(profileNameField.getText());
    }

    private void saveProfileToXml() {
        if (isInputValid()) {
            Profile profile = new Profile(IDAllocator.getNextProfileID());
            profile.setName(profileNameField.getText());
            profile.setDailyGoal(20);
            profile.setDailyRelearningGoal(3);

            try {
                Files.createDirectory(Path.of(AppEnv.getDataDirectory() + "/Profile_" + profile.getId()));
            } catch (IOException e) {
                LOG.error("Could not create a new profile directory, profile not saved.");
                return;
            }

            XMLWriter.saveProfile(profile);
        }
    }

    @Override
    public void handleAccept() {
        saveProfileToXml();
        super.handleAccept();
    }

    @Override
    public DialogMode getDialogMode() {
        return DialogMode.PROFILE;
    }

}
