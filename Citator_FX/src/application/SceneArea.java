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
        setEditable(false);

        // Set the txt to the scene text
        String txt = "Act " + scene.getActNumber() + ", " + scene.getTitle() + "\n\n";
        System.out.flush();
        for (Line l : scene.getLines()) {
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
        setText(txt);

        // Set the copying ability
        // TODO: use Andrew's code later
        setOnKeyReleased((evt) -> {
            if (evt.isShortcutDown() && evt.getCode() == KeyCode.C) {

                // Get the current clipboard content
                Clipboard cb = Clipboard.getSystemClipboard();
                String selected = cb.getContent(DataFormat.PLAIN_TEXT)
                        .toString();

                // Replace the current clipboard content with the edited content
                String edited = "\"" + selected + "\" (" + actNum + "." + sceneNum + ".TODO - TODO)"; // TODO
                ClipboardContent content = new ClipboardContent();
                content.putString(edited);
                content.putHtml("bold: <b>" + edited + "</b>");
                cb.setContent(content);
            }
        });

        // Size to the contents
        setMinHeight(1000);
        //autosize();
    }
}
