/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Yellow Block Model
 * @author Bart Middag
 */
public class YellowBozelModel extends ElementModel {

    public YellowBozelModel(ConfigModel model) {
        super(model);
        setDefaultConfig();
    }
    
    @Override
    public final void setDefaultConfig() {
        density = 10.0f;
        restitution = 0.1f;
        friction = 0.9f;
        color = Color.YELLOW;
        powerThreshold = 0f;
        strength = 0f;
        fragile = false;
        fireStateChanged();
    }   
}
