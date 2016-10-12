/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Red Bozel Model
 * @author Bart Middag
 */
public class RedBozelModel extends ElementModel {

    public RedBozelModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 10.0f;
        restitution = 0.3f;
        friction = 0.9f;
        color = Color.RED;
        powerThreshold = 0f;
        strength = 0f;
        fragile = false;
        fireStateChanged();
    }   
}
