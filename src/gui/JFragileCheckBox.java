/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.gui;

import bozels.models.ElementModel;
import bozels.models.MainModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JList;

/**
 * @author Middag
 */
public class JFragileCheckBox extends JCheckBox implements ActionListener {

    private boolean currentFragile;
    private ElementModel contModel;
    private final MainModel mainModel;
    private final JList selector;

    /**
     * Create a checkbox controlling the Fragile value of a certain model
     *
     * @param label The label for this checkbox.
     * @param mainModel The main model, passed to this object because we can.
     * @param contModel The model this checkbox is controlling.
     * @param selector The selector list this checkbox listens to.
     */
    public JFragileCheckBox(String label, MainModel mainModel, ElementModel contModel, JList selector) {
        super(label);
        this.mainModel = mainModel;
        this.contModel = contModel;
        currentFragile = contModel.isFragile();
        setSelected(currentFragile);
        this.selector = selector;
        addActionListener(this);
    }

    /**
     * Sets the element model this checkbox is controlling.
     *
     * @param contModel The element model this checkbox should be controlling.
     */
    public final void setControllingModel(ElementModel contModel) {
        if (!this.contModel.equals(contModel)) {
            this.contModel = contModel;
            updateCheckBox();
        }
    }

    /**
     * OMG Action performed - must change fragile value!
     *
     * @param e The action performed event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        currentFragile = isSelected();
        contModel.setFragile(currentFragile);
    }

    /**
     * Some changes happened, update this checkbox with the model's value.
     */
    public void updateCheckBox() {
        currentFragile = contModel.isFragile();
        setSelected(currentFragile);
    }
}
