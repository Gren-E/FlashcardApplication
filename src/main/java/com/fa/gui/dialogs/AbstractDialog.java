package com.fa.gui.dialogs;

import com.fa.gui.AppColorPalette;
import com.fa.gui.ComponentFactory;
import com.fa.util.listeners.DragMouseAdapter;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.GridBagLayout;

public abstract class AbstractDialog extends JDialog {

    private final DialogUser dialogUser;

    protected final JPanel mainPanel;

    public AbstractDialog(DialogUser dialogUser) {
        setUndecorated(true);
        setAlwaysOnTop(true);

        getContentPane().setBackground(AppColorPalette.getTransparency());
        setBackground(AppColorPalette.getTransparency());

        this.dialogUser = dialogUser;
        dialogUser.openDialog();

        mainPanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout());

        DragMouseAdapter dragMouseAdapter = new DragMouseAdapter(this);
        mainPanel.addMouseListener(dragMouseAdapter);
        mainPanel.addMouseMotionListener(dragMouseAdapter);

        add(mainPanel);
        setVisible(true);
    }

    public DialogUser getDialogUser() {
        return dialogUser;
    }

    @Override
    public void dispose() {
        super.dispose();
        dialogUser.disposeDialog();
    }

}
