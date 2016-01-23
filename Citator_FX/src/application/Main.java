package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 600, 600);
            scene.getStylesheets().add(
                    getClass().getResource("application.css").toExternalForm());

            TextArea textArea = new TextArea();
            textArea.setText("lolloll\njklsdjfjsa\nto bae or not 2 bay");
            textArea.setEditable(false);
            root.setCenter(textArea);
            
            Clipboard cb = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            cb.setContent(content);
            
            textArea.requestFocus();
            
            textArea.setOnKeyReleased((evt) -> {
                if(evt.isShortcutDown() && evt.getCode() == KeyCode.C) {
                    String selected = cb.getContent(DataFormat.PLAIN_TEXT).toString();
                    String edited = "\"" + selected + "\" (6.9.69)";
                    content.putString(edited);
                    content.putHtml("bold: <b>" + edited + "</b>");
                    cb.setContent(content);
                }    
            });
            
            primaryStage.setTitle("Citator");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
