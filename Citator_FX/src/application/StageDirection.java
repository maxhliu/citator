package com.company;

/**
 * Created by Max on 16-01-22.
 */
public class StageDirection extends Line {
    private String text;
    public StageDirection(String text, int lineNumber) {
        this.text = text;
        this.lineNumber = lineNumber;
    }
    @Override
    public boolean isSpeech() {
        return false;
    }

    public String getText() {
        return text;
    }
}
