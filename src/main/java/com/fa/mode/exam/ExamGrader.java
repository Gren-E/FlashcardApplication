package com.fa.mode.exam;

public class ExamGrader {

    public static final int THRESHOLD_FAILED = 0;
    public static final int THRESHOLD_BRONZE = 50;
    public static final int THRESHOLD_SILVER = 70;
    public static final int THRESHOLD_GOLD = 90;

    public static final String GRADE_FAILED = "failed";
    public static final String GRADE_BRONZE = "bronze";
    public static final String GRADE_SILVER = "silver";
    public static final String GRADE_GOLD = "gold";

    private final float percentage;

    public ExamGrader(Exam exam) {
        long correctAnswers = exam.countAnsweredCorrectly();
        long questions = exam.countQuestions();

        percentage = (float) correctAnswers / questions * 100;
    }

    public String calculateGrade() {
        if (percentage >= THRESHOLD_GOLD) {
            return GRADE_GOLD;
        }
        if (percentage >= THRESHOLD_SILVER) {
            return GRADE_SILVER;
        }
        if (percentage >= THRESHOLD_BRONZE) {
            return GRADE_BRONZE;
        }
        return GRADE_FAILED;
    }

    public boolean passed() {
        return percentage > THRESHOLD_FAILED;
    }

    public float getPercentage() {
        return percentage;
    }

}
