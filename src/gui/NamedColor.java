/* NamedColor.java
 * ============================================================
 * Copyright (C) 2001-2012 Ghent University
 * 
 * An example used in the 'Programming 2' course.
 * 
 * Authors: Kris Coolsaet & Bart Middag
 */
package bozels.gui;

import bozels.models.ElementModel;
import bozels.models.MainModel;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Simple data structure that combines a color and a name.
 * @author Our dear professor Kris Coolsaet
 * :3
 */
public class NamedColor implements ChangeListener {

    private final String name;

    private Color color;
    private final String type;
    private final MainModel model;
    private final ElementModel elModel;

    /**
     * Get the name of this color
     * @return color name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the color
     * @return color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Get the element model tied to this color
     * @return the element model
     */
    public ElementModel getElementModel() {
        return elModel;
    }

    /**
     * Create new named color for our list.
     * @param name Name of the named color
     * @param color Color
     * @param type Type of the element
     * @param model The main model
     */
    public NamedColor(String name, Color color, String type, MainModel model) {
        this.name = name;
        this.color = color;
        this.type = type;
        this.model = model;
        icon = new SquareIcon(16, color);
        elModel = model.getConfigModel().getElementModel(type);
        elModel.addChangeListener(this);
    }
    
    private SquareIcon icon;

    /**
     * Get the icon of this item
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * This color and icon has changed!
     * @param e The ChangeEvent
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        color = elModel.getColor();
        icon.setColor(color);
    }
}
