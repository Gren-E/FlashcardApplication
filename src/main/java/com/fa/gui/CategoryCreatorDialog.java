package com.fa.gui;

import com.fa.data.fc.Flashcard;
import com.fa.io.PDFReader;
import com.fa.io.XMLWriter;
import com.fa.util.gui.GBC;
import com.fa.util.gui.WindowUtil;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;

public class CategoryCreatorDialog extends JDialog {

    private JLabel categoryNameLabel;

    private JTextField categoryNameField;
    private JTextField fileNameField;

    private JButton fileBrowseButton;
    private JButton acceptButton;
    private JButton cancelButton;

    private File sourcePdfFile;

    public CategoryCreatorDialog(AppWindow parent) {
        setSize(300, 200);
        setUndecorated(true);
        setLocationRelativeTo(parent);

        categoryNameLabel = new JLabel("Choose category name and source file.");
        categoryNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        categoryNameField = new JTextField();
        categoryNameField.getDocument().addDocumentListener(new CategoryNameListener());

        fileNameField = new JTextField();
        fileNameField.setEditable(false);

        fileBrowseButton = new JButton("Browse");
        fileBrowseButton.addActionListener((ActionEvent event) -> selectSourcePdfFile());

        acceptButton = new JButton("Accept");
        acceptButton.setEnabled(false);
        acceptButton.addActionListener((ActionEvent event) -> saveCategoryToXml());

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((ActionEvent event) -> dispose());

        setLayout(new GridBagLayout());
        add(categoryNameLabel, new GBC(0, 0, 2, 1).setWeight(1, 1).setFill(GBC.HORIZONTAL));
        add(categoryNameField, new GBC(0, 1, 2, 1).setWeight(1, 1).setFill(GBC.HORIZONTAL));
        add(fileNameField, new GBC(0, 2, 2, 1).setWeight(1, 1).setFill(GBC.HORIZONTAL));
        add(fileBrowseButton, new GBC(0, 3, 2, 1));
        add(acceptButton, new GBC(0, 4, 1, 1).setWeight(0.5, 1).setFill(GBC.HORIZONTAL));
        add(cancelButton, new GBC(1, 4, 1, 1).setWeight(0.5, 1).setFill(GBC.HORIZONTAL));
        setVisible(true);
    }

    public void selectSourcePdfFile() {
        sourcePdfFile = WindowUtil.selectFile(this, "pdf");
        fileNameField.setText((sourcePdfFile != null) ? sourcePdfFile.getName() : "");
        acceptButton.setEnabled(isInputValid());
    }

    public boolean isInputValid() {
        return !"".equals(categoryNameField.getText()) && sourcePdfFile != null;
    }

    public void saveCategoryToXml() {
        if (isInputValid()) {
            Flashcard[] flashcards = PDFReader.loadFlashcardsFromPDF(sourcePdfFile, categoryNameField.getText());
            XMLWriter.saveFlashcardsData(flashcards);
            dispose();
        }
    }

    class CategoryNameListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            acceptButton.setEnabled(isInputValid());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            acceptButton.setEnabled(isInputValid());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            acceptButton.setEnabled(isInputValid());
        }

    }

}
