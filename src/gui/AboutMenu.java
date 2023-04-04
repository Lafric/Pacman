package gui;

import audio.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI window, in which further information about the game is shown.
 */
public class AboutMenu extends JFrame{
    /** container for the level-preview. can be used to scroll through level-preview, if it is too big to be shown */
    private JButton backButton;
    /** top panel of the GUI window*/
    private JPanel mainPanel;

    /**
     * creates and displays the AboutMenu.
     */
    public AboutMenu(){
        //display settings
        setContentPane(mainPanel);
        setVisible(true);
        setTitle("About");
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

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();
                MainMenu.main(new String[0]);
                dispose();
            }
        });
    }
}
