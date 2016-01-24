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
    static int lineNumber = 0;
    //returns true if one of b equals a
    public static boolean stringEquals(String a, String... b) {
        for (String s : b) {
            if (a.equals(s)) {
                return true;
            }
        }
        return false;
    }

    //find child nodes with one of the given names
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
        //parse the xml into a Document object
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("xmlFiles/" + playName + ".xml"));
        Node root = doc.getDocumentElement();

        List<Act> acts = new ArrayList<>();
        List<Node> nodeActs = findChildNodesByName(root, "ACT");

        //navigate through the act nodes, turning them into Act objects
        for (Node act : nodeActs) {
            //start the line number, which resets after each act
            lineNumber = 0;
            //navigate through the scene nodes, turning them into Scene objects
            List<Node> nodeScenes = findChildNodesByName(act, "SCENE");
            List<Scene> scenes = new ArrayList<>();

            for (Node scene : nodeScenes) {
                List<Line> lines = new ArrayList<>();
                List<Node> nodeSegments = findChildNodesByName(scene, "SPEECH", "STAGEDIR");

                //navigate through the speech and stagedir nodes, turning them into Line objects
                for (Node segment : nodeSegments) {
                    //see if it's a speech or stagedir node
                    if (segment.getNodeName().equals("SPEECH")) {
                        String speaker = segment.getFirstChild().getTextContent();
                        NodeList nodeLines = segment.getChildNodes();

                        //go through all the nodes after the speaker node
                        for (int i = 1; i < nodeLines.getLength(); i++) {
                            Node line = nodeLines.item(i);
                            //stagedirs can be hiding inside speeches
                            if (line.getNodeName().equals("STAGEDIR")) {
                                lines.add(new StageDirection(line.getTextContent(), lineNumber++));
                            } else {
                                //they can even be hiding inside lines!
                                String content = line.getTextContent();
                                if (!line.getFirstChild().getNodeName().equals("#text")) {
                                    String direction = line.getFirstChild().getTextContent();
                                    content = content .replaceAll(direction, "[" + direction + "]");
                                }
                                lines.add(new Speech(speaker, content, lineNumber++));
                            }
                        }
                    } else {
                        String text = segment.getTextContent();
                        String[] textLines = text.split("\\r?\\n");
                        //there is the possiblity of multiline stage directions
                        for (String s : textLines) {
                            lines.add(new StageDirection(s, lineNumber++));
                        }
//                        lines.add(new StageDirection(text, lineNumber));
//                        lineNumber += textLines.length;
                    }

                }
                //using the Line objects gathered from the inside for loop, create a Scene object
                scenes.add(new Scene(scene.getFirstChild().getTextContent(), lines, act.getFirstChild().getTextContent()));
            }
            //using the Scene objects gathered from inside the for loops, create an Act object
            acts.add(new Act(act.getFirstChild().getTextContent(), scenes));
        }
        return acts;
    }

    public static void printSpaces(int n) {
        for (;n --> 0;) {
            System.out.print(" ");
        }
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        List <Act> acts = parseXML();
        String lastSpeaker = "";
        for (Act act : acts) {
            for (Scene scene : act.getScenes()) {
                for (Line line : scene.getLines()) {
                    if (line.isSpeech()) {
                        Speech speech = (Speech) line;
                        if (lastSpeaker.equals(speech.getSpeaker())) {
                            printSpaces(15 + 1);
                            System.out.printf("%s%n", speech.getText());
                        } else {
                            System.out.print("\n" + speech.getSpeaker() + ":");
                            printSpaces(15 - speech.getSpeaker().length());
                            System.out.printf("%s%n", speech.getText());
                            lastSpeaker = speech.getSpeaker();
                        }

                    } else {
                        System.out.println("");
                        System.out.printf("\t%s%n", line.getText());
                        System.out.println("");
                    }
                }
            }
        }
    }

}