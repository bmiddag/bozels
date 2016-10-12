/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Ice Block Model
 * @author Bart Middag
 */
public class IceBlockModel extends ElementModel {

    public IceBlockModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 1.0f;
        restitution = 0.0f;
        friction = 0.1f;
        color = Color.BLUE;
        powerThreshold = 15.0f;
        strength = 10.0f;
        fragile = true;
        fireStateChanged();
    }   
}
