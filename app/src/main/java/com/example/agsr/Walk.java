package com.example.agsr;

public class Walk {
    private final String title;
    private final int numSteps;

    public Walk(String title, int numSteps){
        this.title = title;
        this.numSteps = numSteps;
    }

    public String getTitle() {
        return title;
    }
    public int getNumSteps() {
        return numSteps;
    }

}
