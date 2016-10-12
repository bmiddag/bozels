/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.Target;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public abstract class TargetFactory {
    protected final MainModel model;
    
    public TargetFactory(MainModel model) {
        this.model = model;
    }
    
    /**
     * Create a new Target.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the created Target object
     */
    public abstract Target create(float x, float y);
}