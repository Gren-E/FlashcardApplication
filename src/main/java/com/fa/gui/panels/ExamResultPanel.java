package com.fa.gui.panels;

import com.fa.AppEnv;
import com.fa.gui.AppColorPalette;
import com.fa.gui.ComponentFactory;
import com.fa.mode.exam.Exam;
import com.fa.mode.exam.ExamGrader;
import com.fa.util.gui.ComponentUtil;
import com.fa.util.gui.GBC;
import com.fa.util.gui.ImageUtil;
import com.fa.util.gui.components.EmbellishedStringIcon;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class ExamResultPanel extends JPanel {

    public static final String EXAM_COMPLETED = "Exam completed!";
    public static final String TIME_IS_UP = "Time's up!";

    private final EmbellishedStringIcon gradeIcon;

    private final JPanel stampPanel;
    private final JLabel titleLabel;
    private final JLabel correctAnswersLabel;
    private final JLabel incorrectAnswersLabel;
    private final JLabel scoreLabel;
    private final JLabel timeLabel;

    private final Image embellishmentImage;
    private Image passedStampImage;
    private Image failedStampImage;

    public ExamResultPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel mainPanel = ComponentFactory.createContentRoundRecJPanel(new GridBagLayout(), true);
        mainPanel.setMinimumSize(new Dimension(200,200));

        embellishmentImage = ImageUtil.readImage(new File(AppEnv.getImageDirectory(), "embellishment.png"));

        passedStampImage = ImageUtil.readImage(new File(AppEnv.getImageDirectory(), "passed.png"));
        passedStampImage = ImageUtil.replaceColor(passedStampImage, new Color(0,0,255), AppColorPalette.getSuccessColor());

        failedStampImage = ImageUtil.readImage(new File(AppEnv.getImageDirectory(), "failed.png"));
        failedStampImage = ImageUtil.replaceColor(failedStampImage, new Color(0,0,255), AppColorPalette.getFailColor());

        stampPanel = ComponentUtil.createEmptyPanel(new BorderLayout());

        gradeIcon = ComponentFactory.createEmbellishedStringIcon(embellishmentImage, 250, 250);

        titleLabel = ComponentFactory.createGigaJLabel();

        correctAnswersLabel = ComponentFactory.createTitleJLabel();
        correctAnswersLabel.setForeground(AppColorPalette.getButtonSuccessBackground());

        incorrectAnswersLabel = ComponentFactory.createTitleJLabel();
        incorrectAnswersLabel.setForeground(AppColorPalette.getButtonFailBackground());

        scoreLabel = ComponentFactory.createTitleJLabel();
        timeLabel = ComponentFactory.createTitleJLabel();

        mainPanel.add(gradeIcon, new GBC(0,0).setAnchor(GBC.EAST).setInsets(20,0,0,0));
        mainPanel.add(titleLabel, new GBC(0,1).setInsets(0,200,10,200));
        mainPanel.add(correctAnswersLabel, new GBC(0,2).setAnchor(GBC.CENTER));
        mainPanel.add(incorrectAnswersLabel, new GBC(0,3).setAnchor(GBC.CENTER));
        mainPanel.add(timeLabel, new GBC(0,4).setAnchor(GBC.CENTER).setInsets(0,0,0,0));
        mainPanel.add(scoreLabel, new GBC(0,5).setAnchor(GBC.CENTER).setInsets(0,0,30,0));
        mainPanel.add(stampPanel, new GBC(0,6).setAnchor(GBC.CENTER).setInsets(0,0,50,0));

        add(mainPanel, new GBC(0,0).setWeight(1,1).setFill(GBC.BOTH).setInsets(5));
    }

    public void loadResults(String titleMessage, Exam exam, LocalDateTime finishTime) {
        titleLabel.setText(titleMessage);

        ExamGrader grader = new ExamGrader(exam);

        loadStampPanel(grader.passed());
        generateExamGrade(grader);
        correctAnswersLabel.setText(String.format("Correct answers: %d", exam.countAnsweredCorrectly()));
        incorrectAnswersLabel.setText(String.format("Incorrect answers: %d", exam.countAnsweredIncorrectly()));
        scoreLabel.setText(String.format("Score: %d/%d (%.01f%%)", exam.countAnsweredCorrectly(), exam.countQuestions(), grader.getPercentage()));
        timeLabel.setText(createFinishTimeMessage(exam, finishTime));
    }

    private void loadStampPanel(boolean passed) {
        stampPanel.removeAll();
        JLabel imagePanel = new JLabel(new ImageIcon(passed ? passedStampImage : failedStampImage));
        stampPanel.add(imagePanel, BorderLayout.CENTER);
    }

    private void generateExamGrade(ExamGrader grader) {
        String percentage = (int) grader.getPercentage() + "%";
        gradeIcon.setText(percentage);
        gradeIcon.setIcon(embellishmentImage);
        gradeIcon.setTextColor(getForeground());

        final String grade = grader.calculateGrade();
        switch (grade) {
            case ExamGrader.GRADE_FAILED:
                gradeIcon.setTextColor(AppColorPalette.getFailColor());
                gradeIcon.setIcon(null);
                break;
            case ExamGrader.GRADE_BRONZE:
                gradeIcon.setForeground(AppColorPalette.getBronzeColor());
                break;
            case ExamGrader.GRADE_SILVER:
                gradeIcon.setForeground(AppColorPalette.getSilverColor());
                break;
            case ExamGrader.GRADE_GOLD:
                gradeIcon.setForeground(AppColorPalette.getGoldColor());
                break;
        }
    }

    private String createFinishTimeMessage(Exam exam, LocalDateTime finishTime) {
        Duration examTime = Duration.between(exam.getStartTimestamp(), finishTime);
        int minutes = (int) examTime.getSeconds() / 60;
        int seconds = (int) examTime.getSeconds() % 60;
        String time = (minutes == 1 ? "1 minute " : minutes + " minutes ") + (seconds == 1 ? "1 second" : seconds + " seconds");
        return String.format("Exam finished in: %s", time);
    }

}
