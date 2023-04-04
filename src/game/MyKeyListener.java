package game;

import ingameObjects.HungryBall;
import ingameObjects.IngameObject;
import level.Level;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * The class MyKeyListener extends the KeyAdapter class and provides the implementation
 * of keyPressed() and keyReleased() methods. It contains the logic for movement control
 * of a HungryBall object.
 *
 * @author Group 6
 * @version 1.0
 * @since 2023-02-06
 */
public class MyKeyListener extends KeyAdapter {
    /** The HungryBall object which is being controlled by the key events */
    private final HungryBall player;

    /** Key code for the Up movement */
    private final int key_Up;

    /** Key code for the Right movement */
    private final int key_Right;

    /** Key code for the Down movement */
    private final int key_Down;

    /** Key code for the Left movement */
    private final int key_Left;

    /** Flag to indicate if a key is currently pressed */
    private boolean isPressed = false;

    /** Indicates the movement behavior of the player */
    private final int movementBehaviour;

    /**
     * Constructs a MyKeyListener object with specified parameters.
     *
     * @param player The HungryBall object which is being controlled by the key events
     * @param key_Up Key code for the Up movement
     * @param key_Right Key code for the Right movement
     * @param key_Down Key code for the Down movement
     * @param key_Left Key code for the Left movement
     * @param movementBehaviour Indicates the movement behavior of the player
     */
    public MyKeyListener(HungryBall player, int key_Up,int key_Right,int key_Down,int key_Left, int movementBehaviour) {
        this.player = player;
        this.key_Up = key_Up;
        this.key_Right = key_Right;
        this.key_Down = key_Down;
        this.key_Left = key_Left;
        this.movementBehaviour = movementBehaviour;
    }

    /**
     * This method is called when a key is pressed. It checks the key code of the pressed key
     * and updates the player's direction accordingly.
     *
     * @param e The KeyEvent representing the pressed key
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int height = player.getLevel().getHeight();
        int width = player.getLevel().getWidth();

        if(movementBehaviour == 0){
            movementControlKeyIsPressed(keyCode, width, height);
        }
        if(!isPressed && movementBehaviour == 1){
            movementControlKeyIsPressed(keyCode, width, height);
        }
    }

    /**
     * Sets the value of the isPressed flag.
     *
     * @param value The value to be set for the isPressed flag
     */
    public void setIsPressed(boolean value) {
        isPressed = value;
    }

    /**
     * Returns the value of the isPressed flag.
     *
     * @return The value of the isPressed flag
     */
    public boolean getIsPressed(){
        return isPressed;
    }

    /**
     * change the direction and orientation of the Object when key is pressed
     * @param ahead the object in front of the player.
     * @param direction the integer that represents the direction
     * @param spriteRotation integer rotation of the sprite
     */
    private void keyIsPressed(List<IngameObject> ahead, int direction, int spriteRotation){
        //do nothing when we have a wall ahead
        if (!Level.containsWall(ahead)) {
            player.setDirection(direction);
            player.setSpriteQuadrantRotation(spriteRotation);
            if (movementBehaviour == 1){
                isPressed = true;
            }
        }
    }

    /**
     * control the movement according to Keyboard code of the pressed key
     * @param keyCode integer that represent a direction (based on the pressed key)
     * @param width of the level
     * @param height of the level
     */
    private void movementControlKeyIsPressed(int keyCode, int width, int height){
        if (keyCode == key_Up) {
            int nextCoordinate = (player.getPosition_y() + height - 1) % height;
            //check which object is in front first
            List<IngameObject> up = player.getLevel().getIngameObjectsOnPosition(player.getPosition_x(), nextCoordinate);
            keyIsPressed(up, 2,-1);
        } else if (keyCode == key_Right) {
            int nextCoordinate = (player.getPosition_x() + 1) % width;
            //check which object is in front first
            List<IngameObject> right = player.getLevel().getIngameObjectsOnPosition(nextCoordinate, player.getPosition_y());
            keyIsPressed(right, 1,0);
        } else if (keyCode == key_Down) {
            int nextCoordinate = (player.getPosition_y() + 1) % height;
            //check which object is in front first
            List<IngameObject> down = player.getLevel().getIngameObjectsOnPosition(player.getPosition_x(), nextCoordinate);
            keyIsPressed(down, -2,1);
        } else if (keyCode == key_Left) {
            int nextCoordinate = (player.getPosition_x() + width - 1) % width;
            //check which object is in front first
            List<IngameObject> left = player.getLevel().getIngameObjectsOnPosition(nextCoordinate, player.getPosition_y());
            keyIsPressed(left, -1,2);
        }
    }

    /**
     * stop the movement of the object when movement Behavior is 1
     * and the key is released
     * @param keyCode integer that represents a direction (based on the key pressed).
     */
    private void movementControlKeyIsReleased(int keyCode){
        if(isPressed && movementBehaviour == 1){
            if (keyCode == key_Up) {
                setIsPressed(false);
                player.setDirection(0);
            } else if (keyCode == key_Right) {
                setIsPressed(false);
                player.setDirection(0);
            } else if (keyCode == key_Down) {
                setIsPressed(false);
                player.setDirection(0);
            } else if (keyCode == key_Left) {
                setIsPressed(false);
                player.setDirection(0);
            }
        }
    }

    /**
     * change the direction and orientation of the Object when key is released
     * @param e event when key is released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        movementControlKeyIsReleased(keyCode);
    }
}


