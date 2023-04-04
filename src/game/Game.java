package game;

import audio.AudioPlayer;
import gui.*;
import gui.Painter;
import ingameObjects.*;
import level.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * class, where the game itself is created and started.
 */
public class Game {

    /** GUI window in which the game itself is displayed */
    private IngameGui ingameGui;

    /** character object of the first player */
    private final HungryBall player1;
    /** character object of the second player */
    private final HungryBall player2;
    /** level/map in which the game is played in */
    private final Level level;
    /* list of enemies that are currently in the level */
    //private final LinkedList<Enemy> enemyList;
    /** determines the time in ms between 2 frames updating the state of the game*/
    private final int updateTime;
    /** determines the movement behaviour of the game.
     * 0 = standard,
     * 1 = behaviour B,
     * */
    private final int movementBehaviour;

    /**
     * creates a multiplayer game of hungryball.
     * @param name1 string which is displayed as the first players name in the GUI window.
     * @param name2 string which is displayed as the second players name in the GUI window.
     * @param l level/map in which the game is played in
     * @param lives the amount of times a player can die before he is removed from the game.
     * @param delay time (in ms) between the change of the 2 images.
     * @param gameUpdateTime determines the time in ms between 2 frames updating the gamestate.
     * @param movementBehaviour determines the movement behaviour of the game 0 = standard, 1 = behaviour B.
     */
    public Game(String name1, String name2, Level l, int lives, int delay, int gameUpdateTime, int movementBehaviour){
        int[] p1Spawn = l.getPlayer1SpawnPosition();
        int[] p2Spawn = l.getPlayer2SpawnPosition();

        this.updateTime = gameUpdateTime;

        this.player1 = new HungryBall(p1Spawn[0], p1Spawn[1], 0, l, name1, this, lives, delay);
        this.player2 = new HungryBall(p2Spawn[0], p2Spawn[1], 3, l, name2, this, lives, delay);
        l.addIngameObject(player1);
        l.addIngameObject(player2);


        this.level = l;
        this.movementBehaviour = movementBehaviour;
    }

    /**
     * creates a singleplayer game of hungryball.
     * @param name string which is displayed as the players name in the GUI window.
     * @param l level/map in which the game is played in
     * @param lives the amount of times a player can die before he is removed from the game.
     * @param delay time (in ms) between the change of the 2 images.
     * @param gameUpdateTime determines the time in ms between 2 frames updating the gamestate.
     * @param movementBehaviour determines the movement behaviour of the game 0 = standard, 1 = behaviour B.
     */
    public Game(String name, Level l, int lives, int delay, int gameUpdateTime, int movementBehaviour){
        int[] p1Spawn = l.getPlayer1SpawnPosition();

        this.updateTime = gameUpdateTime;

        this.player1 = new HungryBall(p1Spawn[0], p1Spawn[1], 0, l, name, this, lives, delay);
        l.addIngameObject(player1);


        this.player2 = null;
        this.level = l;

        this.movementBehaviour = movementBehaviour;
    }

