package com.harmoniqscrum.model;

import java.util.ArrayList;

public class Composition {
    private ArrayList<Measure> measures;
    private ArrayList<Staff> staves;
    private boolean compositionPrivacy;
    private ArrayList<Chords> chords;
    private ArrayList<String> annotations;
    
    public Composition() {
        this.measures = new ArrayList<>();
        this.staves = new ArrayList<>();
        this.compositionPrivacy = false; // Default to public
        this.chords = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }
    
    public ArrayList<Measure> getMeasures() {
        return measures;
    }
    
    public void setMeasures(ArrayList<Measure> measures) {
        this.measures = measures;
    }
    
    public ArrayList<Staff> getStaves() {
        return staves;
    }
    
    public void setStaves(ArrayList<Staff> staves) {
        this.staves = staves;
    }
    
    public boolean isCompositionPrivacy() {
        return compositionPrivacy;
    }
    
    public void setCompositionPrivacy(boolean compositionPrivacy) {
        this.compositionPrivacy = compositionPrivacy;
    }
    
    public ArrayList<Chords> getChords() {
        return chords;
    }
    
    public void setChords(ArrayList<Chords> chords) {
        this.chords = chords;
    }
    
    public ArrayList<String> getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(ArrayList<String> annotations) {
        this.annotations = annotations;
    }

    public void save(){
        // Save implementation
    }
    
    public void export(String format){
        // Export implementation
    }
    
    public void addAnnotations(String note){
        annotations.add(note);
    }
    
    public void removeAnnotation(String note){
        annotations.remove(note);
    }
}
