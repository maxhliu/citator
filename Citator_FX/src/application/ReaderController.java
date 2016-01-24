package application;

import com.sun.tools.javac.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
//
//            if (l.isSpeech()) {
//                // TODO: use String.format to keep the line number right-justfied
//                Speech speech = (Speech) l;
//
//                // If the speaker is new, print their name
//                String speaker = speech.getSpeaker();
//                if(!speaker.equals(lastSpeaker)) {
//                    txt += speaker;
//                    lastSpeaker = speaker;
//                }
//                txt += "\n" + speech.getText();
//                txt += "  " + speech.getLineNumber();
//                txt += "\n";
//            } else {
//                StageDirection stageDirection = (StageDirection) l;
//                txt += stageDirection.getText() + "   " + stageDirection.getLineNumber() + "\n";
//            }
//            txt += "\n";

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

            // Replace the current clipboard content with the edited content
            Scene selectedScene = sceneList.get(sceneIndex);
            int[] lineNumbers = getLineBoundsHack(selected);
            String lineNumTxt = "";
            if(lineNumbers[0] == lineNumbers[1]) {
                lineNumTxt = lineNumbers[0] + "";
            } else {
                lineNumTxt = lineNumbers[0] + "-" + lineNumbers[1];
            }
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

    private int[] getLineBoundsHack(String searchTxt) {

        String fullTxt = textArea.getText();

        // Count the # of new-line characters
        int index = fullTxt.indexOf(searchTxt);

        // Get the # of new-line characters until the index
        String toSearchTxt = fullTxt.substring(0, index);
        int startLine = numOccurrences(toSearchTxt);
        int endLine = startLine + numOccurrences(searchTxt);
        int[] bounds = {startLine, endLine};
        return bounds;
    }

    private int numOccurrences(String s) {
        // -1 is to prevent the trailing empties from being dropped
        return s.split("\n", -1).length - 1;
    }

//    // Get the start and end line of the
//    private int[] getLineBounds(String searchText, Scene scene) {
//
//        int firstLine = -1;
//
//        String[] searchWords = searchText.split(" ");
//
//        // Don't bother retrieving the lines for very short quotations
//        if(searchWords.length < 2) {
//            return null;
//        }
//
//        String s = "";
//        //s.regionMatches();
//
//        String firstSearchWord = searchWords[0];
//        for(int i = 0; i < scene.getLines().size(); i++) {
//            String line = scene.getLines().get(i).getText();
//            String[] lineWords = line.split(" ");
//            for(int j = 0; j < lineWords.length; j++) {
//                String lineWord = lineWords[j];
//
//                // Check for the beginning of the first line
//                if(lineWord.equals(firstSearchWord)) {
//                    firstLine = i;
//                    int lastLine = getLastLine(searchWords, 1, lineWords, j, scene.getLines(), i);
//                    int[] lines = {firstLine, lastLine};
//                    return lines;
//                }
//            }
//        }
//
//        // Nothing found
//        return null;
//    }

//    private int getLastLine(String[] searchWords, int searchWordIndex, String[] lineWords,
//                            int lineWordIndex, List<Line> lines, int lineIndex) {
//
//        String searchArea = "";
//
//        // Get the current speaker
//        String originalSpeaker = null;
//        Line firstLine = lines.get(lineIndex);
//        if(firstLine.isSpeech()) {
//            Speech speech = (Speech) firstLine;
//            originalSpeaker = speech.getSpeaker();
//        }
//
//        // Look for the phrase until the end of the speech
//        String currentSpeaker = null;
//        do {
//            Line line = lines.get(lineIndex);
//            if(line.isSpeech()) {
//                Speech speech = (Speech) line;
//                currentSpeaker = speech.getSpeaker();
//                if(currentSpeaker.equals(originalSpeaker)) {
//                    searchArea +=
//                    break;
//                }
//            } else {
//                break;
//            }
//            lineIndex++;
//
//        } while(currentSpeaker.equals(originalSpeaker));
//
//        return lineIndex;
//
//        /*
//        for(int i = searchWordIndex; i < searchWords.length; i++, lineWordIndex++) {
//
//            String searchWord = searchWords[i];
//
//            if(lineWordIndex < lineWords.length) {
//                if(!searchWord.equals(lineWords[lineWordIndex])) {
//                    return -1;
//                }
//            } else {
//                // Move on to the next line
//                return getLastLine(searchWords, i, lineWords, lineWordIndex, lines, lineIndex + 1);
//            }
//        }
//        */
//        return lineIndex;
//    }
}
