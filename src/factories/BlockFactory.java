/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.Block;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public abstract class BlockFactory {
    protected final MainModel model;
    
    public BlockFactory(MainModel model) {
        this.model = model;
    }
    
    /**
     * Create a new Block.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the block
     * @param height the height of the block
     * @param angle the angle of the block
     * @param ellipse is this block an ellipse?
     * @return the created Block object
     */
    public abstract Block create(float x, float y, float width, float height, float angle, boolean ellipse);
}