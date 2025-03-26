package com.harmoniqscrum.model;

import java.util.ArrayList;

/**
 * Represents a full musical composition including measures, staves,
 * chords, and annotations. Also includes export and save functionality.
 */
public class Composition {

    /** List of measures in the composition */
    private ArrayList<Measure> measures;

    /** List of staves used in the composition */
    private ArrayList<Staff> staves;

    /** True if the composition is private, false if public */
    private boolean compositionPrivacy;

    /** List of chords used in the composition */
    private ArrayList<Chords> chords;

    /** Optional annotations such as notes or comments */
    private ArrayList<String> annotations;

    /**
     * Default constructor initializes all lists and sets composition to public.
     */
    public Composition() {
        this.measures = new ArrayList<>();
        this.staves = new ArrayList<>();
        this.compositionPrivacy = false;
        this.chords = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    /** @return List of measures in the composition */
    public ArrayList<Measure> getMeasures() {
        return measures;
    }

    /** @param measures Sets the list of measures */
    public void setMeasures(ArrayList<Measure> measures) {
        this.measures = measures;
    }

    /** @return List of staves in the composition */
    public ArrayList<Staff> getStaves() {
        return staves;
    }

    /** @param staves Sets the list of staves */
    public void setStaves(ArrayList<Staff> staves) {
        this.staves = staves;
    }

    /** @return true if composition is private */
    public boolean isCompositionPrivacy() {
        return compositionPrivacy;
    }

    /** @param compositionPrivacy Set composition to private or public */
    public void setCompositionPrivacy(boolean compositionPrivacy) {
        this.compositionPrivacy = compositionPrivacy;
    }

    /** @return List of chords used in the composition */
    public ArrayList<Chords> getChords() {
        return chords;
    }

    /** @param chords Sets the list of chords */
    public void setChords(ArrayList<Chords> chords) {
        this.chords = chords;
    }

    /** @return List of annotation strings */
    public ArrayList<String> getAnnotations() {
        return annotations;
    }

    /** @param annotations Sets the list of annotations */
    public void setAnnotations(ArrayList<String> annotations) {
        this.annotations = annotations;
    }

    /**
     * Saves the composition data to persistent storage.
     * (Implementation pending)
     */
    public void save() {
        // Save implementation
    }

    /**
     * Exports the composition in a specified format (e.g., PDF, MIDI).
     * @param format The format to export to
     */
    public void export(String format) {
        // Export implementation
    }

    /**
     * Adds a new annotation to the composition.
     * @param note The note or comment to add
     */
    public void addAnnotations(String note) {
        annotations.add(note);
    }

    /**
     * Removes a specific annotation from the composition.
     * @param note The annotation to remove
     */
    public void removeAnnotation(String note) {
        annotations.remove(note);
    }
}
