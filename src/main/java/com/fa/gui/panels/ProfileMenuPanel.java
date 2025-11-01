package com.fa.gui.panels;

import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.gui.dialogs.CategoryCreatorDialog;
import com.fa.gui.dialogs.StatsCreatorDialog;
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
    private final RoundRectButton returnToProfileChoiceButton;

    private final CategoryListChoicePanel categoryListPanel;
    private final ProfileStatsPanel profileStatsPanel;

    private boolean isDialogOpen;

    public ProfileMenuPanel(AppWindow parent) {
        setOpaque(false);

        JPanel browseCategoriesPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout());

        profileStatsPanel = new ProfileStatsPanel();
        JPanel statsPanel = ComponentFactory.createContentRoundRecJPanel(new BorderLayout());
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

        JPanel revisionPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout());

        JLabel revisionLabel = ComponentFactory.createTitleJLabel("Revise");
        revisionButton = ComponentFactory.createStandardAppButton("Start revision!",event -> {
            parent.getCardLayout().show(parent.getContentPanel(), AppWindow.BOXES_REVISION_PANEL);
            parent.getBoxesRevisionPanel().reload();
        });

        revisionPanel.add(revisionLabel, new GBC(0,0).setInsets(20));
        revisionPanel.add(revisionButton, new GBC(0,1));

        JPanel examPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout());

        JLabel examLabel = ComponentFactory.createTitleJLabel("Exam");
        examButton = ComponentFactory.createStandardAppButton("Start the exam!",event -> {
            if (!isDialogOpen) {
                new ExamCreatorDialog(this, parent);
            }
        });

        examPanel.add(examLabel, new GBC(0,0).setInsets(20));
        examPanel.add(examButton, new GBC(0,1));

        changeDailyGoalButton = ComponentFactory.createStandardAppButton("Change Daily Goal",event -> new StatsCreatorDialog(this));

        returnToProfileChoiceButton = ComponentFactory.createStandardAppButton("Change Profile",
                event -> parent.getCardLayout().show(parent.getContentPanel(), AppWindow.PROFILE_CHOICE_PANEL));

        setLayout(new GridBagLayout());
        add(changeDailyGoalButton, new GBC(0,0).setAnchor(GBC.WEST).setInsets(10));
        add(returnToProfileChoiceButton, new GBC(1,0).setAnchor(GBC.EAST).setInsets(10));
        add(statsPanel, new GBC(0,1,2,1).setWeight(1,0.2).setFill(GBC.BOTH).setInsets(5,20,5,20));
        add(browseCategoriesPanel, new GBC(0,2,1,2).setWeight(0.3,0.8).setFill(GBC.BOTH).setInsets(5,20,50,5));
        add(revisionPanel, new GBC(1,2).setWeight(0.7,0.8).setFill(GBC.BOTH).setInsets(5,5,5,20));
        add(examPanel, new GBC(1,3).setWeight(0.7,0.8).setFill(GBC.BOTH).setInsets(5, 5, 50, 20));
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

        if (mode == DialogMode.STATS) {
            profileStatsPanel.reloadStats();
        }
    }

}
