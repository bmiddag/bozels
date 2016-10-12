/* SquareIcon.java
 * ============================================================
 * Copyright (C) 2001-2012 Ghent University
 * 
 * An example used in the 'Programming 2' course.
 * 
 * Authors: Kris Coolsaet & Bart Middag
 */

package bozels.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * Simple square picture
 * @author Bart Middag
 */
public class SquareIcon implements Icon {

    /**
     * size of the icon
     */
    private final int size;
    
    /**
     * color of the icon
     */
    private Color color;
   
    /**
     * Create a square icon
     * @param size the size of the icon
     * @param color the color of the icon
     */
    public SquareIcon (int size, Color color) {
        this.size = size;
        this.color = color;
    }
    
    /**
     * How far the icon should be from the side
     */
    private static final int MARGIN = 2;
    
    /**
     * Draw the icon
     * @param c the component it should be drawn in
     * @param g the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     */
    @Override
    public void paintIcon (Component c, Graphics g, int x, int y) {
        g.setColor (color);
        g.fillRect (x+MARGIN, y+MARGIN, size-2*MARGIN, size-2*MARGIN);
    }
    
    /**
     * Get the width of the icon
     * @return the width of the icon
     */
    @Override
    public int getIconWidth () {
        return size;
    }
    
    /**
     * Set a new color to the icon
     * @param color the new color of the icon
     */
    public void setColor(Color color) {
        if(this.color != color) {
            this.color = color;
        }
    }
    
    /**
     * Get the height of the icon
     * @return the height of the icon
     */
    @Override
    public int getIconHeight () {
        return size;
    }
}
