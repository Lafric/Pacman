package ingameObjects;

/** static object in the level, that grants the player a small amount of score when eaten */
public class ScorePoint extends IngameObject implements Eatable{
    /**
     * The score the player gets for eating the scorePoint.
     */
    private final int value = 50;
    /**
     * initializes the position of the Object.
     *
     * @param position_x x Position of the Object in the level.
     * @param position_y y Position of the Object in the level.
     */
    public ScorePoint(int position_x, int position_y) {
        super(position_x, position_y);
        this.sprite = "/resources/scorePoint.png";
    }

    /**
     * returns the score the player gets when eating this object.
     * @return the score the player gets when eating this object.
     */
    @Override
    public int getValue() {
        return value;
    }
}
