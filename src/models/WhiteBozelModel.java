/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The White Bozel Model
 * @author Bart Middag
 */
public class WhiteBozelModel extends ElementModel {

    public WhiteBozelModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 5.0f;
        restitution = 0.0f;
        friction = 0.2f;
        color = Color.GRAY;
        powerThreshold = 0f;
        strength = 0f;
        fragile = false;
        fireStateChanged();
    }   
}
