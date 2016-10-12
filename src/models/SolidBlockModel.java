/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Solid Block Model
 * @author Bart Middag
 */
public class SolidBlockModel extends ElementModel {

    public SolidBlockModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 0f;
        restitution = 0.1f;
        friction = 1.0f;
        color = Color.BLACK;
        powerThreshold = 0f;
        strength = 0f;
        fragile = false;
        fireStateChanged();
    }   
}
