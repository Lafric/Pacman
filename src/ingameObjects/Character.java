package ingameObjects;

/** represents a moving character(HungryBall/Enemy) in the games level */
public abstract class Character extends IngameObject{
    /** direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down. */
    private int direction;

    /**
     * initializes the position of the Character.
     *
     * @param position_x x Position of the Character in the level.
     * @param position_y y Position of the Character in the level.
     * @param direction direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down.
     */
    public Character(int position_x, int position_y, int direction) {
        super(position_x, position_y);
        this.direction = direction;
    }

    /**
     *  returns the direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down.
     * @return the direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down.
     */
    public int getDirection() {
        return direction;
    }

    /**
     *  sets the direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down.
     * @param d the direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down.
     * @throws IndexOutOfBoundsException thrown when the parameter d does not match one of the 5 specified values.
     */
    public void setDirection(int d) throws IndexOutOfBoundsException {
        if (d < 3 && d > -3) {
            direction = d;
        }
        else throw new IndexOutOfBoundsException("Directional value out of bounds! (-2 to 2)");
    }

}
