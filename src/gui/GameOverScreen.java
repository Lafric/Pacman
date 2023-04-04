package gui;

import audio.AudioPlayer;
import scoreBoard.ScoreBoard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI window, in which the player's final score is shown,
 * as well as a list of high scores and the option to enter a name if the player has achieved a high enough score.
 */
public class GameOverScreen extends JFrame{

    /** top panel of the GUI window*/
    private JPanel mainPanel;

    /** label to provide or show the score of the current user after a gameOver*/
    private JLabel showScore;

    /** label that shows the current rank of the player/s in the scoreboard */
    private JLabel newHighScore;

    /** field, where the new HighScorer must enter his name*/
    private JTextField textField1;

    /** Back to main menu button */
    private JButton button1;

    /** indicate that the player has to enter his name */
    private JLabel pleaseEnterName;

    /** table, where name and highScore will be display*/
    private JTable table1;

    /** scrollpane which contains the table for the scoreboard */
    private JScrollPane pane1;

    /** shows "Highscore as text" */
    private JLabel highscores;

    /**
     * creates and displays the GameOverScreen.
     * @param score points/score which the player/s has achieved in the game.
     * @param gameMode The gameMode ( 1 = 1 player, 2 = 2-players,...) to which the scoreboard belongs.
     */
    public GameOverScreen(int score, int gameMode){
        //display settings
        setContentPane(mainPanel);
        setVisible(true);
        setTitle("Game Over!");
        setSize(600,500);
        //place window in the center of the screen
        setLocationRelativeTo(null);
        //close application when using close button
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //set icon in the titlebar
        Image icon = Toolkit.getDefaultToolkit().getImage(MainMenu.class.getResource(Config.getIconPath()));
        setIconImage(icon);
        mainPanel.setBackground(Color.lightGray);

        showScore.setText("Your Score: "+ score);

        String[] namesToWrite = new String[gameMode];
        final int[] currentPlayer = {0};

        /*
        // tmp
        ScoreBoard.clearScoreBoardData();
        ScoreBoard.writeEntry("a", 10);
        ScoreBoard.writeEntry("b", 20);
        ScoreBoard.writeEntry("c", 30);
        ScoreBoard.writeEntry("d", 40);
        ScoreBoard.writeEntry("e", 50);
        ScoreBoard.writeEntry("f", 60);
        ScoreBoard.writeEntry("g", 70);
        ScoreBoard.writeEntry("h", 80);
        ScoreBoard.writeEntry("i", 90);
        ScoreBoard.writeEntry("j", 100);
         */

        //String[] columns = {"Rank", "Score", "Player"};

        //number of columns is dependent on the amount of players
        String[] columns = new String[2+gameMode];
        columns[0] = "Rank";
        columns[1] = "Score";
        if(gameMode == 1) {
            columns[2] = "Player";
        }
        else {
            for (int i = 2; i < columns.length; i++) {
                columns[i] = "Player" + (i - 1);
            }
        }


        String[][] data = new  String[Integer.min(11, ScoreBoard.getEntryCount(gameMode)+1)][columns.length];

        // player not in top10 by default
        int playerRank = 10;
        int offset = 0;

        // check if scoreboard is empty
        if (ScoreBoard.getEntryCount(gameMode) == 0) {
            data[0] = new String[]{"1", Integer.toString(score), ""};
            playerRank = 0;
        }
        else {
            // fill scoreboard
            for (int i = 0; i < 10 && i < ScoreBoard.getEntryCount(gameMode); i++) {
                if (score > ScoreBoard.readScore(i, gameMode) && offset == 0) {
                    data[i] = new String[]{Integer.toString(i+1), Integer.toString(score), ""};
                    playerRank = i;
                    offset = 1;
                }

                //data[i + offset] = new String[]{Integer.toString(i+1 + offset), Integer.toString(ScoreBoard.readScore(i, gameMode)), ScoreBoard.readName(i, gameMode)};

                data[i + offset] = new String[columns.length];
                data[i + offset][0] = Integer.toString(i+1 + offset);
                data[i + offset][1] = Integer.toString(ScoreBoard.readScore(i, gameMode));
                //copy all the names into the according columns
                String[] names = ScoreBoard.readNames(i, gameMode);
                System.arraycopy(names, 0, data[i + offset], 2, names.length);


            }
            // player comes in last place, but the leaderboard not yet fully populated
            if (offset == 0 && ScoreBoard.getEntryCount(gameMode) < 10) {
                data[ScoreBoard.getEntryCount(gameMode)] = new String[]{Integer.toString(ScoreBoard.getEntryCount(gameMode)+1), Integer.toString(score), ""};
                playerRank = ScoreBoard.getEntryCount(gameMode);
            }
            // player not in top 10
            if (playerRank > 9) {
                data[10] = new String[]{"-", Integer.toString(score), ""};
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columns);
        table1.setModel(model);
        // center table entries
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for(int i = 0; i < columns.length; i++){
            table1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }


        // if player score is high enough to place on the scoreboard
        if (playerRank < 10) {
        // allow player to enter a name that goes on the scoreboard
            newHighScore.setVisible(true);
            newHighScore.setText("Congratulations! You are rank "+ (playerRank+1) +" on the leaderboard!");
            pleaseEnterName.setVisible(true);
            textField1.setVisible(true);
            if (gameMode > 1) {
                pleaseEnterName.setText("Player1, please enter your name below. (character limit: 10, no spaces allowed)");
                button1.setText("Confirm Player1 name");
            }
            else {
                button1.setText("Confirm and back to Main Menu");
            }
        }

        int finalPlayerRank = playerRank;

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();

                if (finalPlayerRank < 10) {
                    // check if input size is correct
                    if (!textField1.getText().isEmpty() && textField1.getText().length() <= 10 && !textField1.getText().contains(" ")) {
                        namesToWrite[currentPlayer[0]] = textField1.getText();
                        //System.out.println(textField1.getText());
                        textField1.setText("");     // clear text field
                        currentPlayer[0]++;
                        pleaseEnterName.setText("Thank you. Player"+ (currentPlayer[0] +1) +", please enter your name below. (character limit: 10, no spaces allowed)");
                        button1.setText("Confirm Player" + (currentPlayer[0] +1) +" name");

                        // save scores once all players have entered their names
                        if (currentPlayer[0] == gameMode) {
                            ScoreBoard.writeEntry(namesToWrite, score);
                            MainMenu.main(new String[0]);
                            dispose();
                        }
                    } else {
                        //show dialog window, if the input size is incorrect
                        JOptionPane.showMessageDialog(null,
                                "The submitted name must be at least 1 and at most 10 characters long and is not allowed to contain any spaces.",
                                "Please enter your name!",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                else {
                    MainMenu.main(new String[0]);
                    dispose();
                }
            }
        });
    }
}