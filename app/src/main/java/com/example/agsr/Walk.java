package com.example.agsr;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "walk_table")
public class Walk {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private final String title;
    private final int numSteps;
    private final int currentSteps;

    public Walk(@NonNull String title, int numSteps, int currentSteps) {
        this.title = title;
        this.numSteps = numSteps;
        this.currentSteps = currentSteps;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public int getCurrentSteps() {
        return currentSteps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
