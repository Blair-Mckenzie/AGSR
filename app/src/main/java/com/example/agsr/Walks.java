package com.example.agsr;

public class Walks {
    private final String title;
    private final int numSteps;

    public Walks(String title, int numSteps){
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
