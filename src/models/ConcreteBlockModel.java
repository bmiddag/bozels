/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Concrete Block Model
 * @author Bart Middag
 */
public class ConcreteBlockModel extends ElementModel {

    public ConcreteBlockModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 4.0f;
        restitution = 0.0f;
        friction = 0.9f;
        color = Color.GRAY;
        powerThreshold = 20.0f;
        strength = 50.0f;
        fragile = true;
        fireStateChanged();
    }   
}
