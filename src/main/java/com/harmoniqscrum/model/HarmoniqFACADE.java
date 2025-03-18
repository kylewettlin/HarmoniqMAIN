package com.harmoniqscrum.model;

public class HarmoniqFACADE {
    
    private static HarmoniqFACADE instance;

    private HarmoniqFACADE() {}

    public static HarmoniqFACADE getInstance() {
        if (instance == null) {
            instance = new HarmoniqFACADE();
        }
        return instance;
    }

    public User login(String username, String password) {
        
    }
    
    public List<Song> playSong(Song song) {
        
    }

    public List<Song> searchSongs(String query) {

    }

    public void updateUserSettings(String setting, String value) {

    }

    public void saveSong(Song song) {  

    }

    public void deleteSong(Song song) {

    }

    public void adjustPlaybackSpeed(int speed) {

    }

    public void setMetronomeSpeed(int bpm) {

    }

    public void createComposition(Composition composition) {

    }

    public void createLesson(Lesson lesson, Student student) {

    }

    public List<Lesson> getLessonForUser(User user) {

    }

    public void createSong(String title, String composer, SongGenre genre, int tempo, 
    String keySignature, int numerator, int denominator) {

    }

}
