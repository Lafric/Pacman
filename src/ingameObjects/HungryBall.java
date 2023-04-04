package ingameObjects;

import audio.AudioPlayer;
import game.Game;
import level.Level;
import utility.GifSequenceWriter;


import java.util.Timer;
import java.util.TimerTask;

/** represents the player characters (controlled by the players), in the games level*/
public class HungryBall extends Character{
    /**
     * the highscore points that the player has currently earned.
     */
    private int score = 0;   // max score: 999999999; reset to 0
    /**
     * the amount of times the hungryball is allowed to get hit by enemies.
     */
    private int lives;   // max lives: 2; reset to 2
    /**
     * the level in which the hungryball is placed
     */
    private final Level l;
    /**
     * if true, the player can eat enemies during a collision, otherwise he loses a live.
     */
    private boolean undefeatable = false;
    /**
     * sets {@code undefeatable = false} after a certain amount of time.
     */
    private Timer undefeatableTimer;
    /**
     * the String, which is displayed as the players name in the game GUI.
     */
    public final String name;
    //private String undefeatableSprite = "/resources/HungryBallRight_undefeatable.gif";
    /**
     * Path to the location of the sprite for the undefeatable state of the hungryball.
     */
    private final String undefeatableSprite;
    /** the game in which the hungryball object acts as a player character */
    private final Game game;

    //private Image sprite2;

    /** plays a sound when the HungryBall eats a ScorePoint object */
    private AudioPlayer eatScorePointPlayer;
    /** plays a sound when the HungryBall eats a PowerUp object */
    private AudioPlayer eatPowerUpPlayer;
    /** plays a sound when the HungryBall eats an Enemy object */
    private AudioPlayer eatEnemyPlayer;
    /** the path to the audio file used to create a sound when the hungryball eats a ScorePoint object */
    private final String eatScorePointAudioFilePath = "/resources/beep.au";
    /** the path to the audio file used to create a sound when the hungryball eats a PowerUp object */
    private final String eatPowerUpAudioFilePath = "/resources/blip.au";
    /** the path to the audio file used to create a sound when the hungryball eats an Enemy object */
    private final String eatEnemyFilePath = "/resources/pop.wav";

    //private final AudioPlayer eatPlayer = new AudioPlayer("/resources/notification-sound.au");

    /**
     * initializes the position of the Character.
     *
     * @param position_x x Position of the Character in the level.
     * @param position_y y Position of the Character in the level.
     * @param direction direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down
     * @param l level in which the HungryBall is created.
     * @param name string, which is displayed in the gui window as the name of the player.
     * @param game the game in which the hungryball object acts as a player character.
     * @param lives a value for the speed of the animation of the hungryballs.
     * @param delay time (in ms) between the change of the 2 images.
     */
    public HungryBall(int position_x, int position_y, int direction, Level l, String name, Game game, int lives, int delay) {
        super(position_x, position_y, direction);
        //this.sprite = "/resources/HungryBallRight.gif";
        //HungryBall.sprite = "/resources/HungryBallRight.gif";


        this.l = l;
        this.name = name;
        this.game = game;
        this.lives = lives;

        //create the gif for the mouth animation with a specified delay
        //if(!animationCreated) {
        //animationCreated = true;
        sprite = utility.GifSequenceWriter.createGIF("/resources/HungryBallRight_closed.png","/resources/HungryBallRight_open.png",delay);
        undefeatableSprite = GifSequenceWriter.createGIF("/resources/HungryBallRight_undefeatable_closed.png", "/resources/HungryBallRight_undefeatable_open.png",delay);
        //this.sprite2 = Utility.GifSequenceWriter.createGIF("/resources/HungryBallRight_closed.png","/resources/HungryBallRight_open.png",delay);


        setUpAudioPlayers();
    }

    //not in use anymore, was used for deep copies before
    /*
     * initializes the position of the Character.
     * <p>
     * does not set up the audioplayer.
     *
     * @param position_x x Position of the Character in the level.
     * @param position_y y Position of the Character in the level.
     * @param direction direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down
     * @param l level in which the HungryBall is created.
     * @param name string, which is displayed in the gui window as the name of the player.
     * @param game the game in which the hungryball object acts as a player character.
     * @param sprite the path to the gif file for the normal hungryball animation.
     * @param undefeatableSprite the path to the gif file for the hungryball animation, when the hungryball is undefeatable.
     * @param lives a value for the speed of the animation of the hungryballs.
     */
    /*
    private HungryBall(int position_x, int position_y, int direction, Level l, String name, Game game, String sprite, String undefeatableSprite, int lives) {
        super(position_x, position_y, direction);

        this.l = l;
        this.name = name;
        this.game = game;
        this.lives = lives;

        this.sprite = sprite;
        this.undefeatableSprite = undefeatableSprite;

    }
     */

    /**
     * returns the score / points which the player has earned until now.
     * @return the score / points which the player has earned until now.
     */
    public int getScore() {
        return score;
    }

    /**
     * sets the score / points which the player has earned until now.
     * @param score the score / points which the player has earned until now.
     */
    private void setScore(int score) {
        this.score = Integer.min((score), 999999999);
    }

    /**
     * adds score to the players earned score.
     * @param amount the score is to be added to the players score.
     */
    public void increaseScore(int amount) {
        this.score = Integer.min((score + amount), 999999999);
    }

    /**
     * removes 1 life from the players lives.
     */
    public void decreaseLives() {
        setLives(lives - 1);
    }
    /*
    public void increaseLives() {
        if (this.lives < 2) { this.lives++; }
    }
     */

