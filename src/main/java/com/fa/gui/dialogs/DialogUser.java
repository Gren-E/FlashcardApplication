package com.fa.gui.dialogs;

import javax.swing.JComponent;

public interface DialogUser {

    void openDialog();

    void disposeDialog();

    JComponent getJComponent();

    default void handleAccept(DialogMode mode) {}

    default void handleCancel(DialogMode mode) {}

}
