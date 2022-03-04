package com.example.agsr;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "walk_table")
public class Walk {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final int numSteps;
    private final int currentSteps;

    public Walk( int numSteps, int currentSteps) {
        this.numSteps = numSteps;
        this.currentSteps = currentSteps;
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
