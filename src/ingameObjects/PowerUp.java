package ingameObjects;

/** Objects that grants the player invulnerability and the ability to eat enemies, when eaten */
public class PowerUp extends IngameObject implements Eatable{
    /**
     * initializes the position of the Object.
     *
     * @param position_x x Position of the Object in the level.
     * @param position_y y Position of the Object in the level.
     */
    public PowerUp(int position_x, int position_y) {
        super(position_x, position_y);
        this.sprite = "/resources/powerUp.png";
    }

    /**
     * the score a player gets when eating a powerUp.
     * @return score the player gets when eating a powerUp.
     */
    @Override
    public int getValue() {
        return 50;
    }
}
