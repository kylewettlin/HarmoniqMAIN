package com.harmoniqscrum.model;

import java.util.concurrent.TimeUnit;

public class MusicPlayer {
    
    public static void main(String[] args) {
        System.out.println("Playing Mary Had a Little Lamb...");
        
        try {
            playSong();
        } catch (InterruptedException e) {
            System.out.println("Music playback was interrupted: " + e.getMessage());
        }
    }
    
    public static void playSong() throws InterruptedException {
        Music music = new Music();
        
        // Mary Had a Little Lamb melody
        // E D C D E E E (pause)
        // D D D (pause)
        // E G G (pause)
        // E D C D E E E E D D E D C

        final int NOTE_SPACING = 16; 
        final int PHRASE_BREAK = 12; 

        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("C");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(PHRASE_BREAK);
        
        // Second phrase
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(PHRASE_BREAK);
        
        // Third phrase 
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("G");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("G");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING); 
        
        TimeUnit.MILLISECONDS.sleep(PHRASE_BREAK);
        
        // Fourth phrase (final)
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("C");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("E");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("D");
        TimeUnit.MILLISECONDS.sleep(NOTE_SPACING);
        
        music.playNote("C");
        TimeUnit.MILLISECONDS.sleep(PHRASE_BREAK);
        
        System.out.println("Song finished!");
    }
} 