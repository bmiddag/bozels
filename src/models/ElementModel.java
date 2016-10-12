/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.Color;

/**
 * The Element Model
 * @author Bart Middag
 */
public abstract class ElementModel extends Model {
    protected final ConfigModel configModel;
    protected float density;
    protected float restitution;
    protected float friction;
    protected Color color;
    protected float powerThreshold;
    protected float strength;
    protected boolean fragile;

    /**
     * Create a new Element Model
     * @param model The ConfigModel
     */
    public ElementModel(ConfigModel model) {
        this.configModel = model;
    }
    
    /**
     * Sets element properties back to default configuration.
     */
    public abstract void setDefaultConfig();
    
    /**
     * Get the Configuration Model
     * @return the configuration model
     */
    public ConfigModel getConfigModel() {
        return configModel;
    }
    
    /**
     * Get the density of this element
     * @return the density
     */
    public float getDensity() {
        return density;
    }
    
    /**
     * Set the density of this type of element
     * @param density the new density
     */
    public void setDensity(float density) {
        if(density != this.density) {
            this.density = density;
            fireStateChanged();
        }
    }

    /**
     * Get the friction of this element
     * @return the friction
     */
    public float getFriction() {
        return friction;
    }

    /**
     * Set the friction for this type of element
     * @param friction the new friction
     */
    public void setFriction(float friction) {
        if(friction != this.friction) {
            this.friction = friction;
            fireStateChanged();
        }
    }

    /**
     * Get the restitution of this element
     * @return the restitution
     */
    public float getRestitution() {
        return restitution;
    }

    /**
     * Set the restitution for this type of element
     * @param restitution the new restitution
     */
    public void setRestitution(float restitution) {
        if (restitution != this.restitution) {
            this.restitution = restitution;
            fireStateChanged();
        }
    }

    /**
     * Get the color of this type of element
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the color for this type of element
     * @param color the new color
     */
    public void setColor(Color color) {
        if (color != this.color) {
            this.color = color;
            fireStateChanged();
        }
    }
    
    /**
     * Get the power threshold of this type of element
     * @return the power threshold
     */
    public float getPowerThreshold() {
        return powerThreshold;
    }
    
    /**
     * Set the power threshold for this type of element
     * @param powerThreshold the new power threshold
     */
    public void setPowerThreshold(float powerThreshold) {
        if (powerThreshold != this.powerThreshold) {
            this.powerThreshold = powerThreshold;
            fireStateChanged();
        }
    }
    
    /**
     * Get the strength of this element
     * @return the strength
     */
    public float getStrength() {
        return strength;
    }
    
    /**
     * Set the strength of this type of element
     * @param strength the new strength
     */
    public void setStrength(float strength) {
        if (strength != this.strength) {
            this.strength = strength;
            fireStateChanged();
        }
    }
    
    /**
     * Checks whether this type of element is fragile
     * @return True if fragile - if not, false.
     */
    public boolean isFragile() {
        return fragile;
    }
    
    /**
     * Change this element's fragile state.
     * @param fragile Whether you want it to be fragile or not.
     */
    public void setFragile(boolean fragile) {
        if (fragile != this.fragile) {
            this.fragile = fragile;
            fireStateChanged();
        }
    }
    
}
