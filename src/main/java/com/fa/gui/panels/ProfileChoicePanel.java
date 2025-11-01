package com.fa.gui.panels;

import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.gui.dialogs.DialogMode;
import com.fa.gui.dialogs.DialogUser;
import com.fa.gui.dialogs.ProfileCreatorDialog;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RoundRectButton;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;

public class ProfileChoicePanel extends JPanel implements DialogUser {

    private final ProfileListChoicePanel profileListPanel;

    private final RoundRectButton newProfileButton;

    private boolean isDialogOpen;

    public ProfileChoicePanel(AppWindow parent) {
        setOpaque(false);

        JLabel chooseProfileLabel = ComponentFactory.createTitleJLabel("Choose profile");

        profileListPanel = new ProfileListChoicePanel(parent);
        profileListPanel.reloadList();

        newProfileButton = ComponentFactory.createStandardAppButton("New profile", event -> {
            if (!isDialogOpen) {
                new ProfileCreatorDialog(this);
            }
        });

        setLayout(new GridBagLayout());
        add(chooseProfileLabel, new GBC(0,0).setInsets(20));
        add(profileListPanel, new GBC(0,1));
        add(newProfileButton, new GBC(0,2).setInsets(10));
    }

    @Override
    public void openDialog() {
        isDialogOpen = true;
        newProfileButton.setEnabled(false);
        profileListPanel.setEnabled(false);
    }

    @Override
    public void disposeDialog() {
        isDialogOpen = false;
        newProfileButton.setEnabled(true);
        profileListPanel.setEnabled(true);
    }

    @Override
    public JComponent getJComponent() {
        return this;
    }

    @Override
    public void handleAccept(DialogMode mode) {
        profileListPanel.reloadList();
    }

}
