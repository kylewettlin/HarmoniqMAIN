package com.harmoniqscrum.model;

public class Note {
    private String pitch;
    private int string;
    private int fret;
    private double duration;
    private int volume;

    private String[] notes = {"a","a#","b","c","c#","d","d#","e","f","f#","g","g#"};
    private String[] tuning = {"e","b","g","d","a","e"};
    private int octave;

    //Note(6, 0)

    public Note(int string, int fret){
        this.string = string;
        this.fret = fret;
        this.pitch = getPitch();
    }

    public void play(){

    } 

    public String getPitch(){
        String stringN = tuning[string];
        int startIndex = 0;
        for (int i = 0; i < notes.length; i++) {
            if (notes[i].equals(stringN)) {
                startIndex =  i; // Return the index if the target is found
            }
        }
        int num = startIndex;
        for(int i = startIndex; i<fret; i++){
            num++;
            if(num > notes.length){
                octave++;
                num = 0;
            }
            //if(bigger than array of notes, octave up and move to beginning)
        }
        return pitch;
    }
}