    /**
     * returns the amount of times the hungryball is allowed to get hit by enemies.
     * @return the amount of times the hungryball is allowed to get hit by enemies.
     */
    public int getLives(){
        return this.lives;
    }

    /**
     * sets the amount of times the hungryball is allowed to get hit by enemies.
     * @param lives the amount of times the hungryball is allowed to get hit by enemies.
     */
    public void setLives(int lives) {this.lives = Integer.max(lives, 0);}

    /**
     * returns the level in which the hungryball is placed.
     * @return the level in which the hungryball is placed.
     */
    public Level getLevel() {return this.l;}

    /**
     * removes the Eatable from the level and adds points according to the Eatable to the score.
     * If the Eatable is a powerup, adds a timed effect on the Hungryball, so it can eat enemies.
     * @param e Eatable, that is to be eaten.
     */
    public void eat(Eatable e){
        increaseScore(e.getValue());
        l.deleteEatable(e);
        if (e instanceof Enemy) {
            eatEnemyPlayer.play();
        }
        else if(e instanceof PowerUp) {
            eatPowerUpPlayer.play();
            //Hungryball can now eat Enemies

            if (undefeatableTimer != null) {
                //timer should be reset if another powerup is eaten before it expires
                undefeatableTimer.cancel();

            }
            undefeatableTimer = new Timer();

            //Hungryball is not undefeatable anymore after the task was executed
            TimerTask task = new TimerTask(){
                @Override
                public void run(){
                    setUndefeatable(false);
                    //make sure the Timer Thread terminates after the task was executed
                    undefeatableTimer.cancel();
                }
            };
            undefeatableTimer.schedule(task, game.getUpdateTime() * 50L);

            setUndefeatable(true);
        }
        else if(e instanceof ScorePoint || e instanceof Fruit){
            eatScorePointPlayer.play();
        }


    }
    /**
     * sets the tilt / rotation of how the sprite is to be displayed.
     * 0 = 0°, 1 = 90°, -1 = -90° ...
     * @param rotation the tilt / rotation of how the sprite is to be displayed.
     */
    public void setSpriteQuadrantRotation(int rotation){
        this.spriteQuadrantRotation = rotation;
    }

    /**
     * sets a flag which indicates wether the sprite should be mirrored along the y-axis.
     * @param isSpriteMirrored true = sprite should be mirrored along the y-axis, false if not mirrored.
     */
    public void setSpriteMirrored(boolean isSpriteMirrored){
        this.isSpriteMirrored = isSpriteMirrored;
    }

    /**
     * sets the players capability to eat enemies.
     * true if he is able to eat enemies, false if not (player looses a life instead).
     * @param undefeatable players capability to eat enemies.
     */
    public void setUndefeatable(boolean undefeatable){
        this.undefeatable = undefeatable;
    }

    /**
     * returns true if the player is currently able to eat enemies.
     * @return true if the player is currently able to eat enemies.
     */
    public boolean getUndefeatable(){ return this.undefeatable;}

    //not used anymore
    /*
     * returns a deep-copy of the HungryBall object itself.
     * @return deep-copy of the object itself.
     */
    /*
    public HungryBall copy(){
        //HungryBall hb = new HungryBall(getPosition_x(), getPosition_y(), getDirection(), l, name, game);
        HungryBall hb = new HungryBall(getPosition_x(), getPosition_y(), getDirection(), l, name, game, sprite, undefeatableSprite, lives);
        hb.setScore(getScore());
        hb.setLives(getLives());
        hb.setSpriteMirrored(isSpriteMirrored);
        hb.setSpriteQuadrantRotation(spriteQuadrantRotation);
        hb.setUndefeatable(undefeatable);
        //tmp to test
        //hb.sprite2 = this.sprite2;
        return hb;
    }
     */

    /**
     * returns the absolute path to the currently active sprite as a String, depending on if the player is currently undefeatable or not.
     * @return the absolute path to the currently active sprite.
     */
    @Override
    public String getSprite() {
        if(undefeatable){
            return undefeatableSprite;
        }
        else{
            return sprite;
        }
    }
    /*
    public void deleteSpriteFile() throws IOException {
        //Path p = Files.createTempFile("testDeleteFile",".png");
        //System.out.println(Files.deleteIfExists(p));

        System.out.println(sprite);
        //Files.deleteIfExists(Path.of(Objects.requireNonNull(getClass().getResource(sprite)).toExternalForm()));
        System.out.println(Files.deleteIfExists(Path.of(sprite)));
        System.out.println(undefeatableSprite);
        //Files.deleteIfExists(Path.of(Objects.requireNonNull(getClass().getResource(sprite)).toExternalForm()));
        System.out.println(Files.deleteIfExists(Path.of(undefeatableSprite)));
    }
     */
/*
    public Image getSprite2() {
        System.out.println(sprite2.getSource());
        return sprite2;
    }
*/

    /**
     * free the acquired resources.
     */
    public void close() {
        eatScorePointPlayer.close();
        eatPowerUpPlayer.close();
        eatEnemyPlayer.close();
    }

    /**
     * initialises the AudioPlayers needed to play the sound.
     */
    private void setUpAudioPlayers() {
        eatScorePointPlayer = new AudioPlayer(eatScorePointAudioFilePath, game.getUpdateTime());
        eatScorePointPlayer.setVolume(0.2f);
        eatPowerUpPlayer = new AudioPlayer(eatPowerUpAudioFilePath, game.getUpdateTime());
        eatEnemyPlayer = new AudioPlayer(eatEnemyFilePath, game.getUpdateTime());
        eatEnemyPlayer.setVolume(0.4f);
    }
}
