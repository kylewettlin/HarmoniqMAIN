package com.harmoniqscrum.model;

import java.util.ArrayList;

/**
 * Represents a musical staff that holds measures and is associated with a clef.
 */
public class Staff {

    /** The clef of the staff (e.g., "treble", "bass") */
    private String clef;

    /** List of measures on the staff */
    private ArrayList<Measure> measures;

    /**
     * Default constructor that initializes a staff with a treble clef.
     */
    public Staff() {
        this.clef = "treble";
        this.measures = new ArrayList<>();
    }

    /**
     * Constructs a staff with a specified clef.
     * @param clef The clef to use (e.g., "bass", "treble")
     */
    public Staff(String clef) {
        this.clef = clef;
        this.measures = new ArrayList<>();
    }

    /**
     * Gets the clef of the staff.
     * @return The clef (e.g., "treble", "bass")
     */
    public String getClef() {
        return clef;
    }

    /**
     * Sets the clef of the staff.
     * @param clef The clef to set
     */
    public void setClef(String clef) {
        this.clef = clef;
    }

    /**
     * Gets the list of measures on the staff.
     * @return An ArrayList of Measure objects
     */
    public ArrayList<Measure> getMeasures() {
        return measures;
    }

    /**
     * Sets the measures on the staff.
     * @param measures A list of Measure objects
     */
    public void setMeasures(ArrayList<Measure> measures) {
        this.measures = measures;
    }

    /**
     * Adds a single measure to the staff.
     * @param measure The measure to add
     */
    public void addMeasure(Measure measure) {
        measures.add(measure);
    }

    /**
     * Renders the staff visually (implementation placeholder).
     */
    public void render() {
        // Rendering logic goes here
    }
}
