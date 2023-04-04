package gui;

import ingameObjects.*;
import level.*;

import javax.swing.*;

/**
 * provides functions to paint IngameObjects.
 */
public class Painter {
    /**
     * paints the IngameObjects of the level(walls, PowerUps, ScorePoints, Fruits, Enemies, HungyBalls).
     * Painting is done in an event-dispatch-Thread.
     * @param l Level in which the Objects are placed.
     * @param pp GUI where the Objects are to be painted.
     */
    public static void paintLevelObects(Level l, PaintPanel pp) {
        //SwingUtilities.invokeLater(new Runnable() {
        Runnable toRun = new Runnable() {
            public void run() {
                //clear list
                // could cause concurrentModificationException, if done outside this thread
                // (when clearing list while the event dispatch thread paints the objects in the list)
                pp.clearObjectstoPaint();
                //List<List<List<IngameObject>>> objectMatrix = l.getIngameObjectsDeepCopy();

                for (int row = 0; row < l.getHeight(); row++) {
                    for (int col = 0; col < l.getWidth(); col++) {
                        for(IngameObject o : l.getIngameObjectsOnPosition(col, row)){
                            //puts Object that are to be painted in a list,
                            pp.addObjectstoPaint(o);
                        }
                    }
                }

        //SwingUtilities.invokeAndWait(new Runnable() {
        //not necessary to wait
        //SwingUtilities.invokeLater(new Runnable() {
        //    public void run() {
                pp.repaint();
            }
        };

        //run toRun in EventDispatchThread (invokeLater only if not already in EDT)
        if(SwingUtilities.isEventDispatchThread()){
            toRun.run();
        }
        else {
            SwingUtilities.invokeLater(toRun);
        }
    }
}
