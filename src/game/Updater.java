package game;

import audio.AudioPlayer;
import ingameObjects.*;
import level.Level;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

/**
 * class which provides several functions to update the state of the game.
 */
public final class Updater {

    /**
     * updates the position of a HungryBall object in the game.
     * @param l level in which the game takes place.
     * @param player the player whose position is to be updated.
     */
    public static void updatePlayer(Level l, HungryBall player) {


        int height = l.getHeight();
        int width = l.getWidth();


        //chosen function depends on the movement of the character (x/y-axis)
        BiConsumer<Integer, Level> setPlayerPosition;
        //represents the changed coordinate (on x or y-axis on which the character has moved)
        int nextCoordinate = -1;


        switch (player.getDirection()) {
            // character moving up
            case 2:
                nextCoordinate = (player.getPosition_y() + height - 1) % height;
                //check which object is in front first
                List<IngameObject> up = l.getIngameObjectsOnPosition(player.getPosition_x(), nextCoordinate);
                //choose Method
                setPlayerPosition = player::setPosition_y;
                moveHungryBall(player, l, up, nextCoordinate, setPlayerPosition);
                break;
            // character moving right
            case 1:
                nextCoordinate = (player.getPosition_x() + 1) % width;
                //check which object is in front first
                List<IngameObject> right = l.getIngameObjectsOnPosition(nextCoordinate, player.getPosition_y());
                //choose Method
                setPlayerPosition = player::setPosition_x;
                moveHungryBall(player, l, right, nextCoordinate, setPlayerPosition);
                break;

            // character moving down
            case -2:
                nextCoordinate = (player.getPosition_y() + 1) % height;
                //check which object is in front first
                List<IngameObject> down = l.getIngameObjectsOnPosition(player.getPosition_x(), nextCoordinate);
                //choose Method
                setPlayerPosition = player::setPosition_y;
                moveHungryBall(player, l, down, nextCoordinate, setPlayerPosition);
                break;

            // character moving left
            case -1:
                nextCoordinate = (player.getPosition_x() + width - 1) % width;
                //check which object is in front first
                List<IngameObject> left = l.getIngameObjectsOnPosition(nextCoordinate, player.getPosition_y());
                //choose Method
                setPlayerPosition = player::setPosition_x;
                moveHungryBall(player, l, left, nextCoordinate, setPlayerPosition);
                break;

            default:
                //character does not moveHungryBall
                break;
        }

    }

    /**
     * moves Hungryball object around in the level, depending on which object is in front of it.
     *
     * @param player            Hungryball that is to be moved.
     * @param l                 level in which the Hungryball is to be moved.
     * @param ahead             objects that are in front of the Hungryball (depending on its direction).
     * @param nextCoordinate    represents the changed coordinate (on x or y-axis on which the character has moved).
     * @param setPlayerPosition chosen function depends on the movement of the character (x/y-axis).
     */
    private static void moveHungryBall(HungryBall player, Level l, List<IngameObject> ahead, int nextCoordinate, BiConsumer<Integer, Level> setPlayerPosition) {
        //decide what to do based on which Object is ahead
        if (Level.containsWall(ahead)) {
            //stop if wall is ahead
            player.setDirection(0);
        }
        /*else if (Level.containsEnemy(ahead)) {
            setPlayerPosition.accept(nextCoordinate, l);
        }*/
        else if (Level.containsFruit(ahead) || Level.containsScorePoint(ahead) || Level.containsPowerUp(ahead)) {
            //eat object and moveHungryBall on
            for(IngameObject o : ahead){
                if(o instanceof Fruit || o instanceof ScorePoint || o instanceof PowerUp){
                    player.eat((Eatable) o);
                }
                setPlayerPosition.accept(nextCoordinate, l);
            }
        } else if (Level.isEmpty(ahead)|| Level.containsHungryBall(ahead) || Level.containsEnemy(ahead)) {
            //moveHungryBall the object Up without any object in front of him
            setPlayerPosition.accept(nextCoordinate, l);
        }
    }


    /**
     * determines directions in which there are no walls surrounding an enemy (= directions the enemy can move in)
     * and adds them to a list.
     *
     * @param e   enemy whose surroundings are to be examined.
     * @param l   level in which the Enemy is to be moved.
     */
    private static LinkedList<Integer> checkAdjacentFields(Level l, Enemy e) {

        int height = l.getHeight();
        int width = l.getWidth();
        LinkedList<Integer> availableDirections = new LinkedList<>();

        if (!(Level.containsWall(l.getIngameObjectsOnPosition(e.getPosition_x(), (e.getPosition_y() + height - 1) % height)))) {
            availableDirections.add(2);
        }
        if (!Level.containsWall(l.getIngameObjectsOnPosition((e.getPosition_x() + 1) % width, e.getPosition_y()))) {
            availableDirections.add(1);
        }
        if (!(Level.containsWall(l.getIngameObjectsOnPosition(e.getPosition_x(), (e.getPosition_y() + 1) % height)))) {
            availableDirections.add(-2);
        }
        if (!(Level.containsWall(l.getIngameObjectsOnPosition((e.getPosition_x() + width- 1) % width, e.getPosition_y())))) {
            availableDirections.add(-1);
        }
        //System.out.println("enemy: "+ e.getPosition_x() +" "+ e.getPosition_y() +" available directions: "+ availableDirections);
        return availableDirections;
    }


