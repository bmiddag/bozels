package bozels.gui;

import bozels.models.ElementModel;
import bozels.models.MainModel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Bart Middag
 */
public class BozelsPanel extends JPanel {
    private ElementModel currentModel;
    private final JList<NamedColor> selector;
    private final JConfigTextField txtDensity;
    private final JLabel lblRestitution;
    private final JLabel lblFriction;
    private final JConfigTextField txtRestitution;
    private final JConfigTextField txtFriction;
    private final JLabel lblColor;
    private final JColorButton btnColor;
    private final JLabel lblDensity;
    
    /**
     * Create a new Bozels tab for the tab panel.
     * @param model the main model
     * @throws NoSuchMethodException
     */
    public BozelsPanel(MainModel model) throws NoSuchMethodException {
        // Add whitespace to this panel
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setLayout(new BorderLayout(5,5));
        
        // Add items to the list
        NamedColor[] items = {
            new NamedColor(model.getLocaleBundle().getString("BOZEL_RED"),Color.RED,"redBozel",model),
            new NamedColor(model.getLocaleBundle().getString("BOZEL_BLUE"),Color.BLUE,"blueBozel",model),
            new NamedColor(model.getLocaleBundle().getString("BOZEL_WHITE"),Color.GRAY,"whiteBozel",model),
            new NamedColor(model.getLocaleBundle().getString("BOZEL_YELLOW"),Color.YELLOW,"yellowBozel",model)
        };
        
        // Add editor panel that listens to the selector list
        JPanel editor = new JPanel();
        selector = new JList<NamedColor>(items);
        
        // Initiate some stuff for the selector list
        selector.setSelectedIndex(0);
        selector.setCellRenderer(new NamedColorCellRenderer()); // NEW
        selector.setPrototypeCellValue(new NamedColor("LongColor", Color.BLACK,"solidBlock",model)); // NEW
        editor.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        selector.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Put stuff on the editor panel
        lblDensity = new JLabel (model.getLocaleBundle().getString("PROPERTY_DENSITY"), JLabel.TRAILING);
        currentModel = model.getConfigModel().getElementModel("redBozel");
        txtDensity = new JConfigTextField(new Float(currentModel.getDensity()).toString(),20,0f,Float.MAX_VALUE,currentModel,currentModel.getClass().getMethod("getDensity"),currentModel.getClass().getMethod("setDensity", float.class));
        lblRestitution = new JLabel (model.getLocaleBundle().getString("PROPERTY_RESTITUTION"), JLabel.TRAILING);
        txtRestitution = new JConfigTextField(new Float(currentModel.getRestitution()).toString(),20,0f,1f,currentModel,currentModel.getClass().getMethod("getRestitution"),currentModel.getClass().getMethod("setRestitution", float.class));
        lblFriction = new JLabel (model.getLocaleBundle().getString("PROPERTY_FRICTION"), JLabel.TRAILING);
        txtFriction = new JConfigTextField(new Float(currentModel.getFriction()).toString(),20,0f,1f,currentModel,currentModel.getClass().getMethod("getFriction"),currentModel.getClass().getMethod("setFriction", float.class));
        lblColor = new JLabel(model.getLocaleBundle().getString("PROPERTY_COLOR"), JLabel.TRAILING);
        btnColor = new JColorButton(model,currentModel,selector);
        
        // Create listener
        selector.addListSelectionListener (new ListSelectionListener (){
            @Override
            public void valueChanged (ListSelectionEvent e) {
                if (! selector.getValueIsAdjusting ()) {
                    // Update fields
                    txtDensity.setControllingModel(selector.getSelectedValue().getElementModel());
                    txtRestitution.setControllingModel(selector.getSelectedValue().getElementModel());
                    txtFriction.setControllingModel(selector.getSelectedValue().getElementModel());
                    btnColor.setControllingModel(selector.getSelectedValue().getElementModel());
                }
            }
        });
        
        add(selector,BorderLayout.WEST);
        add(editor,BorderLayout.CENTER);
        
        // Create the layout
        GroupLayout layout = new GroupLayout(editor);
        editor.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblDensity)
                    .addComponent(txtDensity)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblRestitution)
                    .addComponent(txtRestitution)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblFriction)
                    .addComponent(txtFriction)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblColor)
                    .addComponent(btnColor)
                )
        );
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDensity)
                    .addComponent(lblRestitution)
                    .addComponent(lblFriction)
                    .addComponent(lblColor)
                )
                .addGroup(layout.createParallelGroup()
                    .addComponent(txtDensity)
                    .addComponent(txtRestitution)
                    .addComponent(txtFriction)
                    .addComponent(btnColor)
                )
        );
    }
    
    /**
     * Other settings applied without us knowing - update all fields! Now.
     */
    public void updateFields() {
        txtDensity.updateField();
        txtRestitution.updateField();
        txtFriction.updateField();
        btnColor.updateColorButton();
        selector.repaint();
    }
}
