package ingameObjects;

/** An eatable fruit, which gives scorepoints and can be placed in the games level*/
public class Cherry extends Fruit{
    /**
     * initializes the position of the Object.
     *
     * @param position_x x Position of the Object in the level.
     * @param position_y y Position of the Object in the level.
     */
    public Cherry(int position_x, int position_y) {
        super(position_x, position_y);
        value = 100;
        this.sprite = "/resources/cherry.png";
    }
}
