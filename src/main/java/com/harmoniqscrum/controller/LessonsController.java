package com.harmoniqscrum.controller;

import com.harmoniqscrum.model.HarmoniqFACADE;
// Import Lesson model when created
// import com.harmoniqscrum.model.Lesson;
import com.harmoniqscrum.model.Song;
import java.util.List;
import java.util.ArrayList;

public class LessonsController {

    private HarmoniqFACADE facade;

    public LessonsController(HarmoniqFACADE facade) {
        this.facade = facade;
    }

    /**
     * Retrieves the list of songs assigned as lessons for the current user.
     * 
     * @return A list of Song objects assigned as lessons.
     */
    public List<Song> getAssignedSongs() { 
        System.out.println("LessonsController: Fetching assigned lesson songs...");
        return facade.getAssignedLessonSongsForCurrentUser(); 
    }
    
    // TODO: Add methods for searching lessons, selecting a lesson, etc.

} 