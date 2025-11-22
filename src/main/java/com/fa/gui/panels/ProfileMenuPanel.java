package com.fa.gui.panels;

import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.gui.dialogs.BoxesCreatorDialog;
import com.fa.gui.dialogs.CategoryCreatorDialog;
import com.fa.gui.dialogs.RevisionPreferencesCreatorDialog;
import com.fa.gui.dialogs.DialogMode;
import com.fa.gui.dialogs.DialogUser;
import com.fa.gui.dialogs.ExamCreatorDialog;
import com.fa.io.DataManager;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RoundRectButton;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;

public class ProfileMenuPanel extends JPanel implements DialogUser {

    private final RoundRectButton newCategoryButton;
    private final RoundRectButton revisionButton;
    private final RoundRectButton examButton;
    private final RoundRectButton changeDailyGoalButton;
    private final RoundRectButton configureBoxes;
    private final RoundRectButton returnToProfileChoiceButton;

    private final CategoryListChoicePanel categoryListPanel;
    private final ProfileStatsPanel profileStatsPanel;

    private boolean isDialogOpen;

    public ProfileMenuPanel(AppWindow parent) {
        setOpaque(false);

        JPanel browseCategoriesPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout(), true);

        profileStatsPanel = new ProfileStatsPanel();
        JPanel statsPanel = ComponentFactory.createContentRoundRecJPanel(new BorderLayout(), true);
        statsPanel.add(profileStatsPanel, BorderLayout.CENTER);

        JLabel browserLabel = ComponentFactory.createTitleJLabel("Browse categories");

        newCategoryButton = ComponentFactory.createStandardAppButton("New category", event -> {
            if (!isDialogOpen) {
                new CategoryCreatorDialog(this);
            }
        });

        categoryListPanel = new CategoryListChoicePanel(parent);

        browseCategoriesPanel.add(browserLabel, new GBC(0,0).setInsets(20));
        browseCategoriesPanel.add(categoryListPanel, new GBC(0,1));
        browseCategoriesPanel.add(newCategoryButton, new GBC(0,2).setInsets(10));

        JPanel revisionPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout(), true);

        JLabel revisionLabel = ComponentFactory.createTitleJLabel("Revise");
        revisionButton = ComponentFactory.createStandardAppButton("Start revision!",event -> {
            parent.getCardLayout().show(parent.getContentPanel(), AppWindow.BOXES_REVISION_PANEL);
            parent.getBoxesRevisionPanel().reload();
        });

        revisionPanel.add(revisionLabel, new GBC(0,0).setInsets(20));
        revisionPanel.add(revisionButton, new GBC(0,1));

        JPanel examPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout(), true);

        JLabel examLabel = ComponentFactory.createTitleJLabel("Exam");
        examButton = ComponentFactory.createStandardAppButton("Start the exam!",event -> {
            if (!isDialogOpen) {
                new ExamCreatorDialog(this, parent);
            }
        });

        examPanel.add(examLabel, new GBC(0,0).setInsets(20));
        examPanel.add(examButton, new GBC(0,1));

        changeDailyGoalButton = ComponentFactory.createStandardAppButton("Revision Preferences",event -> new RevisionPreferencesCreatorDialog(this));
        configureBoxes = ComponentFactory.createStandardAppButton("Configure Boxes", event -> new BoxesCreatorDialog(this));

        returnToProfileChoiceButton = ComponentFactory.createStandardAppButton("Change Profile",
                event -> parent.getCardLayout().show(parent.getContentPanel(), AppWindow.PROFILE_CHOICE_PANEL));

        setLayout(new GridBagLayout());
        add(changeDailyGoalButton, new GBC(1,0).setWeight(0,0).setAnchor(GBC.WEST).setInsets(10,10,10,5));
        add(configureBoxes, new GBC(2,0).setWeight(0,0).setAnchor(GBC.WEST).setInsets(10,5,10,10));
        add(returnToProfileChoiceButton, new GBC(4,0).setWeight(0,0).setAnchor(GBC.EAST).setInsets(10));
        add(browseCategoriesPanel, new GBC(0,0,1,3).setWeight(0.4,1).setFill(GBC.BOTH).setInsets(30,20,50,5));
        add(statsPanel, new GBC(1,1,4,1).setWeight(0.6,0.2).setFill(GBC.BOTH).setInsets(5,5,5,20));
        add(revisionPanel, new GBC(1,2,2,1).setWeight(0.2,0.7).setFill(GBC.BOTH).setInsets(5,5,50,5));
        add(examPanel, new GBC(3,2,2,1).setWeight(0.4,0.7).setFill(GBC.BOTH).setInsets(5, 5, 50, 20));
    }

    public void reloadCategories() {
        categoryListPanel.reloadList();
        categoryListPanel.revalidate();
        profileStatsPanel.reloadStats();
    }

    @Override
    public void openDialog() {
        isDialogOpen = true;
        newCategoryButton.setEnabled(false);
        revisionButton.setEnabled(false);
        examButton.setEnabled(false);
        changeDailyGoalButton.setEnabled(false);
        configureBoxes.setEnabled(false);
        returnToProfileChoiceButton.setEnabled(false);
        categoryListPanel.setEnabled(false);
    }

    @Override
    public void disposeDialog() {
        isDialogOpen = false;
        newCategoryButton.setEnabled(true);
        revisionButton.setEnabled(true);
        examButton.setEnabled(true);
        changeDailyGoalButton.setEnabled(true);
        configureBoxes.setEnabled(true);
        returnToProfileChoiceButton.setEnabled(true);
        categoryListPanel.setEnabled(true);
    }

    @Override
    public JComponent getJComponent() {
        return this;
    }

    @Override
    public void handleAccept(DialogMode mode) {
        if (mode == DialogMode.CATEGORY) {
            DataManager.reloadCurrentProfile();
            reloadCategories();
            return;
        }

        if (mode == DialogMode.REVISION_PREFERENCES) {
            profileStatsPanel.reloadStats();
        }
    }

}
