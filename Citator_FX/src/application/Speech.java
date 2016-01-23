package com.company;

/**
 * Created by Max on 16-01-22.
 */
public class Speech extends Line {
    private String speaker;
    private String text;

    public Speech(String speaker, String text, int lineNumber) {
        this.speaker = speaker;
        this.text = text;
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean isSpeech() {
        return true;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getText() {
        return text;
    }
}
