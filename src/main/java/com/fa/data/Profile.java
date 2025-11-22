package com.fa.data;

public class Profile {

    private final int id;
    private int dailyGoal;
    private int dailyRelearningGoal;

    private String name;

    public Profile(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDailyGoal(int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public void setDailyRelearningGoal(int dailyRelearningGoal) {
        this.dailyRelearningGoal = dailyRelearningGoal;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public int getDailyRelearningGoal() {
        return dailyRelearningGoal;
    }

}
