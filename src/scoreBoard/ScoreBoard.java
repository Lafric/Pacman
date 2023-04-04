package scoreBoard;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Objects;


/** Handles reads and writes from/to the scoreBoard.xml file */
public final class ScoreBoard {
    /** relative path to the xml file, which stores the scoreboard entries */
    private static final String xmlPath = "/resources/scoreBoard.xml";

    /** String constant which is used to select the root-tag of the scoreboard for the 1-player mode */
    private static final String onePlayer = "singlePlayer";
    /** String constant which is used to select the root-tag of the scoreboard for the 2-player mode */
    private static final String twoPlayers = "multiPlayer";

    /** writes an entry of name and score to the according xml file.
     * <p>
     * writes the entry to the single-player scoreBoard.
     * <p>
     * The new entry is written above the entry with the next lower score.
     * Also deletes entries above rank 10 (not needed anymore).
     * @param playerName string which will be shown in the scoreboard as the players name.
     * @param score points/score which the player has achieved in the game.
     */
    public static void writeEntry(String playerName, int score) {
        //write to single-player scoreboard
        writeEntry(new String[]{playerName}, score);
    }

    /** writes an entry of name and score to the according xml file.
     * <p>
     * writes the entry to the scoreBoard matching the length of the playerNames array.
     * <p>
     * The new entry is written above the entry with the next lower score.
     * Also deletes entries above rank 10 (not needed anymore).
     * @param playerNames strings which will be shown in the scoreboard as the players names.
     * @param score points/score which the player/s has achieved in the game.
     */
    public static void writeEntry(String[] playerNames, int score) {
        //write to the scoreboard matching the count of names.
        int gameMode = playerNames.length;
        String gameModeTag = getGameModeTagName(gameMode);


        Document doc = readXML();


        //does the same as xml.getElementsByTagName("scoreboard").item(0);
        Element root = doc.getDocumentElement();
        //get the root of the according scoreboard
        Element scoreBoardRoot = (Element) root.getElementsByTagName(gameModeTag).item(0);

        Element newEntry = createEntry(doc, playerNames, score);

        insertEntryToScoreboard(scoreBoardRoot, newEntry);

        writeXML(doc);

        //remove entries below rank 10
        int length = getEntryCount(gameMode);
        for(int i = 10; i < length; i++) {
            removeEntry(10, gameMode);
        }
    }

    /**
     * returns the name/s of the player/s at a specified rank in the scoreboard of the given gameMode.
     * @param rank the position of the scoreboard entry (ordered by score).
     * @param gameMode The gameMode ( 1 = 1 player, 2 = 2-players,...) to which the scoreboard belongs.
     * @return the name/s of the player/s at a specified rank in the scoreboard.
     */
    public static String[] readNames(int rank, int gameMode) {
        String gameModeTag = getGameModeTagName(gameMode);

        Document doc = readXML();
        Element root = doc.getDocumentElement();
        //get the root of the according scoreboard
        Element scoreBoardRoot = (Element) root.getElementsByTagName(gameModeTag).item(0);

        NodeList children = scoreBoardRoot.getElementsByTagName("entry");

        //get alle of the names
        NodeList nameNodeList = ((Element) children.item(rank)).getElementsByTagName("name");
        String[] names = new String[nameNodeList.getLength()];
        for(int i = 0; i < nameNodeList.getLength(); i++) {
            names[i] = nameNodeList.item(i).getTextContent();
        }

        return names;
    }

    /**
     * returns the score of the player/s at a specified rank in the scoreboard of the given gameMode.
     * @param rank the position of the scoreboard entry (ordered by score).
     * @param gameMode The gameMode ( 1 = 1 player, 2 = 2-players,...) to which the scoreboard belongs.
     * @return the score of the player at a specified rank in the scoreboard.
     */
    public static Integer readScore(int rank, int gameMode) {
        String gameModeTag = getGameModeTagName(gameMode);

        Document doc = readXML();
        Element root = doc.getDocumentElement();
        //get the root of the according scoreboard
        Element scoreBoardRoot = (Element) root.getElementsByTagName(gameModeTag).item(0);

        NodeList children = scoreBoardRoot.getElementsByTagName("entry");

        return Integer.parseInt(((Element) children.item(rank)).getElementsByTagName("score").item(0).getTextContent());
    }

    /**
     * removes the scoreboard entry at a specified rank/position in the scoreboard of the given gameMode.
     * @param rank the position of the scoreboard entry (ordered by score).
     * @param gameMode The gameMode ( 1 = 1 player, 2 = 2-players,...) to which the scoreboard belongs.
     */
    public static void removeEntry(int rank, int gameMode) {
        String gameModeTag = getGameModeTagName(gameMode);

        Document doc = readXML();
        Element root = doc.getDocumentElement();
        //get the root of the according scoreboard
        Element scoreBoardRoot = (Element) root.getElementsByTagName(gameModeTag).item(0);

        NodeList children = scoreBoardRoot.getElementsByTagName("entry");

        scoreBoardRoot.removeChild(children.item(rank));

        writeXML(doc);
    }

    /** removes all the saved data/the entries in the scoreboard of the give gameMode.
     * @param gameMode The gameMode ( 1 = 1 player, 2 = 2-players,...) to which the scoreboard belongs.
     * */
    public static void clearScoreBoardData(int gameMode) {
        String gameModeTag = getGameModeTagName(gameMode);

        Document doc = readXML();
        Element root = doc.getDocumentElement();
        //get the root of the according scoreboard
        Element scoreBoardRoot = (Element) root.getElementsByTagName(gameModeTag).item(0);

        NodeList children = scoreBoardRoot.getChildNodes();

        int childrenLength = children.getLength();

        for (int i=childrenLength-1; i>=0; i--) {
            scoreBoardRoot.removeChild(children.item(i));
        }

        writeXML(doc);
    }

