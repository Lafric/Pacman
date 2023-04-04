package gui;

import audio.AudioPlayer;
import game.Game;
import level.CustomLevel;
import level.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * GUI window, in which the user can choose options and set up a Game of Hungryball.
 */
public class GameSettingsMenu extends JFrame {
    /** starts the game */
    public JButton startGameButton;
    /** top panel of the GUI window */
    private JPanel mainPanel;
    /** brings the user back to the main menu */
    private JButton backButton;
    /** container for the level-preview. can be used to scroll through level-preview, if it is too big to be shown */
    private JScrollPane levelScrollPane;
    /** can be used to select a level/map to be played */
    private JComboBox<String> levelSelectionBox;
    /** containter for the level selection */
    private JPanel outerLevelPanel;
    /** contains the star button */
    private JPanel startButtonPanel;
    /** Button to set the standard movement behaviour
     * (Press a key once to move in the according direction until a wall is hit or the direction is changed.)
     */
    private JRadioButton standardRadioButton;
    /** Button to set a second movement behaviour
     * (Press a key every turn in order to move in the according direction.)
     */
    private JRadioButton behaviourBRadioButton;
    /** used to set the speed of the game / the delay for updating the game */
    private JSlider gameSpeedSlider;
    /** used to set the speed of the animation for the mouth of the hungryball/s */
    private JSlider mouthSpeedSlider;
    /** used to set the lives of the player/s*/
    private JSlider livesSlider;
    /** the panel which shows the level-preview */
    private final LevelPreviewPanel levelPreviewPanel;

    /**
     * creates and displays the GameSettingsMenu.
     * @param multiplayer true if a multiplayer game is to be set up, false if singleplayer is the gamemode.
     */
    public GameSettingsMenu(boolean multiplayer) {

        //display settings
        setContentPane(mainPanel);
        setVisible(true);
        setTitle("Game Settings");
        setSize(600,500);
        //place window in the center of the screen
        setLocationRelativeTo(null);
        //close application when using close button
        //mm.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //set icon in the titlebar
        Image icon = Toolkit.getDefaultToolkit().getImage(MainMenu.class.getResource(Config.getIconPath()));
        setIconImage(icon);

        mainPanel.setBackground(Color.lightGray);
        startButtonPanel.setOpaque(false);

        //add available Levels to the selection box
        createLevelSelectionEntries();

        levelPreviewPanel = new LevelPreviewPanel(10);
        //use the scrollpane to display the previewPanel, in case the preview image gets too big to be shown.
        levelScrollPane.setViewportView(levelPreviewPanel);
        //paint preview of the selected level
        paintPreviewWindow();

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //start game in new (non event-dispatching) Thread
                //System.out.println(Thread.currentThread());
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();
                new Thread(new Runnable() {
                    public void run() {


                        //System.out.println(Thread.currentThread());
                        Level l = new CustomLevel((String) levelSelectionBox.getSelectedItem());

                        int animationDelay = calculateAnimationDelay(mouthSpeedSlider.getValue(), mouthSpeedSlider.getMaximum());
                        int gameUpdateTime = calculateGameUpdateTime(gameSpeedSlider.getValue(), gameSpeedSlider.getMaximum());

                        //standard case: 0 (standard behaviour is selected)
                        int movementBehaviour = 0;
                        if(behaviourBRadioButton.isSelected()) {
                            movementBehaviour = 1;
                        }

                        if(!multiplayer){
                            Game game = new Game("Player1", l, livesSlider.getValue(),
                                   animationDelay,gameUpdateTime, movementBehaviour);
                            game.startGame();
                        } else {
                            Game game = new Game("Player1", "Player2", l,
                                    livesSlider.getValue(), animationDelay, gameUpdateTime, movementBehaviour);
                            game.startGame();
                        }
                    }
                }).start();
                dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();
                MainMenu.main(new String[0]);
                dispose();
            }
        });
        levelSelectionBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPreviewWindow();
            }
        });
    }


    /**
     * create a preview image of the level in the scrollPane, based on which entry is selected in the comboBox.
     */
    private void paintPreviewWindow(){
        Level l = new CustomLevel((String) levelSelectionBox.getSelectedItem());
        levelPreviewPanel.setSize(l);
        Painter.paintLevelObects(l,levelPreviewPanel);


        //levelScrollPane.setSize(levelPreviewPanel.getPreferredSize());
        //levelScrollPane.setPreferredSize(levelPreviewPanel.getPreferredSize());


        levelPreviewPanel.revalidate();
        //levelScrollPane.getViewport().setPreferredSize(levelPreviewPanel.getPreferredSize());

        levelScrollPane.revalidate();
        outerLevelPanel.revalidate();
    }

    /**
     * searches for the available maps in the customMaps.xml file
     * and adds the names of the maps to the levelSelection comboBox
     * @throws RuntimeException e
     */
    private void createLevelSelectionEntries() {
        //go through all the custom levels in the xml file

        //read xml file, using the DOM parser
        //creates the documentBuilder object
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            //create the documentBuilder in order to parse the file
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(Objects.requireNonNull(getClass().getResource("/resources/customMaps.xml")).toString());


            Element maps = (Element) document.getElementsByTagName("maps").item(0);
            //get all tags within the maps tag
            for(int i = 0; i<maps.getElementsByTagName("*").getLength(); i++){
                String mapName = maps.getElementsByTagName("*").item(i).getNodeName();
                levelSelectionBox.addItem(mapName);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * calculates a delay for the mouth animation of the hungryball (linear function).
     * @param mouthSpeed a value for the speed of the animation of the hungryballs.
     * @param maxValue the largest possible value the user can set with the slider.
     * @return delay [in ms] between 2 frames of the animation.
     */
    private static int calculateAnimationDelay(int mouthSpeed, int maxValue){
        // use a linear function
        //System.out.println(mouthSpeed);
        //System.out.println(100 + (maxValue - mouthSpeed) * 20);
        return 100 + (maxValue - mouthSpeed) * 20;
    }
    /**
     * calculates a time/delay for the game to update its state (linear function).
     * @param gameSpeed a value for the speed of the game.
     * @param maxValue the largest possible value the user can set with the slider.
     * @return delay [in ms] between 2 frames of the animation.
     */
    private static int calculateGameUpdateTime(int gameSpeed, int maxValue){
        // use a linear function
        //System.out.println(200 + (maxValue - gameSpeed) * 80);
        return 200 + (maxValue - gameSpeed) * 80;
    }
}
