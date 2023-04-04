package gui;

import ingameObjects.*;
import game.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * represent the Gui Class for the Game itself
 */
public class IngameGui extends PaintPanel{

    /** the game which the ingameGUI window shows */
    private final Game game;

    /** horizontal screen size in pixels */
    private int screenSizeX;

    /** vertical screen size in pixels */
    private int screenSizeY;

    /**
     * Initialises the window size ect. and adds the listener for proper resizing of the window.
     * @param levelSize_x horizontal size of the level (in blocks /tiles).
     * @param levelSize_y vertical size of the level (in blocks /tiles).
     * @param game the game which the ingameGUI window shows.
     */
    public IngameGui(int levelSize_x, int levelSize_y, Game game){

        this.blockSize = 30;

        //this.levelSize_x = levelSize_x;
        //this.levelSize_y = levelSize_y;
        this.game = game;

        screenSizeX = levelSize_x  * blockSize;
        screenSizeY = (levelSize_y + 2)  * blockSize;   // 2 additional rows for HUD

        //add listener to resize the window properly
        // and wallblocksize + screensize need to not be final
        this.addComponentListener(new ResizeAdapter(levelSize_y, levelSize_x));
    }

    /**
     * paints the game itself
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());


        //paint Objects of level
        Toolkit t=Toolkit.getDefaultToolkit();

        //casting is safe
        //https://docs.oracle.com/javase/tutorial/2d/overview/rendering.html
        Graphics2D g2 = (Graphics2D)g;

        paintObjectstoPaint(t, g2);


        // draw HUD (score and remaining lives)
        g.setColor(Color.white);

        //set font
        int fontSize = blockSize /2;
        Font f = new Font("Times New Roman", Font.PLAIN, fontSize);
        g.setFont(f);

        HungryBall player1 = game.getPlayer1();
        HungryBall player2 = game.getPlayer2();

        if(player2 == null) {
            g.setFont(new Font("Times New Roman", Font.PLAIN, blockSize*3/4));
            g.drawString(player1.name +":   LIVES "+ player1.getLives() +"   SCORE "+ player1.getScore(), 5, getHeight() - blockSize + fontSize/2);
        }
        else {
            g.drawString(player1.name +":   LIVES "+ player1.getLives() +"   SCORE "+ player1.getScore(), 5, getHeight() - blockSize*3/2 + fontSize/2);
            g.drawString(player2.name +":   LIVES "+ player2.getLives() +"   SCORE "+ player2.getScore(), 5, getHeight() - blockSize/2 + fontSize/2);
            g.setFont(new Font("Times New Roman", Font.PLAIN, blockSize*3/4));
            g.drawString("TOTAL SCORE: "+ (player1.getScore() + player2.getScore()), getWidth()/2, getHeight() - blockSize + fontSize/2);
        }
    }

    /**
     * used as ComponentListener, which is needed to resize the window properly.
     */
    private class ResizeAdapter extends ComponentAdapter{
        /** vertical size of the level (in blocks /tiles). */
        private final int levelSize_y;
        /** horizontal size of the level (in blocks /tiles). */
        private final int levelSize_x;
        /** timer is used to perform actions when resizing is finished (only after a delay) */
        Timer resizeTimer;

        /**
         * initialises the resizeAdapter.
         * @param levelSize_x horizontal size of the level (in blocks /tiles).
         * @param levelSize_y vertical size of the level (in blocks /tiles).
         */
        public ResizeAdapter(int levelSize_y, int levelSize_x){
            this.levelSize_y = levelSize_y;
            this.levelSize_x = levelSize_x;
        }

        /**
         * event listener for proper resizing of the window.
         * @param e the event to be processed
         */
        public void componentResized(ComponentEvent e) {
            //float aspect_ratio = getSize().height / getSize().width;

            //This Method is run several times when resizing the window
            // therefore a timer is used to perform the actions when resizing is finished (only after a delay),
            //(further componentResizedEvents reset the timer, so that only the last one performs the transformation)
            // idea is from https://stackoverflow.com/questions/69803367/capture-jframe-resize-only-once-when-mouse-is-released
            if (resizeTimer == null) {
                resizeTimer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        int height = getSize().height;
                        int width = getSize().width;

                        if (height <= width) {
                            //retain aspect ratio of window
                            blockSize = height / (levelSize_y + 2);
                        } else {
                            //retain aspect ratio of window
                            blockSize = width / levelSize_x;
                        }


                        screenSizeX = levelSize_x * blockSize;
                        screenSizeY = (levelSize_y+2) * blockSize;


                        JFrame f = (JFrame) SwingUtilities.getWindowAncestor(IngameGui.this);

                        IngameGui.this.setPreferredSize(new Dimension(screenSizeX, screenSizeY));
                        IngameGui.this.setMaximumSize(new Dimension(screenSizeX, screenSizeY));
                        IngameGui.this.setMinimumSize(new Dimension(screenSizeX, screenSizeY));
                        f.pack();

                    }
                });
                //timer only fires the first time and stops afterwards
                resizeTimer.setRepeats(false);
            }
            //reset the timer
            resizeTimer.restart();

        }
    }

    /**
     * returns the horizontal screen size in pixels.
     * @return horizontal screen size in pixels.
     */
    public int getScreenSizeX() {
        return screenSizeX;
    }

    /**
     * returns the vertical screen size in pixels.
     * @return vertical screen size in pixels.
     */
    public int getScreenSizeY() {
        return screenSizeY;
    }


/*
//does not work as intended (when clicking on the edge of the frame to resize)
    public void addListener(){
        JFrame f = (JFrame) SwingUtilities.getWindowAncestor(this);
        f.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("released");

                //float aspect_ratio = (levelSize_y+1) / (float)levelSize_x;

                //System.out.println(getSize().height);
                //System.out.println(getSize().width);
                int height = getSize().height;
                int width = getSize().width;
                //System.out.println(getSize().height/(float)width);
                //System.out.println(aspect_ratio);
                //if(Float.compare(aspect_ratio, height/(float)width)!=0){
                if (height <= width) {
                    //retain aspect ratio of window
                    wallBlockSize = height / (levelSize_y + 1);
                } else {
                    //retain aspect ratio of window
                    wallBlockSize = width / levelSize_x;
                }
                //System.out.println("wallblocks");
                //System.out.println(Wall_Block_Size);

                screenSizeX = levelSize_x * wallBlockSize;
                screenSizeY = (levelSize_y+1) * wallBlockSize;
                //System.out.println("screensize");
                //System.out.println(screenSizeY);
                //System.out.println(screenSizeX);


                JFrame f = (JFrame) SwingUtilities.getWindowAncestor(IngameGui.this);

                //if(Float.compare(aspect_ratio, Screen_Size_y/(float)Screen_Size_x)!=0){
                //f.setSize(Screen_Size_x, Screen_Size_y);
                IngameGui.this.setPreferredSize(new Dimension(screenSizeX, screenSizeY));
                IngameGui.this.setMaximumSize(new Dimension(screenSizeX, screenSizeY));
                IngameGui.this.setMinimumSize(new Dimension(screenSizeX, screenSizeY));
                f.pack();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
 */


}
