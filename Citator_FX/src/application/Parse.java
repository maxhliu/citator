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

    public static List<Act> parseXML() throws ParserConfigurationException, IOException, SAXException {
        return parseXML("hamlet");
    }

    public static List<Act> parseXML(String playName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("xmlFiles/" + playName + ".xml"));
        Node root = doc.getDocumentElement();

        List<Act> acts = new ArrayList<>();
        List<Scene> scenes = new ArrayList<>();
        List<Line> lines = new ArrayList<>();

        List<Node> nodeActs = findChildNodesByName(root, "ACT");
        int lineNumber = 0;
        for (Node n1 : nodeActs) {
            List<Node> nodeScenes = findChildNodesByName(n1, "SCENE");
            for (Node n2 : nodeScenes) {
                List<Node> nodeLines = findChildNodesByName(n2, "SPEECH", "STAGEDIR");

                for (Node n3 : nodeLines) {
                    if (n3.getNodeName().equals("SPEECH")) {
                        lines.add(new Speech(n3.getFirstChild().getTextContent(),
                                n3.getLastChild().getTextContent(), lineNumber++));
                    } else {
                        lines.add(new StageDirection(n3.getFirstChild().getTextContent(), lineNumber++));
                    }
                }
                scenes.add(new Scene(n2.getFirstChild().getTextContent(), lines));

            }
            acts.add(new Act(n1.getFirstChild().getTextContent(), scenes));
            lineNumber = 0;
        }
        return acts;
    }
}