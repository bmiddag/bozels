/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Small Target Model
 * @author Bart Middag
 */
public class SmallTargetModel extends ElementModel {

    public SmallTargetModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 1.0f;
        restitution = 0.1f;
        friction = 0.9f;
        color = Color.GREEN;
        powerThreshold = 7.0f;
        strength = 10.0f;
        fragile = true;
        fireStateChanged();
    }   
}
