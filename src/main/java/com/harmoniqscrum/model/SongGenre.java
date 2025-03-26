package com.harmoniqscrum.model;

/**
 * Represents supported musical genres within the Harmoniq system.
 * Each genre includes a user-friendly display name and methods
 * for safely converting from input strings (e.g. from JSON or UI).
 * 
 */
public enum SongGenre {
    
    /** Classic rock genre */
    ROCK("Rock"),

    /** Popular music genre */
    POP("Pop"),

    /** Smooth and swing-based jazz genre */
    JAZZ("Jazz"),

    /** Hip-hop and rap music */
    HIP_HOP("Hip-Hop"),

    /** Rhythm and blues music */
    R_AND_B("R&B"),

    /** Country music */
    COUNTRY("Country"),

    /** Classical music compositions */
    CLASSICAL("Classical");

    private final String displayName;

    /**
     * Constructor for each enum value.
     * 
     * @param displayName The name to display
     */
    SongGenre(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the genre.
     * 
     * @return user-friendly name of the genre
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Converts a string into a matching SongGenre.
     * Accepts values like "R&B", "Hip-Hop", "rock", etc.
     * 
     * @param genre The input genre string
     * @return the matching SongGenre enum value
     * @throws IllegalArgumentException if no matching genre is found
     */
    public static SongGenre fromString(String genre) {
        for (SongGenre g : SongGenre.values()) {
            if (g.displayName.equalsIgnoreCase(genre) ||
                g.name().equalsIgnoreCase(
                    genre.replace("-", "_").replace("&", "AND")
                )) {
                return g;
            }
        }
        throw new IllegalArgumentException("Unknown genre: " + genre);
    }

    /**
     * Safe version of fromString. Returns null if input is invalid.
     * 
     * @param genre the input genre string
     * @return the matching SongGenre or null if invalid
     */
    public static SongGenre tryParse(String genre) {
        try {
            return fromString(genre);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
