package com.fa.gui.dialogs;

import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.io.DataManager;
import com.fa.mode.exam.Exam;
import com.fa.mode.exam.ExamBuilder;
import com.fa.util.gui.GBC;
import com.fa.util.listeners.TextFieldListener;
import org.apache.log4j.Logger;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

public class ExamCreatorDialog extends CreatorDialog {

    private static final Logger LOG = Logger.getLogger(ExamCreatorDialog.class);

    private final ExamBuilder examBuilder;

    private final Set<String> examCategories;

    private final AppWindow window;

    private final JPanel categoriesPanel;

    private final JTextField durationField;
    private final JTextField numberOfQuestionsField;

    public ExamCreatorDialog(DialogUser dialogUser, AppWindow window) {
        super(dialogUser);
        setSize(340, 340);
        setLocationRelativeTo(dialogUser.getJComponent());

        this.window = window;

        examBuilder = new ExamBuilder();
        examCategories = new HashSet<>();

        JLabel durationLabel = ComponentFactory.createAccentJLabel("Exam duration:");
        JLabel numberOfQuestionsLabel = ComponentFactory.createAccentJLabel("Number of questions:");

        durationField = ComponentFactory.createStandardJTextField("30");
        durationField.getDocument().addDocumentListener(new TextFieldListener(this::refreshAcceptButton));

        numberOfQuestionsField = ComponentFactory.createStandardJTextField("20");
        numberOfQuestionsField.getDocument().addDocumentListener(new TextFieldListener(this::refreshAcceptButton));

        categoriesPanel = ComponentFactory.createContentRoundRecJPanel();

        parametersPanel.add(durationLabel, new GBC(0,0).setWeight(0.7,0.2).setFill(GBC.HORIZONTAL).setInsets(10, 20, 0,0));
        parametersPanel.add(durationField, new GBC(1,0).setWeight(0.3, 0.2).setFill(GBC.HORIZONTAL).setInsets(0, 0, 0,20));
        parametersPanel.add(numberOfQuestionsLabel, new GBC(0,1).setWeight(0.7,0.2).setFill(GBC.HORIZONTAL).setInsets(0, 20, 0,0));
        parametersPanel.add(numberOfQuestionsField, new GBC(1,1).setWeight(0.3, 0.2).setFill(GBC.HORIZONTAL).setInsets(0, 0, 0,20));
        parametersPanel.add(categoriesPanel, new GBC(0,2,2,1).setWeight(1,0.6).setFill(GBC.BOTH).setInsets(5, 15, 10,15));

        titleLabel.setText("Create a new exam.");

        JScrollPane parametersScrollPane = ComponentFactory.createVerticalJScrollPane(parametersPanel);

        mainPanel.add(titleLabel, new GBC(0,0).setWeight(1,0.2).setInsets(10,10,5,10));
        mainPanel.add(parametersScrollPane, new GBC(0,1).setWeight(1,0.6).setFill(GBC.BOTH).setInsets(5,10,5,10));
        mainPanel.add(buttonPanel, new GBC(0,2).setWeight(1,0.2).setInsets(5,10,20,10));

        loadCategories();
    }

    private void loadCategories() {
        String[] categories = DataManager.getAllCategoriesNames();

        categoriesPanel.removeAll();
        categoriesPanel.setLayout(new GridLayout(categories.length,1));

        for (int i = 0; i < categories.length; i++) {
            final int index = i;
            JPanel categoryPanel = new JPanel(new GridBagLayout());
            categoryPanel.setOpaque(false);
            JLabel categoryNameLabel = ComponentFactory.createStandardJLabel(String.format("%s (%d)", categories[i], DataManager.getAllFlashcardsFromCategory(categories[i]).length));
            JCheckBox categoryCheckBox = new JCheckBox();
            categoryCheckBox.setOpaque(false);
            categoryCheckBox.addActionListener(event -> {
                if (categoryCheckBox.isSelected()) {
                    examCategories.add(categories[index]);
                } else {
                    examCategories.remove(categories[index]);
                }
                refreshAcceptButton();
            });
            categoryPanel.add(categoryNameLabel, new GBC(0,0).setWeight(0.8,1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setInsets(0,5,0,0));
            categoryPanel.add(categoryCheckBox, new GBC(1,0).setWeight(0.2,1).setAnchor(GBC.EAST).setInsets(0,0,0,10));
            categoriesPanel.add(categoryPanel);
        }
    }

    public boolean isInputValid() {
        try {
            int duration = Integer.parseInt(durationField.getText());
            int numberOfQuestions = Integer.parseInt(numberOfQuestionsField.getText());

            if (duration < 1) {
                LOG.warn("Duration must be longer than 1 minute.");
                return false;
            }

            if (numberOfQuestions < 1) {
                LOG.warn("Number of questions must be greater than 1.");
                return false;
            }

            if (numberOfQuestions > examCategories.stream()
                    .map(category -> DataManager.getAllFlashcardsFromCategory(category).length)
                    .reduce(0, Integer::sum)) {
                LOG.warn("Not enough questions in selected categories.");
                return false;
            }
        } catch (NumberFormatException e) {
            LOG.warn("Input must be a number.");
            return false;
        }

        return !examCategories.isEmpty();
    }

    private void refreshAcceptButton() {
        acceptButton.setEnabled(isInputValid());
    }

    @Override
    public void handleAccept() {
        Exam exam = examBuilder.setCategories(examCategories.toArray(new String[0]))
                .setDurationInMinutes(Integer.parseInt(durationField.getText()))
                .setNumberOfQuestions(Integer.parseInt(numberOfQuestionsField.getText()))
                .build();
        window.getCardLayout().show(window.getContentPanel(), AppWindow.EXAM_PANEL);
        window.getExamPanel().loadExam(exam);
        super.handleAccept();
    }

    @Override
    public DialogMode getDialogMode() {
        return DialogMode.EXAM;
    }

}
