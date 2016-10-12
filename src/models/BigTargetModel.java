/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Big Target Model
 * @author Bart Middag
 */
public class BigTargetModel extends ElementModel {

    public BigTargetModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 1.0f;
        restitution = 0.1f;
        friction = 0.9f;
        color = Color.PINK;
        powerThreshold = 5.0f;
        strength = 10.0f;
        fragile = true;
        fireStateChanged();
    }   
}
