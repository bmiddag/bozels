/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.gui;

import bozels.models.ElementModel;
import bozels.models.MainModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JList;

/**
 * @author Middag
 */
public class JColorButton extends JButton implements ActionListener {

    private Color currentColor;
    private ElementModel contModel;
    private final MainModel mainModel;
    private final JList selector;

    /**
     * Create a color chooser button
     *
     * @param mainModel The main model
     * @param contModel The model this button is currently controlling
     * @param selector The list this button listens to
     */
    public JColorButton(MainModel mainModel, ElementModel contModel, JList selector) {
        super();
        setMinimumSize(new Dimension(44, 22));
        this.mainModel = mainModel;
        this.contModel = contModel;
        this.selector = selector;
        currentColor = contModel.getColor();
        setBackground(currentColor);
        setOpaque(true);
        addActionListener(this);
    }

    /**
     * Sets the model this is controlling
     *
     * @param contModel
     */
    public final void setControllingModel(ElementModel contModel) {
        if (!this.contModel.equals(contModel)) {
            this.contModel = contModel;
            updateColorButton();
        }
    }

    /**
     * Set the color
     *
     * @param color
     */
    public final void setColor(Color color) {
        if (!currentColor.equals(color)) {
            currentColor = color;
            contModel.setColor(currentColor);
            selector.repaint();
            setBackground(currentColor);
        }
    }

    /**
     * You clicked the button!
     *
     * @param e The click ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(mainModel.getWindow(), "Kies een nieuwe kleur...", getBackground());
        if (newColor != null) {
            setColor(newColor);
        }
    }

    /**
     * Some changes happened, must update this button too.
     */
    public void updateColorButton() {
        currentColor = contModel.getColor();
        setBackground(currentColor);
    }
}
