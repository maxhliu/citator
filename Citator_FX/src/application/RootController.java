package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewriley on 16-01-23.
 */
public class RootController {

    private List<Scene> sceneList;
    private int sceneIndex;

    @FXML
    private TextArea textArea;

    @FXML
    private TreeView treeView;

    @FXML
    protected void initialize() {

        // Parse the play into a list of acts
        List<Act> acts = null;
        try {
            acts = Parse.parseXML();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Init the treeView
        TreeItem<String> root = new TreeItem<>("Hamlet"); // TODO: add play name
        for(Act a : acts) {
            TreeItem<String> actItem = new TreeItem<>("Act " + a.getActNumber());
            root.getChildren().add(actItem);
            for(Scene s : a.getScenes()) {
                TreeItem<String> sceneItem = new TreeItem<>("Scene " + s.getSceneNumber());
                actItem.getChildren().add(sceneItem);
            }
        }
        treeView.setRoot(root);
        treeView.setEditable(false);

        //
        sceneList = generateSceneList(acts, sceneList);

        // Start the textview at the first scene
        sceneIndex = 0;
        changeScene(sceneIndex);
    }

    private List<Scene> generateSceneList(List<Act> acts, List<Scene> sceneList) {
        List<Scene> scenes = new ArrayList<>();
        for(Act a : acts) {
            scenes.addAll(a.getScenes());
        }
        return scenes;
    }

    private void nextScene() {
        if(sceneIndex < sceneList.size() - 1) {
            sceneIndex++;
            changeScene(sceneIndex);
        }
    }

    private void lastScene() {
        if(sceneIndex > 1) {
            sceneIndex--;
            changeScene(sceneIndex);
        }
    }

    private void changeScene(int newIndex) {

        Scene nextScene = sceneList.get(newIndex);

        // Set the txt to the scene text
        String txt = "Act " + nextScene.getActNumber() + ", " + nextScene.getTitle() + "\n\n";
        System.out.flush();
        for (Line l : nextScene.getLines()) {
            if (l.isSpeech()) {
                // TODO: use String.format to keep the line number right-justfied
                Speech speech = (Speech) l;
                txt += speech.getSpeaker();
                txt += "\n" + speech.getText();
                txt += "  " + speech.getLineNumber();
            } else {
                StageDirection stageDirection = (StageDirection) l;
                txt += stageDirection.getText() + "   " + stageDirection.getLineNumber();
            }
            txt += "\n";
        }
        textArea.setText(txt);
    }

    public void onKeyReleased(KeyEvent evt) {

        KeyCode kc = evt.getCode();

        if(kc == KeyCode.UP) {
            lastScene();
        }
        if(kc == KeyCode.DOWN) {
            nextScene();
        }
        if (evt.isShortcutDown() && kc == KeyCode.C) {

            // Get the current clipboard content
            Clipboard cb = Clipboard.getSystemClipboard();
            String selected = cb.getContent(DataFormat.PLAIN_TEXT)
                    .toString();

            // Replace the current clipboard content with the edited content
            Scene selectedScene = sceneList.get(sceneIndex);
            String edited = "\"" + selected + "\" (" + selectedScene.getActNumber() + "."
                    + selectedScene.getSceneNumber() + ".TODO - TODO)"; // TODO
            ClipboardContent content = new ClipboardContent();
            content.putString(edited);
            content.putHtml("bold: <b>" + edited + "</b>");
            cb.setContent(content);
        }
    }

//    private List<Act> getTestData() {
//
//        List<Act> acts = new ArrayList<>();
//
//        List<Line> lines = new ArrayList<>();
//        lines.add(new StageDirection("Enter Maximum Lius", 1));
//        lines.add(new Speech("Max", "Wassup Brittany", 2));
//        lines[2] = new Speech("Andrew", "dsajflkjaskl;jdkfl;jak;sjl;fjsal;j;l", 3);
//
//        Scene[] scenes = new Scene[3];
//        scenes[0] = new Scene("First Scene", lines);
//        scenes[0].setActNumber("1");
//        scenes[1] = new Scene("Second Scene", lines);
//        scenes[1].setActNumber("1");
//        scenes[2] = new Scene("Third Scene", lines);
//        scenes[2].setActNumber("2");
//
//        acts.add(new Act("First Act", scenes));
//        acts.add(new Act("Second Act", scenes));
//        acts.add(new Act("Third Act", scenes));
//
//        return acts;
//    }
}
