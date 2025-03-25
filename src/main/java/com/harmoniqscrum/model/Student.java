package com.harmoniqscrum.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private ArrayList<Lesson> completedLessons;
    private ArrayList<Lesson> assignedLessons;
    
    public Student(String firstName, String lastName, String username, String email, 
                   String password, String theme, String highlightColor) {
        super(firstName, lastName, username, email, password, "student", theme, highlightColor);
        this.completedLessons = new ArrayList<>();
        this.assignedLessons = new ArrayList<>();
    }
    
    public ArrayList<Lesson> getLessonObjects() {
        return completedLessons;
    }
    
    public void setCompletedLessons(ArrayList<Lesson> completedLessons) {
        this.completedLessons = completedLessons;
    }
    
    public ArrayList<Lesson> getAssignedLessons() {
        return assignedLessons;
    }
    
    public void setAssignedLessons(ArrayList<Lesson> assignedLessons) {
        this.assignedLessons = assignedLessons;
    }
    
    public void completeLesson(Lesson lesson) {
        if (assignedLessons.contains(lesson) && !completedLessons.contains(lesson)) {
            completedLessons.add(lesson);
            // Also add to parent class string-based tracking
            addCompletedLesson(lesson.toString());
        }
    }

    public void viewLessons() {
        // Implementation to view lessons
    }
    
    public void trackProgress() {
        // Implementation to track progress
    }
    
    public Integer getStudentGrade() {
        return super.getGrade();
    }
    
    public void setGrade(int grade) {
        super.setGrade(grade);
    }
    
    /**
     * Gets all lessons for the student
     * 
     * @return List containing both assigned and completed lessons
     */
    public List<Lesson> getAllLessons() {
        List<Lesson> allLessons = new ArrayList<>();
        allLessons.addAll(assignedLessons);
        allLessons.addAll(completedLessons);
        return allLessons;
    }
}