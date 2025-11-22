package com.fa.gui.dialogs;

import com.fa.gui.AppColorPalette;
import com.fa.gui.ComponentFactory;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RoundRectButton;
import com.fa.util.gui.components.RoundRectPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;

public class RevisionMessageDialog extends AbstractDialog {

    public static final String RANDOM = "Let's review some learnt flashcards!";
    public static final String BOXES = "Box revision time!";

    public RevisionMessageDialog(DialogUser dialogUser, String revisionMessage) {
        super(dialogUser);
        setSize(350, 230);
        setLocationRelativeTo(dialogUser.getJComponent());

        JLabel messageTitleLabel = ComponentFactory.createAccentTitleJLabel("Revision Assistant");
        RoundRectButton okButton = ComponentFactory.createStandardAppButton("OK", event -> dispose());

        JLabel messageLabel = ComponentFactory.createAccentJLabel(revisionMessage);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel messagePanel = new RoundRectPanel(new BorderLayout());
        messagePanel.setBackground(AppColorPalette.getSecondaryBackground());
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout(), false);
        buttonPanel.add(okButton, new GBC(0,0).setInsets(5,15,5,15).setIpad(30,0));

        mainPanel.add(messageTitleLabel, new GBC(0, 0).setWeight(1,0.2).setInsets(10,10,5,10));
        mainPanel.add(messagePanel, new GBC(0, 1).setWeight(1,0.6).setFill(GBC.BOTH).setInsets(5,10,5,10));
        mainPanel.add(buttonPanel, new GBC(0,2).setWeight(1,0.2).setInsets(5,10,20,10));
    }

}
