package gui;

import level.Level;

import java.awt.*;


/**
 * a Panel which is meant to be a non-playable preview of the level.
 */
public class LevelPreviewPanel extends PaintPanel {

    /**
     * creates the preview panel.
     * @param blockSize pixel size of the blocks for the level in the level-preview.
     */
    public LevelPreviewPanel(int blockSize){
        this.blockSize = blockSize;

        //this.setLayout(new GridLayoutManager(1,1));
    }

    /**
     * paints the preview panel
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g) {
        //System.out.println("paintComp");
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());


        //paint Objects of level
        Toolkit t = Toolkit.getDefaultToolkit();

        //casting is safe
        //https://docs.oracle.com/javase/tutorial/2d/overview/rendering.html
        Graphics2D g2 = (Graphics2D) g;


        paintObjectstoPaint(t, g2);
    }

    /**
     * sets the minimum + preferredSize of the panel according to the size of the displayed level.
     * @param l level which is to be displayed.
     */
    public void setSize(Level l){
        int sizeX = l.getWidth() * blockSize;
        int sizeY = l.getHeight() * blockSize;

        Dimension d = new Dimension(sizeX, sizeY);
        this.setMinimumSize(d);
        this.setPreferredSize(d);

    }
}
