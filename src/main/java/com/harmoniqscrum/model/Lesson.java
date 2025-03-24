package com.harmoniqscrum.model;
import java.util.List;

public class Lesson {
    private String lessonId;
    private String title;
    private String description;
    private List<String> relatedSongIds;
    private String difficulty;
    private List<String> objectives;
    private List<String> exercises;
    private int durationMinutes;
    private String genre;

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

    public String getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getRelatedSongIds() { return relatedSongIds; }
    public String getDifficulty() { return difficulty; }
    public List<String> getObjectives() { return objectives; }
    public List<String> getExercises() { return exercises; }
    public int getDurationMinutes() { return durationMinutes; }
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

    public void viewLesson(){
    }
    public void completeLesson(){

    }
    public void beginLesson(){

    }
    public void assignLesson(Student student){

    }

}