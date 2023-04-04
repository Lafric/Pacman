package level;

import ingameObjects.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Objects;


/**
 * represents a custom level.
 */
public class CustomLevel extends Level{
    /**
     * builds a custom level, according to the structure in the customMaps.xml file.
     * @param name name-tag of the level in the xml file, that is to be created.
     */
    public CustomLevel(String name){

        //Structure of the level
        String lvl = getLevelStructure(name);

        //remove whitespaces
        lvl = lvl.replace(" ","");

        //delete first and last word-wraps
        lvl = lvl.substring(1,lvl.length()-1);

        char[] lvl_array = lvl.toCharArray();
        //System.out.print(lvl_array);

        Tuple size = getLevelSize(lvl_array);

        initializeLevelMatrix(size.x, size.y);

        buildLevel(lvl_array, size.x, size.y);
    }

    /**
     * reads the customMaps.xml file, and returns the structure of the given level as string.
     * @param name name-tag of the level in the xml file.
     * @return structure of the level.
     */
    private String getLevelStructure(String name){
        //read xml file, using the DOM parser
        //creates the documentBuilder object
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            //create the documentbuilder in order to parse the file
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //Document document = documentBuilder.parse(file);
            Document document = documentBuilder.parse(Objects.requireNonNull(getClass().getResource("/resources/customMaps.xml")).toString());
            //search for the first occurrence of name in the document
            return document.getElementsByTagName(name).item(0).getTextContent();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * provides the shape of the level as a tuple (x,y).
     * @param lvl_array structure of the lvl.
     * @return shape of the level.
     */
    private Tuple getLevelSize(char[] lvl_array){

        char current;

        int current_x = 0;
        int max_x = 0;
        int max_y = 1;

        //loop through the array find out level dimensions
        for (char c : lvl_array) {
            current = c;
            //if end of line, increment y
            if ((current == '\n') || (current == '\r')) {
                if ((max_y == 1)) {
                    max_x = current_x;
                }
                // check if shape is recangular
                else if(max_x != current_x) {
                    throw new IllegalArgumentException("levels must be of rectangular shape");
                }

                current_x = 0;
                max_y++;
            } else {
                current_x++;
            }
        }
        return new Tuple(max_x, max_y);
    }

    /**
     * creates the Objects of the level according to the structure given by lvl_array.
     * @param lvl_array represents the structure of the level.
     * @param x width of the level
     * @param y height of the level
     */
    private void buildLevel(char[] lvl_array, int x, int y){
        char current;

        int current_index = 0;


        for (int row=0; row<y; row++){
            for(int col=0; col< x; col++){

                //skip end-of-lines
                if(lvl_array[current_index] == '\n'  || lvl_array[current_index] == '\r'){
                    current_index++;
                    //System.out.println();
                }

                current = lvl_array[current_index];
                //System.out.print(current);
                switch(current){
                    case 'w':
                        addIngameObject(new Wall(col, row));
                        break;
                    case 'e':
                        addIngameObject(new Enemy(col, row, 0));
                        break;
                    case 'p':
                        addIngameObject(new PowerUp(col, row));
                        break;
                    case 'f':
                        enemyRespawnPosition[0] = col;
                        enemyRespawnPosition[1] = row;
                        addIngameObject(new ScorePoint(col, row));
                        break;
                    case '1':
                        player1SpawnPosition[0] = col;
                        player1SpawnPosition[1] = row;
                        addIngameObject(new ScorePoint(col, row));
                        break;
                    case '2':
                        player2SpawnPosition[0] = col;
                        player2SpawnPosition[1] = row;
                        addIngameObject(new ScorePoint(col, row));
                        break;
                    case '.':
                        addIngameObject(new ScorePoint(col, row));
                        break;
                }
                current_index++;
            }
        }

        //choose sprites of the walls based on the neighboured walls
        setWallSprites();
    }

    /**
     * Tuple of 2 integers.
     * @param x width of the level
     * @param y height of the level
     */
    private record Tuple(int x, int y) {
    }
}
