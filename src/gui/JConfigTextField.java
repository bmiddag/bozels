/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.gui;

import bozels.models.Model;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Middag
 */
public class JConfigTextField extends JTextField implements DocumentListener, FocusListener {

    private final Color editingColor = new Color(250, 240, 170);
    private final Color errorColor = Color.PINK;
    private final Color normalColor;
    private final String cancelAction = "cancel";
    private final String enterAction = "enter";
    private String normalValue;
    private final float maxValue;
    private final float minValue;
    private final Method setMethod;
    private Model contModel;
    private final Method getMethod;

    /**
     * Create a text field used for configuration
     *
     * @param value The current value of the text field
     * @param width The with of the text field
     * @param min The minimum acceptable value
     * @param max The maximum acceptable value
     * @param contModel The model this text field is controlling
     * @param getMethod The method used to get the correct (float) value from
     * the model
     * @param setMethod The method used to set the correct (float) value in the
     * model
     */
    public JConfigTextField(String value, int width, float min, float max, Model contModel, Method getMethod, Method setMethod) {
        super(value, width);
        normalValue = value;
        maxValue = max;
        minValue = min;
        this.getMethod = getMethod;
        this.setMethod = setMethod;
        this.contModel = contModel;
        getDocument().addDocumentListener(this);
        addFocusListener(this);
        InputMap im = getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke("ESCAPE"), cancelAction);
        im.put(KeyStroke.getKeyStroke("ENTER"), enterAction);
        am.put(cancelAction, new CancelAction());
        am.put(enterAction, new EnterAction());
        normalColor = getBackground();
    }

    @Override
    public void focusGained(FocusEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * This text field lost focus, set back to normal.
     *
     * @param e The FocusEvent
     */
    @Override
    public void focusLost(FocusEvent e) {
        setText(normalValue);
        setBackground(normalColor);
    }

    /**
     * The Cancel Action ( ESC pressed ). Sets the text back to normal value.
     */
    class CancelAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ev) {
            setText(normalValue);
            setBackground(normalColor);
        }
    }

    /**
     * The Enter action ( ENTER pressed ). Invokes the set method.
     */
    class EnterAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ev) {
            check();
            if (getBackground() == editingColor) {
                normalValue = getText();
                try {
                    setMethod.invoke(contModel, Float.parseFloat(normalValue));
                } catch (Exception ex) {
                }

                setBackground(normalColor);
            }
        }
    }

    /**
     * Update inserted, check this value.
     *
     * @param e DocumentEvent
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        check();
    }

    /**
     * Update removed, check this value.
     *
     * @param e DocumentEvent
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        check();
    }

    /**
     * Update changed - unused
     *
     * @param e DocumentEvent
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Check the current input.
     */
    public void check() {
        String s = getText();
        try {
            float f = Float.parseFloat(s);
            if (f <= maxValue && f >= minValue) {
                if (f == (Float.parseFloat(normalValue))) {
                    setBackground(normalColor);
                } else {
                    setBackground(editingColor);
                }
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            setBackground(errorColor);
        }
    }

    /**
     * Set the model this text field is controlling.
     *
     * @param contModel The model this field should be controlling.
     */
    public void setControllingModel(Model contModel) {
        if (!this.contModel.equals(contModel)) {
            this.contModel = contModel;
            updateField();
        }
    }

    /**
     * Update this field to the model's current value.
     */
    public void updateField() {
        try {
            normalValue = ((Float) getMethod.invoke(contModel)).toString();
        } catch (Exception ex) {
        }
        setText(normalValue);
        setBackground(normalColor);
    }
}