    /**
     * return the count of entries of the scoreboard matching the gameMode.
     * @param gameMode The gameMode ( 1 = 1 player, 2 = 2-players,...) to which the scoreboard belongs.
     * @return count of entries in the scoreBoard.xml file
     */
    public static int getEntryCount(int gameMode) {
        String gameModeTag = getGameModeTagName(gameMode);

        Document doc = readXML();
        Element root = doc.getDocumentElement();
        //get the root of the according scoreboard
        Element scoreBoardRoot = (Element) root.getElementsByTagName(gameModeTag).item(0);

        NodeList children = scoreBoardRoot.getElementsByTagName("entry");

        return children.getLength();
    }


    /**
     * reads in the according xml file and returns the document.
     * @return entire xml document.
     */
    private static Document readXML() {
        //read xml file, using the DOM parser
        //creates the documentBuilder object
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            //create the documentbuilder in order to parse the file
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            //return documentBuilder.parse(ScoreBoard.class.getClassLoader().getResource(xmlPath).toString());
            return documentBuilder.parse(getCorrectXMLPathString());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * transforms a Document(DOM) to a xml representation and saves it to xmlPath.
     * @param doc DOM representation of the xml file
     */
    private static void writeXML(Document doc) {
        //save whole file again
        try {
            //create Transformer object, which transforms the source tree of DOM into a result tree for xml
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer=
                    transformerFactory.newTransformer();
            //transform source to result (xml)
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(getCorrectXMLPathString());


            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * returns the correct path to the scoreBoard.xml file, depending on if the application is run from the IDE or the jar file
     * @return Path to the scoreBoard.xml file as String.
     */
    private static String getCorrectXMLPathString() {
        //cannot write to the xml in a jar file, therefore a separate xml file is placed in the directory of the jars
        // when running the jar, it is written to this file instead
        if(Objects.requireNonNull(ScoreBoard.class.getResource(xmlPath)).toExternalForm().startsWith("jar")){

            String newPath = Objects.requireNonNull(ScoreBoard.class.getResource(xmlPath)).getPath();

            //remove the last part of the path and correct it for running jar file
            newPath = newPath.substring(0,newPath.lastIndexOf('/'));
            newPath = newPath.substring(0,newPath.lastIndexOf('/'));
            newPath = newPath.substring(0,newPath.lastIndexOf('/')) + xmlPath;

            return newPath;
        } else {
            //result = new StreamResult(ScoreBoard.class.getResource(xmlPath).toExternalForm());
            return Objects.requireNonNull(ScoreBoard.class.getResource(xmlPath)).toExternalForm();
        }
    }

    /**
     * inserts new entries to the scoreBoard.xml file in order (sorted by score).
     * @param root element to which the new Entry is to be added as a child.
     * @param newEntry new element which is to be inserted.
     */
    private static void insertEntryToScoreboard(Element root, Element newEntry) {
        //insert child/entry at the according position (ordered after score)
        NodeList children = root.getElementsByTagName(newEntry.getTagName());
        //NodeList children = root.getChildNodes();


        //new entry is inserted before the next entry, which has a score <= score of newEntry
        int i = 0;
        for(; i<children.getLength(); i++){
            Element child = (Element) children.item(i);
            int childScore = Integer.parseInt(child.getElementsByTagName("score").item(0).getTextContent());

            if(childScore <= Integer.parseInt(newEntry.getElementsByTagName("score").item(0).getTextContent())){
                //insert the new entry before this one
                child.getParentNode().insertBefore(newEntry, child);
                break;
            }
            //System.out.println(children.item(i).getChildNodes().item(1).getTextContent());
        }
        //if the root note does not have children already, or the new entry is the last,
        // we can just append the new entry
        if(i == children.getLength()){
            root.appendChild(newEntry);
        }
    }

    /**
     * create entry and format it with line-wraps.
     * @param doc DOM representation of the xml file.
     * @param playerNames string array, which holds the names of the players.
     * @param score points/score which the player has achieved in the game.
     * @return newly created Element/entry.
     */
    private static Element createEntry(Document doc, String[] playerNames, int score) {
        //create entry and (format it with linewraps)
        //add linewrap
        //root.appendChild(doc.createTextNode("\n"));

        //create new entry
        Element newEntry = doc.createElement("entry");
        //root.appendChild(newEntry);

        //add linewrap
        newEntry.appendChild(doc.createTextNode("\n"));

        //add name tags and names for the players
        for (int i = 0; i < playerNames.length; i++) {
            Element entryName = doc.createElement("name");
            newEntry.appendChild(entryName);
            entryName.appendChild(doc.createTextNode(playerNames[i]));
        }

        //add the score
        Element entryScore = doc.createElement("score");
        newEntry.appendChild(entryScore);
        entryScore.appendChild(doc.createTextNode(Integer.toString(score)));

        //add linewrap
        newEntry.appendChild(doc.createTextNode("\n"));



        return newEntry;
    }

    /**
     * returns the name / the String of the tag which is used to select the scoreboard for the according gameMode.
     * @param gameMode The gameMode ( 1 = 1 player, 2 = 2-players,...) to which the scoreboard belongs.
     * @return the name / the String of the tag which is used to select the scoreboard for the according gameMode.
     * @exception IllegalArgumentException thrown when the selected gameMode is not supported.
     */
    private static String getGameModeTagName(int gameMode) {
        switch (gameMode) {
            case 1 -> {return onePlayer;}
            case 2 -> {return twoPlayers;}
            default -> throw new IllegalArgumentException("The selected gameMode is not supported");
        }
    }
}
