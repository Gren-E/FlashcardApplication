package com.fa.gui.dialogs;

import com.fa.gui.AppColorPalette;
import com.fa.gui.ComponentFactory;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RoundRectButton;
import com.fa.util.gui.components.RoundRectPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;

public abstract class CreatorDialog extends AbstractDialog {

    protected JPanel parametersPanel;
    protected JPanel buttonPanel;

    protected JLabel titleLabel;

    protected RoundRectButton acceptButton;
    protected RoundRectButton cancelButton;

    public CreatorDialog(DialogUser dialogUser) {
        super(dialogUser);

        parametersPanel = new RoundRectPanel(new GridBagLayout());
        parametersPanel.setBackground(AppColorPalette.getSecondaryBackground());

        titleLabel = ComponentFactory.createAccentTitleJLabel();

        acceptButton = ComponentFactory.createStandardAppButton("Accept", event -> handleAccept());
        acceptButton.setEnabled(false);

        cancelButton = ComponentFactory.createStandardAppButton("Cancel", event -> handleCancel());

        buttonPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout());
        buttonPanel.add(acceptButton, new GBC(0,0).setInsets(5,15,5,5));
        buttonPanel.add(cancelButton, new GBC(1,0).setInsets(5,5,5,15));
    }

    protected void handleAccept() {
        getDialogUser().handleAccept(getDialogMode());
        dispose();
    }

    protected void handleCancel() {
        getDialogUser().handleCancel(getDialogMode());
        dispose();
    }

    protected abstract DialogMode getDialogMode();

}
