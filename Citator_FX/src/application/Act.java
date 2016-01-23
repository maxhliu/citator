package com.company;

/**
 * Created by Max on 16-01-23.
 */
public class Act {
    private String actNumber;
    private String actTitle;
    private Scene scenes[];

    public Act(String actTitle, Scene scenes[]) {
        this.scenes = scenes;
        this.actTitle = actTitle;
        this.actNumber = actTitle.split(" ")[1].replaceAll("\\.", "");
    }

    public String getActNumber() {
        return actNumber;
    }

    public String getActTitle() {
        return actTitle;
    }

    public Scene getScene(int i) {
        return scenes[i];
    }
}
