/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Metal Block Model
 * @author Bart Middag
 */
public class MetalBlockModel extends ElementModel {

    public MetalBlockModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 3.0f; //density = 0f;
        //restitution = 0.1f;
        //friction = 1.0f;
        restitution = 0.2f;
        friction = 0.3f;
        color = new Color(50,100,100);
        powerThreshold = 18.0f;
        strength = 52.0f;
        fragile = true;
        fireStateChanged();
    }   
}
