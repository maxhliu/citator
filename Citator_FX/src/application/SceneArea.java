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

    public int actNum, sceneNum;

    public SceneArea() {
        super();
        setEditable(false);

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
    }

    public void addScene(Scene s) {

        String txt = getText();
        txt += "\n\n";
        txt += "Act " + s.getActNumber() + ", Scene " + s.getSceneNumber();
        txt += s.getTitle();
        setText(txt);
    }

    public void addLine(Line l) {
        String txt = getText();
        txt += "\n";

        if(l.isSpeech()) {
            // TODO: use String.format to keep the line number right-justfied
            Speech speech = (Speech) l;
            txt += speech.getSpeaker();
            txt += "\n" + speech.getText();
            txt += "  " + speech.getLineNumber();
        } else {
            StageDirection stageDirection = (StageDirection) l;
            txt += stageDirection.getText() + "   " + stageDirection.getLineNumber();
        }
        setText(txt);
    }
}
