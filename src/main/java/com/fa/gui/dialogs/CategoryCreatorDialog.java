package com.fa.gui.dialogs;

import com.fa.data.fc.Flashcard;
import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.io.PDFReader;
import com.fa.io.XMLWriter;
import com.fa.util.FileExtensions;
import com.fa.util.gui.GBC;
import com.fa.util.gui.WindowUtil;
import com.fa.util.gui.components.RoundRectButton;
import com.fa.util.listeners.TextFieldListener;

import javax.swing.JTextField;
import java.io.File;

public class CategoryCreatorDialog extends CreatorDialog {

    private final JTextField categoryNameField;
    private final JTextField fileNameField;

    private File sourcePdfFile;

    public CategoryCreatorDialog(DialogUser dialogUser) {
        super(dialogUser);
        setSize(500, 300);
        setLocationRelativeTo(dialogUser.getJComponent());

        categoryNameField = ComponentFactory.createStandardJTextField();
        categoryNameField.getDocument().addDocumentListener(new TextFieldListener(() -> acceptButton.setEnabled(isInputValid())));

        fileNameField = ComponentFactory.createStandardJTextField();
        fileNameField.setEditable(false);

        RoundRectButton fileBrowseButton = ComponentFactory.createStandardAppButton("Browse");
        fileBrowseButton.setActionListener(event -> selectSourcePdfFile());

        parametersPanel.add(categoryNameField, new GBC(0,0).setWeight(1,0.3).setFill(GBC.HORIZONTAL).setInsets(0, 20, 0,20));
        parametersPanel.add(fileNameField, new GBC(0,1).setWeight(1,0.3).setFill(GBC.HORIZONTAL).setInsets(0, 20, 0,20));
        parametersPanel.add(fileBrowseButton, new GBC(0,2).setWeight(1,0.4).setInsets(0, 20, 10,20));

        titleLabel.setText("Choose category name and source file.");

        mainPanel.add(titleLabel, new GBC(0, 0).setWeight(1,0.2).setInsets(10,10,5,10));
        mainPanel.add(parametersPanel, new GBC(0,1).setWeight(1,0.6).setFill(GBC.BOTH).setInsets(5,10,5,10));
        mainPanel.add(buttonPanel, new GBC(0,2).setWeight(1,0.2).setWeight(1,0.2).setInsets(5,10,20,10));
    }

    private void selectSourcePdfFile() {
        sourcePdfFile = WindowUtil.selectFile(this, FileExtensions.PDF.get());
        fileNameField.setText(sourcePdfFile != null ? sourcePdfFile.getName() : "");
        acceptButton.setEnabled(isInputValid());
    }

    private boolean isInputValid() {
        return !"".equals(categoryNameField.getText()) && sourcePdfFile != null;
    }

    private void saveCategoryToXml() {
        if (isInputValid()) {
            Flashcard[] flashcards = PDFReader.loadFlashcardsFromPDF(sourcePdfFile, categoryNameField.getText());
            XMLWriter.saveFlashcardsData(flashcards, DataManager.getCurrentProfile());
            XMLWriter.addNewFlashcardsToBoxes(flashcards, DataManager.getCurrentProfile());
            DataManager.setCurrentProfile(DataManager.getCurrentProfile());
        }
    }

    @Override
    public void handleAccept() {
        saveCategoryToXml();
        super.handleAccept();
    }

    @Override
    public DialogMode getDialogMode() {
        return DialogMode.CATEGORY;
    }

}
