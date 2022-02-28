package com.example.agsr;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history_table")
public class History {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String goalTitle;
    private int goalSteps;
    private int currentSteps;

    public History(String date, String goalTitle, int goalSteps, int currentSteps){
        this.date = date;
        this.goalTitle = goalTitle;
        this.goalSteps = goalSteps;
        this.currentSteps = currentSteps;
    }

    public int getCurrentSteps() {
        return currentSteps;
    }

    public String getDate() {
        return date;
    }

    public int getGoalSteps() {
        return goalSteps;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalSteps(int goalSteps) {
        this.goalSteps = goalSteps;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCurrentSteps(int currentSteps) {
        this.currentSteps = currentSteps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
