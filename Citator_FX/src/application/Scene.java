package application;

/**
 * Created by Max on 16-01-22.
 */
public class Scene {
    private Line lines[];
    private String sceneNumber;
    private String title;

    public Scene(String title, Line[] lines) {
        this.title = title;
        sceneNumber = title.split(" ")[1].replaceAll("\\.", "");
        this.lines = lines;
    }

    public Line getLine(int i) {
        return lines[i];
    }

    public String getSceneNumber() {
        return sceneNumber;
    }

    public String getTitle() {
        return title;
    }
}