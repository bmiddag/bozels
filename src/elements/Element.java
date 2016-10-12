/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import java.awt.Graphics;

/**
 *
 * @author Bart Middag
 */
public interface Element {
    /**
     * Initiate this element.
     */
    public void init();
    
    /**
     * Update this element.
     */
    public void update();
    
    /**
     * Register this element with the models.
     */
    public void register();
    
    /**
     * Unregister this element with the models and destroy it.
     */
    public void unregister();
    
    /**
     * Is this element awake?
     * @return true if the element is awake, false if it's asleep.
     */
    public boolean isAwake();
    
    /**
     * Get the speed vector's X coordinate.
     * @return awake
     */
    public float getSpeedX();
    
    /**
     * Get the speed vector's Y coordinate.
     * @return the speed x
     */
    public float getSpeedY();
    
    /**
     * Get the speed vector's length.
     * @return the speed y
     */
    public float getSpeed();
    
    /**
     * Get this element's X coordinate.
     * @return x
     */
    public float getX();
    
    /**
     * Get this element's Y coordinate.
     * @return y
     */
    public float getY();
    
    /**
     * Get this element's angle.
     * @return the angle
     */
    public float getAngle();
    
    /**
     * Draw this element.
     * @param g the graphics context
     * @param showScenery draw this element in its dark color
     */
    public void draw(Graphics g, boolean showScenery);
    
    /**
     * Add a force to this element.
     * @param force the force to add to this element
     */
    public void addBreakForce(float force);
    
    
}
