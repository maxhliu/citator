package application;

import java.util.List;

/**
 * Created by Max on 16-01-22.
 */
public class Scene {
    //    private Line lines[];
    private List<Line> lines;
    private String sceneNumber;
    private String title;
    private String actNumber;

    public Scene(String title, List<Line> lines) {
        this.title = title;
        sceneNumber = title.split(" ")[1].replaceAll("\\.", "");
        this.lines = lines;
    }

    public String getSceneNumber() {
        return sceneNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getActNumber() {
        return actNumber;
    }

    public void setActNumber(String actNumber) {
        this.actNumber = actNumber;
    }

    public List<Line> getLines() {
        return lines;
    }
}
