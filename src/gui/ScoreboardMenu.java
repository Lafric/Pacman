package gui;

import audio.AudioPlayer;
import scoreBoard.ScoreBoard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * GUI window, in which the scoreboard is shown.
 */
public class ScoreboardMenu extends JFrame{
    /** top panel of the GUI window*/
    private JPanel mainPanel;

    /** brings the user back to the main menu */
    private JButton backButton;

    /** table in which the best players and their scores are shown*/
    private JTable scoreboardTable;

    /** Box where the Scoreboard of a chosen gameMode can be selected */
    private JComboBox<String> modeSelectionBox;

    /** String constant which is used to select the scoreboard for the 1-player mode */
    private static final String onePlayer = "Singleplayer";

    /** String constant which is used to select the scoreboard for the 2-player mode */
    private static final String twoPlayers = "Multiplayer";

    /** sets up the GUI window and its event listeners*/
    public ScoreboardMenu(){
        //display settings
        setContentPane(mainPanel);
        setVisible(true);
        setTitle("Scoreboard");
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

        //create comboBox options
        modeSelectionBox.addItem(onePlayer);
        modeSelectionBox.addItem(twoPlayers);


        //tmp delete later

        /*
        ScoreBoard.writeEntry("name",4);
        ScoreBoard.writeEntry("name",4);
        ScoreBoard.writeEntry("name",4);
        ScoreBoard.writeEntry("name",7);
        ScoreBoard.writeEntry("name",1);
        ScoreBoard.writeEntry("name",2);
        ScoreBoard.writeEntry("name",4);
        ScoreBoard.writeEntry("name",1);
        ScoreBoard.writeEntry("name",2);
        ScoreBoard.writeEntry("name",4);

        ScoreBoard.writeEntry(new String[]{"name", "name2"},4);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},4);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},4);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},7);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},1);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},2);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},4);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},1);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},2);
        ScoreBoard.writeEntry(new String[]{"name", "name2"},15);
        */
        //ScoreBoard.writeEntry(new String[]{"10charlong", "10charlong"},20);
        //System.out.println(ScoreBoard.readScore(4));
        //System.out.println(ScoreBoard.readName(4));
        //ScoreBoard.removeEntry(0);
        //ScoreBoard.clearScoreBoardData();


        //show the scoreBoard table
        int gameMode = getGameModeInt((String) Objects.requireNonNull(modeSelectionBox.getSelectedItem()));
        showTable(gameMode);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AudioPlayer(Config.getMenuButtonSoundPath()).playAndClose();
                MainMenu.main(new String[0]);
                dispose();
            }
        });
        modeSelectionBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //show the scoreBoard table matching the selected gameMode
                int gameMode = getGameModeInt((String) Objects.requireNonNull(modeSelectionBox.getSelectedItem()));
                showTable(gameMode);
            }
        });
    }

    /**
     * returns the integer for the chosen gameMode.
     * @param mode the String for the chosen gameMode.
     * @return the integer for the chosen gameMode.
     */
    private int getGameModeInt(String mode){
        switch (mode) {
            case onePlayer -> {return 1;}
            case twoPlayers -> {return 2;}
            default -> throw new IllegalArgumentException("The selected gameMode is not supported");
        }
    }

    /**
     * fills the scoreBoard table according to the selected gameMode
     * @param gameMode The gameMode ( 1 = 1 player, 2 = 2-players,...) to which the scoreboard belongs.
     */
    private void showTable(int gameMode){
        // columns: Rank, score and player name columns
        int columns = 2 + gameMode;
        String[][] data = new String[ScoreBoard.getEntryCount(gameMode)][columns];

        // set captions for the columns of the table
        //number of columns is dependent on the amount of players
        String[] tableColumns = new String[2+gameMode];
        tableColumns[0] = "Rank";
        tableColumns[1] = "Score";
        if(gameMode == 1) {
            tableColumns[2] = "Player";
        }
        else {
            for (int i = 2; i < columns; i++) {
                tableColumns[i] = "Player" + (i - 1);
            }
        }


        // pull table data from xml file
        for (int i = 0; i < 10 && i < ScoreBoard.getEntryCount(gameMode); i++) {
            //data[i] = new String[]{Integer.toString(i+1), Integer.toString(ScoreBoard.readScore(i, gameMode)), ScoreBoard.readName(i, gameMode)};

            data[i] = new String[columns];
            data[i][0] = Integer.toString(i+1);
            data[i][1] = Integer.toString(ScoreBoard.readScore(i, gameMode));
            //copy all the names into the according columns
            String[] names = ScoreBoard.readNames(i, gameMode);
            System.arraycopy(names, 0, data[i], 2, names.length);
        }

        // insert table data into table
        DefaultTableModel model = new DefaultTableModel(data, tableColumns);
        scoreboardTable.setModel(model);

        // center table entries
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i < columns; i++){
            scoreboardTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        //scoreboardTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        //scoreboardTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        //scoreboardTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }
}
