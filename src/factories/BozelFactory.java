/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.Bozel;
import bozels.models.MainModel;

/**
 * @author Bart Middag
 */
public abstract class BozelFactory {
    protected final MainModel model;
    
    public BozelFactory(MainModel model) {
        this.model = model;
    }
    /**
     * Create a new Bozel.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param id the Bozel's id
     * @return The created Bozel object.
     */
    public abstract Bozel create(float x, float y, int id);
}