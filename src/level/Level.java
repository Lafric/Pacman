package level;
import ingameObjects.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * represents the level/map on which the actual game is played.
 */
public abstract class Level {

    // generic array creation causes warnings
    // see for explanation: https://stackoverflow.com/questions/18581002/how-to-create-a-generic-array
    // see also: https://www.geeksforgeeks.org/array-of-arraylist-in-java/
    //private LinkedList<IngameObject>[][] ingameObjects;
    //thus we use lists instead of an array
    /**
     * stores which objects are on which field of the level
     */
    private ArrayList<ArrayList<LinkedList<IngameObject>>> ingameObjects;
    /** list to keep track of the enemies in a level */
    private final LinkedList<Enemy> enemyList = new LinkedList<>();
    // enemy respawn position, default: (1, 1)
    /** determines where the enemies are created again after, they were eaten */
    protected final int[] enemyRespawnPosition = {1, 1};
    /** determines where the HungryBall object for player1 is created in the level */
    protected final int[] player1SpawnPosition = {1, 1};
    /** determines where the HungryBall object for player2 is created in the level */
    protected final int[] player2SpawnPosition = {2, 1};

    /** counts the eatable objects in the level (excluding enemies including powerUps */
    protected int eatableCounter = 0;

    /**
     * removes an Eatable from the level.
     * <p>
     * if the eatable is of type Enemy, the enemy is removed from the enemyList.
     * @param e Eatable that is to be removed.
     */
    public synchronized void deleteEatable(Eatable e){
        //every eatable should be an IngameObject -> cast is safe
        //boolean removed = ingameObjects[e.getPosition_y()][e.getPosition_x()].remove((IngameObject)e);
        boolean removed = ingameObjects.get(e.getPosition_y()).get(e.getPosition_x()).remove((IngameObject)e);
        if(removed){
            if(e instanceof Enemy){
                enemyList.remove(e);
            }
            else {
                eatableCounter--;
            }
        }
    }


    /**
     * adds an IngameObject to the internal level matrix.
     * <p>
     * If the IngameObject is of type Enemy, it is added to the enemyList.
     * @param o Ingameobject that is to be added to the level Matrix.
     */
    public synchronized void addIngameObject(IngameObject o){
        ///boolean added = ingameObjects[o.getPosition_y()][o.getPosition_x()].add(o);
        boolean added = ingameObjects.get(o.getPosition_y()).get(o.getPosition_x()).add(o);
        if(added) {
            if(o instanceof Enemy){
                enemyList.add((Enemy) o);
            } else if (o instanceof Eatable) {
                eatableCounter++;
            }
        }
    }
    /**
     * removes an IngameObject from the level.
     * <p>
     * if the IngameObject is of type Enemy, the enemy is removed from the enemyList.
     * @param o object that is to be deleted.
     */
    public synchronized void deleteIngameObject(IngameObject o){
        //boolean removed = ingameObjects[o.getPosition_y()][o.getPosition_x()].remove(o);
        boolean removed = ingameObjects.get(o.getPosition_y()).get(o.getPosition_x()).remove(o);
        if(removed) {
            if(o instanceof Enemy){
                enemyList.remove(o);
            } else if (o instanceof Eatable) {
                eatableCounter--;
            }
        }
    }

    //not used anymore
    /*
     * Returns a deep copy of the internal matrix, in which the objects are placed.
     * @return level-sized matrix that contains the Objects in the level.
     */
    /*
    public List<List<List<IngameObject>>> getIngameObjectsDeepCopy() {
        //System.out.println(Thread.currentThread());
        //clone only creates a shallow copy
        //return ingameObjects.clone();

        List<List<List<IngameObject>>> copy = new ArrayList<>();


        //one thread might do this iteration (e.g. EDT Thread),
        //while other modifies the list -> concurrentmodificationException
        //-> synchronized
        synchronized (this) {
            for (int y = 0; y < ingameObjects.size(); y++) {
                copy.add(new ArrayList<>());
                for (int x = 0; x < ingameObjects.get(0).size(); x++) {
                    copy.get(y).add(new LinkedList<>());
                    for (IngameObject o : ingameObjects.get(y).get(x)) {
                        if (ingameObjects.get(y).get(x) != null) {
                            copy.get(y).get(x).add(o.copy());
                        }

                    }
                }
            }
        }
        return copy;
    }
     */

    /**
     * Returns a shallow copy of the internal matrix, in which the objects are placed.
     * @return level-sized matrix that contains the Objects in the level.
     */
    @SuppressWarnings("unchecked")
    public List<List<List<IngameObject>>> getIngameObjects() {
        //clone only creates a shallow copy
        //warning is supressed here, since clone should return an object of the same type -> casting should be safe here
        return (List<List<List<IngameObject>>>) ingameObjects.clone();
    }

