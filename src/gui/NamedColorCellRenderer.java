/* NamedColorCellRenderer.java
 * ============================================================
 * Copyright (C) 2001-2012 Ghent University
 * 
 * An example used in the 'Programming 2' course.
 * 
 * Authors: Kris Coolsaet & Bart Middag
 */
package bozels.gui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Cell renderer picturing a {@link NamedColor} as a name & icon combination. 
 * @author Kris Coolsaet
 */
public class NamedColorCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        NamedColor namedColor = (NamedColor) value;

        setText(namedColor.getName());
        setIcon(namedColor.getIcon());



        return this;
    }
}
