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

    public Note(int string, int fret){
        this.string = string;
        this.fret = fret;
        this.pitch = getPitch();
    }

    public void play(){

    } 

    public String getPitch(){
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
}
