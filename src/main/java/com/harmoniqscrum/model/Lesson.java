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
     * View the lesson content by displaying all relevant information.
     * @return A formatted string containing lesson details
     */
    public String viewLesson() {
        StringBuilder content = new StringBuilder();
        content.append("Lesson: ").append(title).append("\n");
        content.append("Description: ").append(description).append("\n");
        content.append("Difficulty: ").append(difficulty).append("\n");
        content.append("Genre: ").append(genre).append("\n");
        content.append("Duration: ").append(durationMinutes).append(" minutes\n\n");
        
        content.append("Learning Objectives:\n");
        for (String objective : objectives) {
            content.append("- ").append(objective).append("\n");
        }
        
        content.append("\nExercises:\n");
        for (String exercise : exercises) {
            content.append("- ").append(exercise).append("\n");
        }
        
        if (!relatedSongIds.isEmpty()) {
            content.append("\nRelated Songs:\n");
            for (String songId : relatedSongIds) {
                content.append("- Song ID: ").append(songId).append("\n");
            }
        }
        
        return content.toString();
    }

    /**
     * Mark the lesson as completed for a student.
     * @param student The student completing the lesson
     * @return true if the lesson was successfully marked as completed
     * @throws IllegalArgumentException if student is null
     */
    public boolean completeLesson(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        try {
            // Check if the lesson was assigned to the student
            if (!student.getAssignedLessons().contains(this)) {
                return false;
            }

            // Add to student's completed lessons if not already completed
            if (!student.getLessonObjects().contains(this)) {
                student.completeLesson(this);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Begin the lesson by initializing necessary resources and tracking.
     * @param student The student starting the lesson
     * @return true if the lesson was successfully started
     * @throws IllegalArgumentException if student is null
     */
    public boolean beginLesson(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        try {
            // Verify the lesson is assigned to the student
            if (!student.getAssignedLessons().contains(this)) {
                return false;
            }

            // Check if lesson is already completed
            if (student.getLessonObjects().contains(this)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Assign the lesson to a student.
     * @param student The student receiving the lesson
     * @return true if the lesson was successfully assigned
     * @throws IllegalArgumentException if student is null
     */
    public boolean assignLesson(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        try {
            // Check if lesson is already assigned
            if (student.getAssignedLessons().contains(this)) {
                return false;
            }

            // Add lesson to student's assigned lessons
            student.getAssignedLessons().add(this);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return title + " (" + lessonId + ")";
    }
}
