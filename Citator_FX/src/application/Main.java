package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    static javafx.scene.Scene scene;

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("library_view.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Hello World");
        scene = new javafx.scene.Scene(root, 600, 600);
        scene.getStylesheets().add(
                getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Citator");
        primaryStage.show();
    }

//    private String loadText()
//            throws SAXException, IOException, ParserConfigurationException {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        File xmlFile = new File("hamlet.xml");
//        Document doc = builder.parse(xmlFile);
//        Element root = doc.getDocumentElement();
//        return parseText(root);
//    }
//
//    private String parseText(Node root) {
//
//        System.out.println(root.getNodeName() + " " + root.getTextContent());
//        NodeList nList = root.getChildNodes();
//        for(int i = 0; i < nList.getLength(); i++) {
//            Node n = nList.item(i);
//            System.out.println(n.getNodeName());
//        }
//
//        return "";
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