    /**
     * returns the number of columns in the level.
     * @return number of columns.
     */
    public int getWidth(){
        return ingameObjects.get(0).size();
    }

    /**
     * returns the number of rows in the level.
     * @return number of rows.
     */
    public int getHeight(){
        return ingameObjects.size();
    }
    /**
     *
     * @param x x-coordinate of the level.
     * @param y y-coordinate of the level.
     * @return ingameObjects on position x,y in the level.
     */
    @SuppressWarnings("unchecked")
    public List<IngameObject> getIngameObjectsOnPosition(int x, int y) {
        //return a shallow copy of the list
        //return (LinkedList<IngameObject>) ingameObjects.[y][x].clone();
        //return ingameObjects.get(y).get(x);
        //warning is supressed here, since clone should return an object of the same type -> casting should be safe here
        return (List<IngameObject>) ingameObjects.get(y).get(x).clone();
    }
    /*
    /**
     *
     * @param x x-coordinate of the level.
     * @param y y-coordinate of the level.
     * @return copy of ingameObjects on position x,y in the level.
     */
    /*
    public List<IngameObject> getIngameObjectsOnPosition(int x, int y) {
        List<IngameObject> copy = new LinkedList<>();
        for(IngameObject o: ingameObjects[y][x]){
            copy.add(o.copy());
        }
        return copy;
    }
    */


    /**
     * chooses sprites of the walls based on the neighboured walls
     */
    protected void setWallSprites(){
        //go through the level matrix and choose sprites of wall-blocks accordingly
        for(int row = 0; row < ingameObjects.size(); row++){
            for(int col = 0; col< ingameObjects.get(0).size(); col++){
                //IngameObject current = ingameObjects[row][col];
                List<IngameObject> current = ingameObjects.get(row).get(col);

                if(containsWall(current)){

                    boolean isBlockNorth = false;
                    boolean isBlockSouth = false;
                    boolean isBlockWest = false;
                    boolean isBlockEast = false;
                    boolean isBlockNorthWest = false;
                    boolean isBlockNorthEast = false;
                    boolean isBlockSouthWest = false;
                    boolean isBlockSouthEast = false;

                    /*
                    isBlockNorth = ingameObjects[row-1][col] instanceof Wall;
                    boolean isBlockSouth = ingameObjects[row+1][col] instanceof Wall;
                    boolean isBlockWest = ingameObjects[row][col-1] instanceof Wall;
                    boolean isBlockEast = ingameObjects[row][col+1] instanceof Wall;
                    boolean isBlockNorthWest = ingameObjects[row-1][col-1] instanceof Wall;
                    boolean isBlockNorthEast = ingameObjects[row-1][col+1] instanceof Wall;
                    boolean isBlockSouthWest = ingameObjects[row+1][col-1] instanceof Wall;
                    boolean isBlockSouthEast = ingameObjects[row+1][col+1] instanceof Wall;
                    */


                    if(row > 0){
                        isBlockNorth = containsWall(ingameObjects.get(row-1).get(col));
                        if(col > 0) {
                            isBlockNorthWest = containsWall(ingameObjects.get(row-1).get(col-1));
                        }
                        if(col+1 < ingameObjects.get(0).size()) {
                            isBlockNorthEast = containsWall(ingameObjects.get(row-1).get(col+1));
                        }
                    }
                    if(row+1 < ingameObjects.size()){
                        isBlockSouth = containsWall(ingameObjects.get(row+1).get(col));
                        if(col > 0) {
                            isBlockSouthWest = containsWall(ingameObjects.get(row+1).get(col-1));
                        }
                        if(col+1 < ingameObjects.get(0).size()) {
                            isBlockSouthEast = containsWall(ingameObjects.get(row+1).get(col+1));
                        }
                    }
                    if(col > 0){
                       isBlockWest = containsWall(ingameObjects.get(row).get(col-1));
                    }
                    if(col+1 < ingameObjects.get(0).size()){
                        isBlockEast = containsWall(ingameObjects.get(row).get(col+1));
                    }

                    //call the Method in Wall class to set sprite based on this data
                    for(IngameObject o : current){
                        if(o instanceof Wall){
                            ((Wall) o).setSprite(isBlockNorth, isBlockSouth, isBlockWest, isBlockEast,
                                    isBlockNorthWest, isBlockNorthEast, isBlockSouthWest, isBlockSouthEast);
                        }
                    }
                }
            }
        }
    }

