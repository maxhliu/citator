package application;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewriley on 16-01-23.
 */
public class RootController {

    @FXML
    private VBox vBox;

    @FXML
    protected void initialize() {
        System.out.println("Initialize called");
        configureText(vBox, getTestData());
    }

    private List<Act> getTestData() {

        List<Act> acts = new ArrayList<>();

        Line[] lines = new Line[3];
        lines[0] = new StageDirection("Enter Maximum Lius", 1);
        lines[1] = new Speech("Max", "Wassup Brittany", 2);
        lines[2] = new Speech("Andrew", "dsajflkjaskl;jdkfl;jak;sjl;fjsal;j;l", 3);

        Scene[] scenes = new Scene[3];
        scenes[0] = new Scene("First Scene", lines);
        scenes[0].setActNumber("1");
        scenes[1] = new Scene("Second Scene", lines);
        scenes[1].setActNumber("1");
        scenes[2] = new Scene("Third Scene", lines);
        scenes[2].setActNumber("2");

        acts.add(new Act("First Act", scenes));
        acts.add(new Act("Second Act", scenes));
        acts.add(new Act("Third Act", scenes));

        return acts;
    }

    private void configureText(VBox vBox, List<Act> acts) {
        for(Act a : acts) {
            for(Scene s : a.getScenes()) {
                SceneArea sceneArea = new SceneArea(s);
                sceneArea.actNum = a.getActNumber();
                sceneArea.sceneNum = s.getSceneNumber();
                vBox.getChildren().add(sceneArea);
            }
        }
    }
}
