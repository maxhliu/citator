package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

            SceneArea ta = new SceneArea();
            ta.setText("aaaa\naaaaaa\naaaaaaa\naaaaaaa");
            ta.actNum = 1;
            ta.sceneNum = 2;
            SceneArea t2 = new SceneArea();
            t2.setText("bbbbb\nbbbbbb\nbbbbb\nbbbbbb");
            t2.actNum = 3;
            t2.sceneNum = 4;
            VBox vBox = new VBox();
            vBox.getChildren().addAll(ta, t2);
            root.setCenter(vBox);

            List<Act> acts = new ArrayList<>();
            configureText(vBox, acts);

            loadText();

            primaryStage.setTitle("Citator");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureText(VBox vBox, List<Act> acts) {
        for(Act a : acts) {
            SceneArea sceneArea = new SceneArea();
            for(Scene s : a.getScenes()) {
                sceneArea.addScene(s);
                for(Line l : s.getLines()) {
                    sceneArea.addLine(l);
                }
            }
            vBox.getChildren().add(sceneArea);
        }
    }

    private String loadText()
            throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File("hamlet.xml");
        Document doc = builder.parse(xmlFile);
        Element root = doc.getDocumentElement();
        return parseText(root);
    }

    private String parseText(Node root) {

        System.out.println(root.getNodeName() + " " + root.getTextContent());
        NodeList nList = root.getChildNodes();
        for(int i = 0; i < nList.getLength(); i++) {
            Node n = nList.item(i);
            System.out.println(n.getNodeName());
        }

        return "";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
