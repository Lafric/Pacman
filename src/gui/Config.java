package gui;

/** class that is meant for additional settings of the GUI classes */
public final class Config {
    /** location of the icon for the GUI */
    private static final String iconPath = "/resources/HungryBallIcon6.png";
    /** location of the sound effect for pressing a button in the menu */
    private static final String menuButtonSoundPath = "/resources/button-click.au";

    /**
     * returns the path to the icon for the GUI.
     * @return path to the icon for the GUI.
     */
    public static String getIconPath() {
        return iconPath;
    }

    /**
     * returns the path to the audio file, used for the sound effect for pressing a button in the menu.
     * @return path to the button-click audio file.
     */
    public static String getMenuButtonSoundPath() {
        return menuButtonSoundPath;
    }


}
