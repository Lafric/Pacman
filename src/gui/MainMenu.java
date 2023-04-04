package gui;


import audio.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * first GUI window, in which the user has several options to choose from.
 */
public class MainMenu extends JFrame{
    /** top panel of the GUI window*/
    private JPanel mainPanel;
    /** brings the user to the gui window to set up a singleplayer game */
    private JButton singleplayerButton;
    /** brings the user to the gui window that shows the scoreboard */
    private JButton highscoreButton;
    /** brings the user to the gui window to set up a multiplayer game */
    private JButton multiplayerButton;
    /** brings the user to the gui window that shows information about the game and its developers */
    private JButton aboutButton;
    /** panel in which the caption label is placed*/
    private JPanel labelPanel;

    /**
     * initialises Listeners of the MainMenu.
     */
    public MainMenu() {
        singleplayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();
                new GameSettingsMenu(false);
                dispose();
            }
        });
        highscoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();
                new ScoreboardMenu();
                dispose();
            }
        });
        multiplayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();
                new GameSettingsMenu(true);
                dispose();
            }
        });
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();
                new AboutMenu();
                dispose();
            }
        });
    }

    /**
     * starts the application and creates the main menu.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        MainMenu mm = new MainMenu();
        mm.setContentPane(mm.mainPanel);
        mm.setTitle("Hungry-Ball");
        mm.setSize(400,400);

        //set icon in the titlebar
        Image icon = Toolkit.getDefaultToolkit().getImage(MainMenu.class.getResource(Config.getIconPath()));
        mm.setIconImage(icon);

        //place window in the center of the screen
        mm.setLocationRelativeTo(null);
        //close application when using close button
        //mm.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mm.labelPanel.setOpaque(false);
        mm.mainPanel.setBackground(Color.lightGray);

        mm.setVisible(true);
    }
}
