package ingameObjects;

/** Interface for objects that grant the player score when eaten (like fruits ,enemies and scorepoints) */
public interface Eatable {

    /**
     * returns the score the player gets when eating this object.
     * @return the score the player gets when eating this object.
     */
    int getValue();

    /**
     * returns the x-coordinate of the object placed in the level.
     * @return the x-coordinate of the object placed in the level.
     */
    int getPosition_x();
    /**
     * returns the y-coordinate of the object placed in the level.
     * @return the y-coordinate of the object placed in the level.
     */
    int getPosition_y();
}