    /**
     * determines directions for each Enemy in the level and moves them in those directions (if able to).
     *
     * @param l     level in which the Enemy is to be moved.
     * @param eL    List of Enemies present in the current level.
     */
    public static void updateEnemies(Level l, LinkedList<Enemy> eL) {

        int height = l.getHeight();
        int width = l.getWidth();

        for (int i = 0; i < eL.size(); i++) {

            // current enemy
            Enemy e = eL.get(i);
            LinkedList<Integer> availableDirections = checkAdjacentFields(l, e);


            // update direction

            switch (availableDirections.size()) {
                // enemy trapped, set to static
                case 0:
                    // set Direction to static. This is needed for the checkCollision method, which uses the directions to check if a collision happened.
                    // if the direction is not set here, there is a special case, where a collision is detected when it should not be.
                    e.setDirection(0);
                    break;
                // dead end, turn towards way out
                case 1:
                    e.setDirection(availableDirections.get(0));
                    break;

                // in a corridor, keep moving straight (or make a turn if in a corner)
                case 2:
                    // if the enemy has spawned in a corridor, choose a random direction
                    if (e.getDirection() == 0) {
                        int randomDir = (int) (Math.random() * availableDirections.size());
                        e.setDirection(availableDirections.get(randomDir));
                    }
                    //
                    else {
                        // turn left or right if way ahead is blocked (corner)
                        if (!availableDirections.contains(e.getDirection())) {
                            // if the first available direction is the direction the enemy came from, choose the other entry to go around the corner
                            if (availableDirections.get(0) + e.getDirection() == 0) {
                                e.setDirection(availableDirections.get(1));
                            }
                            else {
                                e.setDirection(availableDirections.get(0));
                            }
                        }
                    }
                    break;

                // in a crossing, choose a random direction to go in
                default:
                    // generate random index in range 0 (included) and size (excluded)
                    int randomDir = (int) (Math.random() * availableDirections.size());
                    e.setDirection(availableDirections.get(randomDir));
                    break;
            }

            // move in updated direction
            switch (e.getDirection()) {
                case -1:
                    e.setPosition_x((e.getPosition_x() + width - 1) % width, l);
                    break;
                case -2:
                    e.setPosition_y((e.getPosition_y() + 1) % height, l);
                    break;
                case 1:
                    e.setPosition_x((e.getPosition_x() + 1) % width, l);
                    break;
                case 2:
                    e.setPosition_y((e.getPosition_y() + height - 1) % height, l);
                    break;
                default:
                    break;  // no movement
            }
        }
    }

    /**
     * checks if the player and an enemy have collided and handles the collision itself.
     * <p>
     * requires that the direction of the player is properly set (0 when player is standing still).
     *
     * @param player Hungryball object that is to be checked for collision with an enemy.
     * @param enemyList List of Enemies present in the current level.
     * @param game the game in which the collision takes place.
     */
    public static void checkCollision(HungryBall player, LinkedList<Enemy> enemyList, Game game){
        //create shallow copy of the list first to avoid concurrentModificationException
        //LinkedList<Enemy> eL = (LinkedList<Enemy>) enemyList.clone();

        for(Enemy e: enemyList){
            //check if current position of player and an enemy overlaps
            //and if the player and an enemy have swapped positions(were never on the same field
            // and went in the opposite direction of each other)
            int pX = player.getPosition_x();
            int eX = e.getPosition_x();
            int pY = player.getPosition_y();
            int eY = e.getPosition_y();

            if(pX == eX && pY == eY ||
                    (player.getDirection() == e.getDirection() * -1
                            && ((pX == eX && pY == eY - 1 && player.getDirection() == 2) || (pX == eX && pY == eY + 1 && player.getDirection() == -2)
                                || (pX == eX +1 && pY == eY && player.getDirection() == 1) || (pX == eX -1 && pY == eY && player.getDirection() == -1)))) {
                handleCollision(player, e, game);
            }
        }
    }

    /**
     * actions when the Pacman collides with the Enemy.
     * if the player is currently undefeatable, the enemy will be eaten, otherwise the player loses 1 life.
     * @param p player Character which has collided with an enemy.
     * @param e Enemy which has collided with a hungryball.
     */
    private static void handleCollision(HungryBall p, Enemy e, Game game){
        if(p.getUndefeatable()){
            p.eat(e);
            Level l = p.getLevel();
            //the Enemy reborn at a fixed position
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run(){
                    //l.addEnemy(e.getColor(), l.getEnemyRespawnPosition()[0], l.getEnemyRespawnPosition()[1]);
                    l.addIngameObject(new Enemy(l.getEnemyRespawnPosition()[0], l.getEnemyRespawnPosition()[1], 0, e.getColor()));
                    //make sure the Timer Thread terminates after the task was executed
                    timer.cancel();
               }
            };
            timer.schedule(task, game.getUpdateTime() * 50L);
        }else{
            //play audio
            AudioPlayer hurtPlayer = new AudioPlayer("/resources/hurt.au");
            hurtPlayer.setVolume(0.25f);
            hurtPlayer.playAndClose();
            //decrease lives
            p.decreaseLives();
        }
    }
}

