/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Wooden Block Model
 * @author Bart Middag
 */
public class WoodenBlockModel extends ElementModel {

    public WoodenBlockModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 0.75f;
        restitution = 0.3f;
        friction = 0.8f;
        color = new Color(175,100,50);
        powerThreshold = 10.0f;
        strength = 12.0f;
        fragile = true;
        fireStateChanged();
    }   
}
