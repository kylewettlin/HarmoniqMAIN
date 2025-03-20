package com.harmoniqscrum;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class Song {
    //
    private UUID songId;
    private List<String> genres;
    private List<String> lyrics;
    //
	private String title;
    private String composer;
    private int tempo;
	private double rating;
    private String keySignature;
	private TimeSignature timeSignature;
    private int numerator;
    private int denominator;
	public void setSignature(int sig1, int sig2){

    }


    //
        public static class TimeSignature {
            private int numerator;
            private int denominator;
    
            // Constructor
            public TimeSignature(int numerator, int denominator) {
                this.numerator = numerator;
                this.denominator = denominator;
            }
    
            
        }
    
        public Song(UUID songId, String title, String composer, int tempo, 
                   List<String> genres, double rating, String keySignature, 
                   TimeSignature timeSignature, List<String> lyrics) {
            this.songId = songId;
            this.title = title;
            this.composer = composer;
            this.tempo = tempo;
            this.genres = genres;
            this.rating = rating;
            this.keySignature = keySignature;
            this.timeSignature = timeSignature;
            this.lyrics = lyrics;
        }
    
        public UUID getSongId() { return songId; }
        public String getTitle() { return title; }
        public String getComposer() { return composer; }
        public int getTempo() { return tempo; }
        public List<String> getGenres() { return genres; }
        public double getRating() { return rating; }
        public String getKeySignature() { return keySignature; }
        public TimeSignature getTimeSignature() { return timeSignature; }
        public List<String> getLyrics() { return lyrics; }
    
        public void setSongId(UUID songId) { this.songId = songId; }
        public void setTitle(String title) { this.title = title; }
        public void setComposer(String composer) { this.composer = composer; }
        public void setTempo(int tempo) { this.tempo = tempo; }
        public void setGenres(List<String> genres) { this.genres = genres; }
        public void setRating(double rating) { this.rating = rating; }
        public void setKeySignature(String keySignature) { this.keySignature = keySignature; }
        public void setTimeSignature(TimeSignature timeSignature) { this.timeSignature = timeSignature; }
        public void setLyrics(List<String> lyrics) { this.lyrics = lyrics; }
    //
}
