package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by matthewriley on 16-01-23.
 */
public class LibraryController {

    private Map<String, String> playNameToFilename = new HashMap<>();

    @FXML
    private ListView<String> bookList;

    @FXML
    protected void initialize() throws IOException, SAXException, ParserConfigurationException {

        List<String> playNames = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        File dir = new File("xmlFiles");
        File[] bookFiles = dir.listFiles();
        for(File f : bookFiles) {
            // Check the extension for xml
            String fileName = f.getName();
            String[] nameParts = fileName.split(Pattern.quote("."));
            if(nameParts.length == 2 && nameParts[1].equals("xml")) {
                Document doc = builder.parse(f);
                Element root = doc.getDocumentElement();
                assert root.getFirstChild().getNodeName().equals("TITLE");
                String playName = root.getElementsByTagName("PLAYSUBT").item(0).getTextContent();
                playNames.add(playName);
                playNameToFilename.put(playName, nameParts[0]);
            }
        }

        Collections.sort(playNames);
        bookList.setItems(FXCollections.observableArrayList(playNames));
    }

    public void selectBook(ActionEvent evt) throws IOException {
        if(!bookList.getSelectionModel().isEmpty()) {
            String playName = bookList.getSelectionModel().getSelectedItem();
            String fileName = playNameToFilename.get(playName);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("root.fxml"));
            RootController rc = loader.<RootController>getController();
            rc.setPlayName(playName);
            Pane readPane = (Pane) loader.load();
            Main.scene.setRoot(readPane);
        }
    }
}
