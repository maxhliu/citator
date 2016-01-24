package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewriley on 16-01-23.
 */
public class ReaderController {

    private List<Scene> sceneList;
    private int sceneIndex;

    @FXML
    private Text playNameText;

    @FXML
    private TextArea textArea;

    @FXML
    private TreeView treeView;

    @FXML
    protected void initialize() {
    }

    public void initViewer(String playName, String fileName) {

        // Parse the play into a list of acts
        List<Act> acts = null;
        try {
            acts = Parse.parseXML(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        playNameText.setText(playName);

        // Init the treeView
        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(true);
        for(Act a : acts) {
            String actName = String.format("Act %5s", a.getActNumber());
            TreeItem<String> actItem = new TreeItem<>(actName);
            root.getChildren().add(actItem);
            for(Scene s : a.getScenes()) {
                String sceneName = String.format("Scene %5s", s.getSceneNumber());
                TreeItem<String> sceneItem = new TreeItem<>(sceneName);
                actItem.getChildren().add(sceneItem);
            }
        }
        treeView.setRoot(root);
        treeView.getSelectionModel().selectFirst();
        treeView.setEditable(false);

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
        String lastSpeacker = null;
        String txt = "Act " + nextScene.getActNumber() + ", " + nextScene.getTitle() + "\n\n";
        System.out.flush();
        for (Line l : nextScene.getLines()) {
            if (l.isSpeech()) {
                // TODO: use String.format to keep the line number right-justfied
                Speech speech = (Speech) l;

                // If the speaker is new, print their name
                String speaker = speech.getSpeaker();
                if(!speaker.equals(lastSpeacker)) {
                    txt += speaker;
                    lastSpeacker = speaker;
                }
                txt += "\n" + speech.getText();
                txt += "  " + speech.getLineNumber();
                txt += "\n";
            } else {
                StageDirection stageDirection = (StageDirection) l;
                txt += stageDirection.getText() + "   " + stageDirection.getLineNumber() + "\n";
            }
            txt += "\n";
        }
        textArea.setText(txt);
    }

    // TODO: allow the user to change the scene using the root view
    public void onKeyPressedTreeView(KeyEvent evt) {
        if(evt.getCode() == KeyCode.ENTER) {
            // Change the selected scene

        }
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

    // Return to the menu
    public void goBack(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("library_view.fxml"));
        Pane libraryPane = loader.load();
        Main.scene.setRoot(libraryPane);
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
