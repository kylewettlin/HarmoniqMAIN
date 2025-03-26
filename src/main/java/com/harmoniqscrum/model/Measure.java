package com.harmoniqscrum.model;

import java.util.ArrayList;

/**
 * Represents a musical measure containing a list of notes and a time signature.
 */
public class Measure {
    /** List of notes in the measure */
    private ArrayList<Note> notes;

    /** Time signature of the measure (e.g., "4/4", "3/4") */
    private String timeSignature;

    /**
     * Default constructor that initializes with a default time signature of 4/4.
     */
    public Measure() {
        this.notes = new ArrayList<>();
        this.timeSignature = "4/4";
    }

    /**
     * Constructs a measure with a specified time signature.
     * @param timeSignature The time signature (e.g., "3/4", "6/8")
     */
    public Measure(String timeSignature) {
        this.notes = new ArrayList<>();
        this.timeSignature = timeSignature;
    }

    /**
     * Gets the list of notes in the measure.
     * @return ArrayList of Note objects
     */
    public ArrayList<Note> getNotes() {
        return notes;
    }

    /**
     * Sets the list of notes in the measure.
     * @param notes The list of notes to assign
     */
    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    /**
     * Gets the time signature of the measure.
     * @return The time signature as a string (e.g., "4/4")
     */
    public String getTimeSignature() {
        return timeSignature;
    }

    /**
     * Sets the time signature for the measure.
     * @param timeSignature The new time signature to apply
     */
    public void setTimeSignature(String timeSignature) {
        this.timeSignature = timeSignature;
    }

    /**
     * Adds a note to the measure.
     * @param note The note to be added
     */
    public void addNote(Note note) {
        notes.add(note);
    }

    /**
     * Validates the measure.
     * (In a future implementation, this could check that the total duration of notes
     * does not exceed the measure's time signature.)
     * @return true if the measure is valid (placeholder implementation)
     */
    public boolean validate(){
        return true;
    }
}
