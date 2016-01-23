package application;

import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;

/**
 * Created by matthewriley on 16-01-23.
 */
public class SceneArea extends TextArea {

    public String actNum, sceneNum;

    public SceneArea(Scene scene) {
        super();

        // Set the copying ability
        // TODO: use Andrew's code later
        setOnKeyReleased((evt) -> {

        });

        // Size to the contents
        setMinHeight(1000);
        //autosize();
    }
}