    /**
     * checks if a Wall is on the field, which is represented by current.
     * @param current contains the Objects, that are placed on a field in the level.
     * @return true if a wall is placed on the given field.
     */
    public static boolean containsWall(List<IngameObject> current){
        for(IngameObject o : current){
            if(o instanceof Wall){
                return true;
            }
        }
        return false;
    }
    /**
     * checks if an enemy is on the field, which is represented by current.
     * @param current contains the Objects, that are placed on a field in the level.
     * @return true if an enemy is placed on the given field.
     */
    public static boolean containsEnemy(List<IngameObject> current){
        for(IngameObject o : current){
            if(o instanceof Enemy){
                return true;
            }
        }
        return false;
    }

    /**
     * checks if a PowerUp is on the field, which is represented by current.
     * @param current contains the Objects, that are placed on a field in the level.
     * @return true if a powerUp is placed on the given field.
     */
    public static boolean containsPowerUp(List<IngameObject> current){
        for(IngameObject o : current){
            if(o instanceof PowerUp){
                return true;
            }
        }
        return false;
    }
    /**
     * checks if a fruit is on the field, which is represented by current.
     * @param current contains the Objects, that are placed on a field in the level.
     * @return true if a fruit is placed on the given field.
     */
    public static boolean containsFruit(List<IngameObject> current){
        for(IngameObject o : current){
            if(o instanceof Fruit){
                return true;
            }
        }
        return false;
    }
    /**
     * checks if a scorepoint is on the field, which is represented by current.
     * @param current contains the Objects, that are placed on a field in the level.
     * @return true if a scorpoint is placed on the given field.
     */
    public static boolean containsScorePoint(List<IngameObject> current){
        for(IngameObject o : current){
            if(o instanceof ScorePoint){
                return true;
            }
        }
        return false;
    }
    /**
     * checks if a hungyball is on the field, which is represented by current.
     * @param current contains the Objects, that are placed on a field in the level.
     * @return true if a hungryball is placed on the given field.
     */
    public static boolean containsHungryBall(List<IngameObject> current){
        for(IngameObject o : current){
            if(o instanceof HungryBall){
                return true;
            }
        }
        return false;
    }
    /**
     * checks if there is no Object on the field, which is represented by current.
     * @param current contains the Objects, that are placed on a field in the level.
     * @return true if the field is empty.
     */
    public static boolean isEmpty(List<IngameObject> current){
        return current.size() ==0;
    }
    /*
    /**
     * returns a list of walls on the field, which is represented by current.
     * @param current contains the Objects, that are placed on a field in the leve.
     * @return List of walls on the field.
     */
    /*
    private static List<Wall> returnWalls(List<IngameObject> current){
        List<Wall> walls = new LinkedList<>();
        for(IngameObject o : current){
            if(o instanceof Wall){
                walls.add((Wall) o);
            }
        }
        return walls;
    }
     */

    /**
     * returns a list that holds the enemies that are in the level.
     * @return a list that holds the enemies that are in the level.
     */
    @SuppressWarnings("unchecked")
    public LinkedList<Enemy> getEnemyList() {
        //return a shallow copy of the list
        //warning is supressed here, since clone should return an object of the same type -> casting should be safe here
        return (LinkedList<Enemy>) enemyList.clone();
    }

    /**
     * returns the coordinates of the level, where the enemies are respawned after they were eaten.
     * @return the coordinates of the level, where the enemies are respawned after they were eaten.
     */
    public int[] getEnemyRespawnPosition() {
        return enemyRespawnPosition;
    }

    /**
     * returns the coordinates of the level, where the HungryBall object for first player is spawned.
     * @return the coordinates of the level, where the HungryBall object for first player is spawned.
     */
    public int[] getPlayer1SpawnPosition() {
        return player1SpawnPosition;
    }

    /**
     * returns the coordinates of the level, where the HungryBall object for second player is spawned.
     * @return the coordinates of the level, where the HungryBall object for second player is spawned.
     */
    public int[] getPlayer2SpawnPosition() {
        return player2SpawnPosition;
    }

    /**
     * initialises the internal level matrix.
     * Must be called before modifying it (by adding /deleting items).
     * @param levelSize_x horizontal size of the level.
     * @param levelSize_y vertical size of the level.
     */
    protected void initializeLevelMatrix (int levelSize_x, int levelSize_y){
        //create matrix
        //ingameObjects = new IngameObject[size.x][size.y];
        ingameObjects = new ArrayList<>();

        //create the lists, which are needed to store the IngameObjects
        for(int y = 0; y < levelSize_y; y++) {
            ingameObjects.add(new ArrayList<>());
            for(int x = 0; x < levelSize_x; x++) {
                ingameObjects.get(y).add(new LinkedList<>());
            }
        }
    }

    /**
     * returns the number of eatable objects in the level (excluding enemies).
     * @return the number of eatable objects in the level (excluding enemies).
     */
    public int getEatableCounter(){
        return eatableCounter;
    }
}