package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewriley on 16-01-23.
 */
public class ReaderController {

    // The txt of the entire scene (for line-num searching)
    private String sceneTxt;

    private List<Scene> sceneList;
    private int sceneIndex;

    @FXML
    private Label playNameText;

    @FXML
    private TextArea textArea;

    @FXML
    private TreeView treeView;

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
        for (Act a : acts) {
            String actName = String.format("Act %3s", a.getActNumber());
            TreeItem<String> actItem = new TreeItem<>(actName);
            root.getChildren().add(actItem);
            for (Scene s : a.getScenes()) {
                String sceneName = String.format("Scene %3s", s.getSceneNumber());
                TreeItem<String> sceneItem = new TreeItem<>(sceneName);
                actItem.getChildren().add(sceneItem);
            }
        }

        treeView.setRoot(root);
        treeView.setEditable(false);
        VBox.setVgrow(treeView, Priority.ALWAYS);
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("selected changed!");
            TreeItem<String> newItem = (TreeItem<String>) newVal;
            String leafTxt = newItem.getValue();
            String parentTxt = newItem.getParent().getValue();

            // Iterate through the tree structure until the new scene is found
            int count = 0;
            for (TreeItem<String> act : root.getChildren()) {
                for (TreeItem<String> scene : act.getChildren()) {
                    if (scene.getValue().equals(leafTxt) && scene.getParent().getValue().equals(parentTxt)) {
                        changeScene(count);
                        break;
                    }
                    count++;
                }
            }
        });

        sceneList = generateSceneList(acts, sceneList);

        // Start the treeview and textview at the first scene
        root.getChildren().get(0).setExpanded(true);
        treeView.getSelectionModel().select(root.getChildren().get(0).getChildren().get(0));
        sceneIndex = 0;
        changeScene(sceneIndex);
    }

    private List<Scene> generateSceneList(List<Act> acts, List<Scene> sceneList) {
        List<Scene> scenes = new ArrayList<>();
        for (Act a : acts) {
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

    public static String getSpaces(int n) {
        StringBuilder s = new StringBuilder();
        for (;n --> 0;) {
            s.append(" ");
        }
        return s.toString();
    }

    private void changeScene(int newIndex) {

        sceneIndex = newIndex;
        Scene nextScene = sceneList.get(newIndex);

        // Set the txt to the scene text
        String lastSpeaker = "";
        StringBuilder txt = new StringBuilder().append("Act ").append(nextScene.getActNumber()).append(", ").append(nextScene.getTitle()).append("\n\n");
        System.out.flush();
        for (Line l : nextScene.getLines()) {

            if (l.isSpeech()) {
                Speech speech = (Speech) l;
                if (lastSpeaker.equals(speech.getSpeaker())) {
                    txt.append(getSpaces(15 + 1))
                            .append(speech.getText())
                            .append("\n");
                } else {
                    txt.append("\n").append(speech.getSpeaker())
                            .append(":")
                            .append(getSpaces(15 - speech.getSpeaker().length()))
                            .append(speech.getText())
                            .append("\n");
                    lastSpeaker = speech.getSpeaker();
                }

            } else {
                txt.append("\n").append(getSpaces(4)).append(l.getText()).append("\n");
            }
        }
        textArea.setText(txt.toString());
    }

    public void onKeyReleased(KeyEvent evt) {

        KeyCode kc = evt.getCode();

        if (evt.isShortcutDown() && kc == KeyCode.C) {

            // Get the current clipboard content
            Clipboard cb = Clipboard.getSystemClipboard();
            String selected = cb.getContent(DataFormat.PLAIN_TEXT)
                    .toString();
            selected = selected.trim();

            // Replace the current clipboard content with the edited content
            Scene selectedScene = sceneList.get(sceneIndex);
            int[] lineNumbers = getLineBoundsHack(selected);
            String lineNumTxt = "";
            if(lineNumbers[0] == lineNumbers[1]) {
                lineNumTxt = lineNumbers[0] + "";
            } else {
                lineNumTxt = lineNumbers[0] + "-" + lineNumbers[1];
            }

            // Edit the selected string: add slashes for line #s, and remove extravagant white spaces
            selected = selected.replace("\n", " \\ "); // replace new-lines with backslashes
            selected = selected.replaceAll("\\s+"," "); // remove consecutive spaces

            String edited = "\"" + selected + "\" (" + selectedScene.getActNumber() + "."
                    + selectedScene.getSceneNumber() + "." + lineNumTxt + ")"; // TODO
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

    // Based on the occurence of new-line characters
    private int[] getLineBoundsHack(String searchTxt) {

        String fullTxt = textArea.getText();

        // Remove consecutive newlines from the fulltext
        String slimFull = fullTxt.replaceAll("\n+", "\n");

        int index = fullTxt.indexOf(searchTxt);

        // Get the # of new-line characters until the index
        String toSearchTxt = slimFull.substring(0, index);
        int startLine = numOccurrences(toSearchTxt);
        int endLine = startLine + numOccurrences(searchTxt);
        int[] bounds = {startLine, endLine};
        return bounds;
    }

    private int numOccurrences(String s) {
        // -1 is to prevent the trailing empties from being dropped
        return s.split("\n", -1).length - 1;
    }
}
