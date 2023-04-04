package level;

import ingameObjects.*;

/**
 * represents a simple level with walls on the top and bottom.
 */
public class SimpleLevel extends Level {
    /**
     * generates a simple level with walls on top and bottom
     */
    public SimpleLevel(){
        //creates a 2-dimensional matrix, which stores objects that are placed on the map
        int y = 10;
        int x = 10;
        /*
        ingameObjects = new LinkedList[y][x];
        for(int row = 0; row < y; row++){
            for(int col = 0; col < x; col++){
                ingameObjects[row][col] = new LinkedList<>();
            }
        }
        */
        initializeLevelMatrix(x, y);

        //generate walls
        //for(int i=0; i < ingameObjects[0].length; i++){
        for(int i=0; i < x; i++){
            //ingameObjects[0][i].add(new Wall(i, 0));
            //ingameObjects[ingameObjects.length-1][i].add(new Wall(i, ingameObjects.length-1));
            addIngameObject(new Wall(i, 0));
            addIngameObject(new Wall(i, y-1));
        }
        //ingameObjects[4][5].add(new PowerUp(5,4));
        //ingameObjects[4][0].add(new ScorePoint(0,4));
        addIngameObject(new PowerUp(5,4));
        addIngameObject(new ScorePoint(0,4));
    }
}
