package application;

import java.util.List;

/**
 * Created by Max on 16-01-23.
 */
public class Act {
    private String actNumber;
    private String actTitle;
    private List<Scene> scenes;
//    private Scene scenes[];

    public Act(String actTitle, List<Scene> scenes) {
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

    public List<Scene> getScenes() {
        return scenes;
    }
}
