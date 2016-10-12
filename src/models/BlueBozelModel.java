/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Blue Bozel Model
 * @author Bart Middag
 */
public class BlueBozelModel extends ElementModel {

    public BlueBozelModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 8.0f;
        restitution = 0.7f;
        friction = 1.0f;
        color = Color.BLUE;
        powerThreshold = 0f;
        strength = 0f;
        fragile = false;
        fireStateChanged();
    }   
}
