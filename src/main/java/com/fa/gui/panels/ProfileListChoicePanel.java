package com.fa.gui.panels;

import com.fa.data.Profile;
import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RoundRectButton;

import javax.swing.JPanel;

public class ProfileListChoicePanel extends ListChoicePanel<Profile> {

    public ProfileListChoicePanel(AppWindow window) {
        super(window);
        reloadList();
    }

    @Override
    public JPanel createElementPanel(Profile profile) {
        RoundRectButton chooseProfileButton = ComponentFactory.createStandardAppButton("Open profile",event -> {
            window.getCardLayout().show(window.getContentPanel(), AppWindow.CATEGORY_CHOICE_PANEL);
            DataManager.setCurrentProfile(profile);
            window.getProfileMenuPanel().reloadCategories();
        });
        buttonList.add(chooseProfileButton);

        JPanel profilePanel = createElementLabelPanel(profile.getName());
        profilePanel.add(chooseProfileButton, new GBC(1,0).setInsets(5,10,5,15));

        return profilePanel;
    }

    @Override
    protected Profile[] loadElements() {
        return DataManager.getAllProfiles();
    }

}
