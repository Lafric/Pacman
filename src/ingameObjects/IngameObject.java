package ingameObjects;

import level.Level;

import java.io.*;
/** represents characters and static objects that are used games levels */
public abstract class IngameObject implements Serializable{
    /** the x-coordinate of the object placed in the level. */
    private int position_x;
    /** the y-coordinate of the object placed in the level. */
    private int position_y;

    /** Location of the sprite of the object */
    protected String sprite;

    /** tilt of the sprite 0 = 0°, 1 = 90°, -1 = -90° ...*/
    protected int spriteQuadrantRotation = 0;
    /** true = sprite should be mirrored along the y-axis, false if not mirrored */
    protected boolean isSpriteMirrored = false;

    /**
     * initializes the position of the object.
     * @param position_x x position of the object in the level.
     * @param position_y y position of the object in the level.
     */
    public IngameObject(int position_x, int position_y){
        this.position_x = position_x;
        this.position_y = position_y;
    }
    /**
     * returns the x-coordinate of the object placed in the level.
     * @return the x-coordinate of the object placed in the level.
     */
    public int getPosition_x() {
        return position_x;
    }
    /**
     * returns the y-coordinate of the object placed in the level.
     * @return the y-coordinate of the object placed in the level.
     */
    public int getPosition_y() {
        return position_y;
    }
    /**
     * Sets x coordinate of the Object to a number > 0.
     * @param x the x-coordinate of the object placed in the level.
     * @param l level in which the object ist placed.
     * @throws IllegalArgumentException when the x-coordinate is less than 0.
     */
    public void setPosition_x(int x, Level l){
        if(x >= 0){
            //If the Object is in a level, change the position in the level too

            if(l!=null){

                l.deleteIngameObject(this);

                position_x = x;
                l.addIngameObject(this);
            }
        }
        else{
            throw new IllegalArgumentException("The x-coordinate must be >= 0");
        }
    }

    /**
     * Sets y coordinate of the Object to a number > 0.
     * @param y the y-coordinate of the object placed in the level.
     * @param l level in which the object ist placed.
     * @throws IllegalArgumentException when the y-coordinate is less than 0.
     */
    public void setPosition_y(int y, Level l){

        if(y >= 0) {
            //If the Object is in a level, change the position in the level too

            if(l!=null){

                l.deleteIngameObject(this);

                position_y = y;
                l.addIngameObject(this);
            }

        }
        else {
            throw new IllegalArgumentException("The y-coordinate must be >= 0");
        }
    }

    /**
     * returns the path to the sprite of the object as a String.
     * @return the path to the sprite of the object as a String.
     */
    public String getSprite(){
        return sprite;
    }

    /**
     * returns a flag, which indicates the tilt / rotation of the sprite.
     * @return tilt of the sprite 0 = 0°, 1 = 90°, -1 = -90° ...
     */
    public int getSpriteQuadrantRotation(){
        return spriteQuadrantRotation;
    }

    /**
     * returns a flag which indicates if the sprite should be mirrored.
     * @return true = sprite should be mirrored along the y-axis, false if not mirrored.
     */
    public boolean getSpriteMirrored(){
        return isSpriteMirrored;
    }

    //not used anymore
    /*
     * creates a deep copy of the object, using serialisation.
     * @return deep copy of the object.
     */
    /*
    public IngameObject copy(){
        //code copied from here
        //https://stackoverflow.com/questions/64036/how-do-you-make-a-deep-copy-of-an-object

        IngameObject object;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();

            //Restore your class from a stream of bytes:
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            object = (IngameObject) new ObjectInputStream(bais).readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return object;
    }
     */
}
