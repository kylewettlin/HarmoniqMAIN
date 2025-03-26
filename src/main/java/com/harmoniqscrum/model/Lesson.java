package com.harmoniqscrum.model;

import java.util.List;

/**
 * Represents a musical lesson with objectives, exercises, and associated songs.
 */
public class Lesson {
    /** Unique identifier for the lesson */
    private String lessonId;

    /** Title of the lesson */
    private String title;

    /** Description of what the lesson covers */
    private String description;

    /** List of song IDs related to this lesson */
    private List<String> relatedSongIds;

    /** Difficulty level (e.g., Beginner, Intermediate, Advanced) */
    private String difficulty;

    /** Learning objectives for the lesson */
    private List<String> objectives;

    /** Exercises included in the lesson */
    private List<String> exercises;

    /** Estimated duration of the lesson in minutes */
    private int durationMinutes;

    /** Genre of the lesson (e.g., Rock, Jazz) */
    private String genre;

    /**
     * Constructs a Lesson with all required properties.
     */
    public Lesson(String lessonId, String title, String description, 
                  List<String> relatedSongIds, String difficulty, 
                  List<String> objectives, List<String> exercises, 
                  int durationMinutes, String genre) {
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.relatedSongIds = relatedSongIds;
        this.difficulty = difficulty;
        this.objectives = objectives;
        this.exercises = exercises;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
    }

    /** @return The unique ID of the lesson */
    public String getLessonId() { return lessonId; }

    /** @return The title of the lesson */
    public String getTitle() { return title; }

    /** @return A short description of the lesson */
    public String getDescription() { return description; }

    /** @return List of song IDs related to this lesson */
    public List<String> getRelatedSongIds() { return relatedSongIds; }

    /** @return The difficulty level of the lesson */
    public String getDifficulty() { return difficulty; }

    /** @return The list of learning objectives */
    public List<String> getObjectives() { return objectives; }

    /** @return The list of exercises in the lesson */
    public List<String> getExercises() { return exercises; }

    /** @return Estimated duration in minutes */
    public int getDurationMinutes() { return durationMinutes; }

    /** @return The genre category of the lesson */
    public String getGenre() { return genre; }

    public void setLessonId(String lessonId) { this.lessonId = lessonId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRelatedSongIds(List<String> relatedSongIds) { this.relatedSongIds = relatedSongIds; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setObjectives(List<String> objectives) { this.objectives = objectives; }
    public void setExercises(List<String> exercises) { this.exercises = exercises; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setGenre(String genre) { this.genre = genre; }

    /**
     * View the lesson content (stub for future implementation).
     */
    public void viewLesson(){
        // Display lesson content
    }

    /**
     * Mark the lesson as completed (stub for future implementation).
     */
    public void completeLesson(){
        // Track completion
    }

    /**
     * Begin the lesson (stub for future implementation).
     */
    public void beginLesson(){
        // Start lesson interaction
    }

    /**
     * Assign the lesson to a student (stub for future implementation).
     * @param student The student receiving the lesson
     */
    public void assignLesson(Student student){
        // Assign lesson logic
    }
}
