package com.harmoniqscrum.model;

import java.util.ArrayList;

public class Teacher extends User {
    private ArrayList<Student> assignedStudents;
    
    public Teacher(String firstName, String lastName, String username, String email, 
                  String password, String theme, String highlightColor) {
        super(firstName, lastName, username, email, password, "teacher", theme, highlightColor);
        this.assignedStudents = new ArrayList<>();
    }
    
    public ArrayList<Student> getStudents() {
        return assignedStudents;
    }
    
    public void setAssignedStudents(ArrayList<Student> assignedStudents) {
        this.assignedStudents = assignedStudents;
    }
    
    public void addStudent(Student student) {
        assignedStudents.add(student);
        // Also add to parent class string-based list for data persistence
        addAssignedStudent(student.getUsername());
    }
    
    public void removeStudent(Student student) {
        assignedStudents.remove(student);
        // Also remove from parent class string-based list
        removeAssignedStudent(student.getUsername());
    }

    public void assignSong(Student student, Song song) {
        // Implementation for assigning a song to a student
    }
    
    public void provideFeedback(Song song, String feedback) {
        // Implementation for providing feedback on a song
    }
    
    /**
     * Creates a new lesson and assigns it to a student
     * 
     * @param lesson The lesson to create
     * @param student The student taking the lesson
     */
    public void createAndAssignLesson(Lesson lesson, Student student) {
        if (assignedStudents.contains(student)) {
            student.getAssignedLessons().add(lesson);
            // Future implementation could save to Lessons.json
        }
    }
}
