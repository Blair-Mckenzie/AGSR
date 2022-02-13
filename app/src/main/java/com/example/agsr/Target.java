package com.example.agsr;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "target_table")
public class Target {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "target")
    private String title;
    private int numSteps;
    private boolean isActive;

    public Target(@NonNull String title, int numSteps, boolean isActive){
        this.title = title;
        this.numSteps = numSteps;
        this.isActive = isActive;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
