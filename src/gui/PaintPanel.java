package gui;

import ingameObjects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * represents a panel, on which a level can be painted.
 */
public abstract class PaintPanel extends JPanel {
    /** pixel size (width / height) of the individual tiles in the level */
    protected int blockSize;
    /**
     * holds objects which are to be painted.
     */
    protected final List<IngameObject> ObjectstoPaint = new ArrayList<>();

    /**
     * returns a transformation matrix for the sprite of Ingameobject o.
     * @param o the object for which the sprite shall be transformed.
     * @param anchorx x-coordinate of the rotation point/axis.
     * @param anchory y-coordinate of the rotation point/axis.
     * @return transformation matrix for the sprite of Ingameobject o.
     */
    protected AffineTransform getSpriteTransformation(IngameObject o, int anchorx, int anchory) {
        AffineTransform tr = new AffineTransform(); //identity

        int quadrantRotation = o.getSpriteQuadrantRotation();
        boolean isMirrored = o.getSpriteMirrored();

        if(quadrantRotation != 0){
            tr.setToQuadrantRotation(quadrantRotation,anchorx, anchory);
        }
        if(isMirrored){
            tr.scale(-1,1);
            tr.translate(-2*anchorx,0);
        }

        return tr;
    }

    /**
     * returns the pixel size (width / height) of the individual tiles in the level.
     * @return pixel size (width / height) of  the individual tiles in the level.
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * puts an Object that is to be painted in a list, the object will be painted on repaint.
     * @param o object that is to be painted.
     */
    public void addObjectstoPaint(IngameObject o){
        ObjectstoPaint.add(o);
    }

    /**
     * clears the list of Objects that are to be painted.
     */
    public void clearObjectstoPaint(){
        ObjectstoPaint.clear();
    }

    /**
     * paints the objects in the ObjectstoPaint list.
     * @param t needed to load images.
     * @param g2 allows to draw the content.
     */
    protected void paintObjectstoPaint(Toolkit t, Graphics2D g2){
        ObjectstoPaint.sort((o1, o2) -> Integer.compare(getPaintPrecedence(o2), getPaintPrecedence(o1)));

        for (IngameObject o : ObjectstoPaint) {
            if(o!=null) {
                Image sprite;
                try {
                    sprite = t.getImage(getClass().getResource(o.getSprite()));
                } catch (NullPointerException e) {
                    //try absolute path (Hungryball gif is a tmp file to which the path is absolute)
                    sprite = t.getImage(o.getSprite());
                    //sprite = ((HungryBall) o).getSprite2();
                }


                int sprite_location_y = o.getPosition_y() * blockSize;
                int sprite_location_x = o.getPosition_x() * blockSize;

                //wallBlockSize should better be multiple of 2
                AffineTransform tr = getSpriteTransformation(o, sprite_location_x + blockSize /2, sprite_location_y + blockSize /2);
                // Get the current transform
                AffineTransform saved = g2.getTransform();
                // Perform transformation
                g2.transform(tr);
                // Render
                g2.drawImage(sprite, sprite_location_x, sprite_location_y, this.blockSize, this.blockSize, this);
                // Restore original transform
                g2.setTransform(saved);

            }
        }
    }

    /**
     * returns the paint-precedence of an IngameObject.
     * <p>
     * The paint-precedence defines the order in which the objects sprite should be painted.
     * 1 is the highest precedence, Integer.MAX_VALUE the lowest.
     * <p>
     * E.g. HungryBall Objects have the highest precedence,
     * therefore they are always in the foreground when multiple objects are painted on one position.
     *
     * @param o the IngameObject to get the paint-precedence from.
     * @return paint-precedence of an IngameObject
     */
    private int getPaintPrecedence(IngameObject o) {
        if (o instanceof HungryBall) {
            return 1;
        } else if (o instanceof Enemy) {
            return 2;
        } else if (o instanceof PowerUp) {
            return 3;
        } else if (o instanceof Cherry) {
            return 4;
        } else if (o instanceof ScorePoint) {
            return 5;
        } else if (o instanceof Wall) {
            return 6;
        } else {
            //default: other objects have the lowest precedence
            return Integer.MAX_VALUE;
        }
    }
}
