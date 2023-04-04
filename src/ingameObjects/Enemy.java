package ingameObjects;

/** represents enemies, which are controlled by the computer, in the games level*/
public class Enemy extends Character implements Eatable{
    /**
     * The score the player gets for eating the Enemy.
     */
    private final int value = 200;
    /** used to loop colors of enemies in the level when using the constructor without the "color" option*/
    private static int colorLoop = 0;
    /** color of the sprite of the enemy,can be "yellow", "cyan", "red, "pink" */
    private String color;
    /**
     * initializes the Enemy.
     * @param position_x x Position of the Character in the level.
     * @param position_y y Position of the Character in the level.
     * @param direction direction the Character is moving in: 0: static, 1: right, 2: up, -1: left, -2: down
     * @param color sprite of the enemy is chosen, depending on color.
     */
    public Enemy(int position_x, int position_y, int direction, String color) {
        super(position_x, position_y, direction);
        chooseColor(color);
    }
    /**
     * initializes the Enemy, loops through the possible colors.
     * @param position_x x Position of the Character in the level.
     * @param position_y y Position of the Character in the level.
     * @param direction direction the Character is moving in - 0: static, 1: right, 2: up, -1: left, -2: down
     */
    public Enemy(int position_x, int position_y, int direction) {
        super(position_x, position_y, direction);
        switch(colorLoop){
            case 0:
                chooseColor("yellow");
                colorLoop++;
                break;
            case 1:
                chooseColor("cyan");
                colorLoop++;
                break;
            case 2:
                chooseColor("red");
                colorLoop++;
                break;
            default:
                chooseColor("pink");
                colorLoop = 0;
                break;
        }
    }

    /**
     * sets the color of the sprite of the enemy,can be "yellow", "cyan", "red, "pink".
     * @param color color of the sprite of the enemy,can be "yellow", "cyan", "red, "pink".
     */
    public void setColor(String color){
        this.color = color;
    }

    /**
     * returns the color of the sprite of the enemy,can be "yellow", "cyan", "red, "pink".
     * @return color of the sprite of the enemy,can be "yellow", "cyan", "red, "pink".
     */
    public String getColor(){
        return this.color;
    }

    /**
     * returns the score the player gets when eating this object.
     * @return the score the player gets when eating this object.
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * sets the sprite of the enemy according to the given color.
     * @param color String to choose the color of the enemy,can be "yellow", "cyan", "red, "pink"
     */
    private void chooseColor(String color) {
        switch (color) {
            case "yellow" -> {
                this.sprite = "/resources/yellowEnemy.gif";
                setColor(color);
            }
            case "pink" -> {
                this.sprite = "/resources/pinkEnemy.gif";
                setColor(color);
            }
            case "cyan" -> {
                this.sprite = "/resources/cyanEnemy.gif";
                setColor(color);
            }
            case "red" -> {
                this.sprite = "/resources/redEnemy.gif";
                setColor(color);
            }
        }
    }

}