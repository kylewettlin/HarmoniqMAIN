package com.harmoniqscrum.model;

import java.util.ArrayList;

public class Staff {
    private String clef; 
    private ArrayList<Measure> measures;

    public Staff() {
        this.clef = "treble"; // Default clef
        this.measures = new ArrayList<>();
    }
    
    public Staff(String clef) {
        this.clef = clef;
        this.measures = new ArrayList<>();
    }
    
    public String getClef() {
        return clef;
    }
    
    public void setClef(String clef) {
        this.clef = clef;
    }
    
    public ArrayList<Measure> getMeasures() {
        return measures;
    }
    
    public void setMeasures(ArrayList<Measure> measures) {
        this.measures = measures;
    }
    
    public void addMeasure(Measure measure) {
        measures.add(measure);
    }
    
    public void render(){
        // Rendering logic goes here
    }
}
