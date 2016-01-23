package application;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
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

            // Configure
            ListView textList = new ListView();
            ObservableList<Line> lines = FXCollections.observableArrayList();
            textList.setItems(lines);

            TextArea ta = configureTextArea();
            root.setCenter(ta);
            String text = loadText();
            ta.setText(text);

            primaryStage.setTitle("Citator");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextArea configureTextArea() {
        TextArea textArea = new TextArea();
        textArea.setText("lolloll\njklsdjfjsa\nto bae or not 2 bay");
        textArea.setEditable(false);

        Clipboard cb = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        cb.setContent(content);

        textArea.requestFocus();

        textArea.setOnKeyReleased((evt) -> {
            if (evt.isShortcutDown() && evt.getCode() == KeyCode.C) {
                String selected = cb.getContent(DataFormat.PLAIN_TEXT)
                        .toString();
                String edited = "\"" + selected + "\" (2.3.23)"; // TODO
                content.putString(edited);
                content.putHtml("bold: <b>" + edited + "</b>");
                cb.setContent(content);
            }
        });
        return textArea;
    }

    private String loadText()
            throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File("hamlet.xml");
        Document doc = builder.parse(xmlFile);
        Element root = doc.getDocumentElement();
        String txt = parseText(root, "");
        return txt;
    }

    private String parseText(Node root, String text) {

        return root.getNodeName() + " " + root.getNodeValue();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
