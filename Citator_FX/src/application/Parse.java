package application;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parse {

    public static boolean stringEquals(String a, String... b) {
        for (String s : b) {
            if (a.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static List<Node> findChildNodesByName(Node root, String... names) {
        List<Node> returnNodes = new ArrayList<>();
        NodeList n = root.getChildNodes();
        for (int i = 0; i < n.getLength(); i++) {
            Node node = n.item(i);
            if (stringEquals(node.getNodeName(), names)) {
                returnNodes.add(node);
            }
        }
        return returnNodes;
    }

    public static List<Node> findChildNodesByName(Node root, String name) {
        List<Node> returnNodes = new ArrayList<>();
        NodeList n = root.getChildNodes();
        for (int i = 0; i < n.getLength(); i++) {
            Node node = n.item(i);
            if (node.getNodeName().equals(name)) {
                returnNodes.add(node);
            }
        }
        return returnNodes;
    }

    public static void parseXML() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("xmlFiles/hamlet.xml"));
        Node root = doc.getDocumentElement();

        List<Act> acts = new ArrayList<>();
        List<Scene> scenes = new ArrayList<>();
        List<Line> lines = new ArrayList<>();

        List<Node> nodeActs = findChildNodesByName(root, "ACT");
        for (Node n : nodeActs) {
            List<Node> nodeScenes = findChildNodesByName(root, "SCENE");

        }
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        parseXML();
    }
}