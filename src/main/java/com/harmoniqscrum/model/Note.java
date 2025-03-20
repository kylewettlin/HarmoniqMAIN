package com.harmoniqscrum.model;

public class Note {
    private String pitch;
    private int string;
    private int fret;
    private double duration;
    private int volume;

    private String[] notes = {"a","a#","b","c","c#","d","d#","e","f","f#","g","g#"};
    private String[] tuning = {"e","b","g","d","a","e"};
    private int[] tuningOct = {2, 2, 1, 1, 1, 0};
    private int octave;

    //default constructor (volume should be set to a default amount)
    public Note(int string, int fret, double duration){
        this.string = string;
        this.fret = fret;
        this.pitch = calculatePitch();
        this.duration = duration;
    }
    //constructor for modified volume
    public Note(int string, int fret, double duration, int volume){
        this.string = string;
        this.fret = fret;
        this.pitch = calculatePitch();
        this.duration = duration;
        this.volume = volume;
    }
    //Constructor for rest
    public Note(double duration){
        this.duration = duration;
        this.volume = 0;
    }

    public void play(){

    } 

    //calculates pitch based on string, fret, and tuning
    //Need to add conversion from note name + octave to pitch
    public String calculatePitch(){
        octave = tuningOct[string];
        String stringN = tuning[string];
        int startIndex = 0;
        for (int j = 0; j < notes.length;j++) {
            if (notes[j].equals(stringN)) {
                startIndex =  j;
            }
        }
        int noteTracker = startIndex-1;
        for(int i = 0; i<fret+1; i++){
            noteTracker++;
            if(noteTracker == notes.length){
                octave++;
                noteTracker = 0;
            }
        }
        String pitchDisplay = notes[noteTracker]+", "+octave+" octave";
        return pitchDisplay;
    }

    public String getPitch(){
        return pitch;
    }
    public int getString(){
        return string;
    }
    public int getFret(){
        return fret;
    }
}
