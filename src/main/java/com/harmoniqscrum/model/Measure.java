package com.harmoniqscrum.model;

import java.util.ArrayList;

public class Measure {
    private ArrayList<Note> notes;
    private String timeSignature;
    
    public Measure() {
        this.notes = new ArrayList<>();
        this.timeSignature = "4/4"; // Default time signature
    }
    
    public Measure(String timeSignature) {
        this.notes = new ArrayList<>();
        this.timeSignature = timeSignature;
    }
    
    public ArrayList<Note> getNotes() {
        return notes;
    }
    
    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }
    
    public String getTimeSignature() {
        return timeSignature;
    }
    
    public void setTimeSignature(String timeSignature) {
        this.timeSignature = timeSignature;
    }
    
    public void addNote(Note note) {
        notes.add(note);
    }
    
    public boolean validate(){
        return true;
    }
}
