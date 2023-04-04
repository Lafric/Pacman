package ingameObjects;

/** represents static objects, that grant the player score when eaten (more than a normal scorepoint) */
public abstract class Fruit extends IngameObject implements Eatable{
    /**
     * The score the player gets for eating the fruit.
     */
    protected int value=100;
    /**
     * initializes the position of the Object.
     *
     * @param position_x x Position of the Object in the level.
     * @param position_y y Position of the Object in the level.
     */
    public Fruit(int position_x, int position_y) {
        super(position_x, position_y);
    }

    /**
     * returns the score the player gets when eating this object.
     * @return the score the player gets when eating this object.
     */
    public int getValue(){
        return value;
    }
}