    /**
     * creates the ingameGUI window, starts/runs the game.
     */
    public void startGame(){

        boolean isRunning = true;


        //run GUI related things in event-dispatch thread
        try {
            createIngameGui(level.getWidth(), level.getHeight(), player1, player2);


            //if the player starts on a field that has an eatable or powerup on it, eat it
            java.util.List<IngameObject> currentField = level.getIngameObjectsOnPosition(player1.getPosition_x(), player1.getPosition_y());

            if (Level.containsScorePoint(currentField) || Level.containsFruit(currentField)
                    || Level.containsPowerUp(currentField)) {
                for (IngameObject o : currentField) {
                    if (o instanceof Eatable) {
                        player1.eat((Eatable) o);
                    }
                }
            }
            if (player2 != null) {
                currentField = level.getIngameObjectsOnPosition(player2.getPosition_x(), player2.getPosition_y());
                if (Level.containsScorePoint(currentField) || Level.containsFruit(currentField)
                        || Level.containsPowerUp(currentField)) {
                    for (IngameObject o : currentField) {
                        if (o instanceof Eatable) {
                            player2.eat((Eatable) o);
                        }
                    }
                }
            }


            Painter.paintLevelObects(level, ingameGui);

            //run the game
            while (isRunning) {
                try {
                    Thread.sleep(updateTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }



                //player character disappears when the lives of the player are 0
                if (player1.getLives() <= 0) {
                    level.deleteIngameObject(player1);
                    player1.close();
                } else {
                    Updater.updatePlayer(level, player1);
                    //add 5 points to the score of a player if he survived the time step
                    player1.increaseScore(5);
                }

                if (player2 != null) {
                    if (player2.getLives() <= 0) {
                        level.deleteIngameObject(player2);
                        player2.close();
                    } else {
                        Updater.updatePlayer(level, player2);
                        //add 5 points to the score of a player if he survived the time step
                        player2.increaseScore(5);
                    }
                }

                // update enemies in the level and check for collision of enemies and hungryballs
                if (level.getEnemyList() != null) {
                    Updater.updateEnemies(level, level.getEnemyList());

                    if(player1.getLives() > 0) {
                        Updater.checkCollision(player1, level.getEnemyList(), this);
                    }

                    if (player2 != null && player2.getLives() > 0) {
                        Updater.checkCollision(player2, level.getEnemyList(), this);
                    }
                }

                Painter.paintLevelObects(level, ingameGui);


                // end game if the player/s lost all their lives or all eatables on the map have been eaten
                if (((player1.getLives() <= 0) && (player2 == null || player2.getLives() <= 0))
                        || level.getEatableCounter() == 0) {

                    //play a sound
                    AudioPlayer p = new AudioPlayer("/resources/success.au");
                    p.playAndClose();

                    isRunning = false;
                    if (player2 == null) {
                        new GameOverScreen(player1.getScore(), 1);
                    }
                    // add scores if instance was a multiplayer game
                    else {
                        new GameOverScreen(player1.getScore() + player2.getScore(), 2);
                    }
                    Window win = SwingUtilities.getWindowAncestor(ingameGui);
                    win.dispose();
                }
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * creates the GUI window, in which the game is played.
     * Method runs in event-dispatch-Thread.
     * @param levelSize_x width of the level, which is to be played.
     * @param levelSize_y height of the level, which is to be played.
     * @param player1 the playable character object of the first player1.
     * @param player2 the playable character object of the second player1.
     * @throws InterruptedException  if we're interrupted while waiting for the event dispatching thread to finish executing doRun.run() in SwingUtilities.invokeAndWait().
     * @throws InvocationTargetException if an exception is thrown while running doRun in SwingUtilities.invokeAndWait().
     */
    private void createIngameGui(int levelSize_x, int levelSize_y, HungryBall player1, HungryBall player2) throws InterruptedException, InvocationTargetException {
        //other Thread needs to wait until ingameGui is correctly assigned
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame("HungryBall");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //other thread still runs
            //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            IngameGui panel = new IngameGui(levelSize_x, levelSize_y, Game.this);

            frame.add(panel);
            frame.addKeyListener(new MyKeyListener(player1,38,39,40,37, movementBehaviour));
            if (player2 != null) {
                frame.addKeyListener(new MyKeyListener(player2, 87, 68, 83, 65, movementBehaviour));
            }

            //frame.setSize(panel.getScreen_Size_x(), panel.getScreen_Size_y());
            panel.setPreferredSize(new Dimension(panel.getScreenSizeX(), panel.getScreenSizeY()));
            //size window according to preferred size of subcomponents
            frame.pack();

            //frame.setResizable(false);

            //place window in the center of the screen
            frame.setLocationRelativeTo(null);

            //set icon in the titlebar
            Image icon = Toolkit.getDefaultToolkit().getImage(MainMenu.class.getResource(Config.getIconPath()));
            frame.setIconImage(icon);

            frame.setVisible(true);
            //return panel;
            ingameGui = panel;
        });
    }
    /**
     * returns a deep-copy of the hungryBall object of the first player.
     * @return hungryBall object (deep-copy) of the first player.
     */
    public HungryBall getPlayer1(){
        //return player1.copy();
        return player1;
    }

    /**
     * returns a deep-copy of the hungryBall object of the second player.
     * @return hungryBall object (deep-copy) of the second player if it exists, null otherwise.
     */
    public HungryBall getPlayer2(){
        /*
        if (player2 != null) {
            return player2.copy();
        }
        else{
            return null;
        }
         */
        return player2;
    }

    /**
     * returns the time it takes (in ms) until the gamestate is updated again after an update.
     * @return the time it takes (in ms) until the gamestate is updated again after an update.
     */
    public int getUpdateTime() {
        return updateTime;
    }

    /*
     * cleans up after running the game.
     * deletes the temp gif files for the hungryballs.
     */
    /*
    private void close(){
        try {
            player1.deleteSpriteFile();
            if (player2 != null) {
                player2.deleteSpriteFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
     */

}
