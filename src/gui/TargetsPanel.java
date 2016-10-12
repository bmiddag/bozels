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
 * The Targets Tab of the Tab Panel
 * @author Bart Middag
 */
public class TargetsPanel extends JPanel {
    private final JLabel lblDensity;
    private final JConfigTextField txtDensity;
    private final JLabel lblRestitution;
    private final JConfigTextField txtRestitution;
    private final JLabel lblPowerTreshold;
    private final JConfigTextField txtPowerTreshold;
    private final JLabel lblFriction;
    private final JConfigTextField txtFriction;
    private final JLabel lblStrength;
    private final JConfigTextField txtStrength;
    private final JLabel lblColor;
    private final JColorButton btnColor;
    private final JPanel editor;
    private final JList<NamedColor> selector;
    private final ElementModel currentModel;
    private final JFragileCheckBox chkFragile;
    
    /**
     * Create the Targets tab of the Tab Panel.
     * @param model The Main Model, King of all Models.
     * @throws NoSuchMethodException 
     */
    public TargetsPanel(MainModel model) throws NoSuchMethodException {
        // Create whitespace
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setLayout(new BorderLayout(5,5));
        
        // Create items for the very short list
        NamedColor[] items = {
            new NamedColor(model.getLocaleBundle().getString("TARGET_BIG"),Color.PINK,"bigTarget",model),
            new NamedColor(model.getLocaleBundle().getString("TARGET_SMALL"),Color.GREEN,"smallTarget",model)
        };
        
        // Create editor panel & selector list
        editor = new JPanel();
        selector = new JList<NamedColor>(items);
        
        // Do stuff for the selector list
        selector.setSelectedIndex(0);
        selector.setCellRenderer(new NamedColorCellRenderer());
        selector.setPrototypeCellValue(new NamedColor("LongColor", Color.BLACK,"solidBlock",model));
        editor.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        selector.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Create the content of the editor panel
        currentModel = model.getConfigModel().getElementModel("bigTarget");
        lblDensity = new JLabel (model.getLocaleBundle().getString("PROPERTY_DENSITY"), JLabel.TRAILING);
        txtDensity = new JConfigTextField(new Float(currentModel.getDensity()).toString(),20,0f,Float.MAX_VALUE,currentModel,currentModel.getClass().getMethod("getDensity"),currentModel.getClass().getMethod("setDensity", float.class));
        chkFragile = new JFragileCheckBox(model.getLocaleBundle().getString("PROPERTY_FRAGILE"),model,currentModel,selector);
        lblRestitution = new JLabel (model.getLocaleBundle().getString("PROPERTY_RESTITUTION"), JLabel.TRAILING);
        txtRestitution = new JConfigTextField(new Float(currentModel.getRestitution()).toString(),20,0f,1f,currentModel,currentModel.getClass().getMethod("getRestitution"),currentModel.getClass().getMethod("setRestitution", float.class));
        lblPowerTreshold = new JLabel (model.getLocaleBundle().getString("PROPERTY_POWER_THRESHOLD"), JLabel.TRAILING);
        txtPowerTreshold = new JConfigTextField(new Float(currentModel.getPowerThreshold()).toString(),20,0f,Float.MAX_VALUE,currentModel,currentModel.getClass().getMethod("getPowerThreshold"),currentModel.getClass().getMethod("setPowerThreshold", float.class));
        lblFriction = new JLabel (model.getLocaleBundle().getString("PROPERTY_FRICTION"), JLabel.TRAILING);
        txtFriction = new JConfigTextField(new Float(currentModel.getFriction()).toString(),20,0f,1f,currentModel,currentModel.getClass().getMethod("getFriction"),currentModel.getClass().getMethod("setFriction", float.class));
        lblStrength = new JLabel (model.getLocaleBundle().getString("PROPERTY_STRENGTH"), JLabel.TRAILING);
        txtStrength = new JConfigTextField(new Float(currentModel.getStrength()).toString(),20,0f,Float.MAX_VALUE,currentModel,currentModel.getClass().getMethod("getFriction"),currentModel.getClass().getMethod("setFriction", float.class));
        lblColor = new JLabel(model.getLocaleBundle().getString("PROPERTY_COLOR"), JLabel.TRAILING);
        btnColor = new JColorButton(model,currentModel,selector);
        
        // Create list selection listener
        selector.addListSelectionListener (new ListSelectionListener (){
            @Override
            public void valueChanged (ListSelectionEvent e) {
                if (! selector.getValueIsAdjusting ()) { 
                    // Adjust the values of the content of the editor panel when the selection changes.
                    txtDensity.setControllingModel(selector.getSelectedValue().getElementModel());
                    chkFragile.setControllingModel(selector.getSelectedValue().getElementModel());
                    txtRestitution.setControllingModel(selector.getSelectedValue().getElementModel());
                    txtPowerTreshold.setControllingModel(selector.getSelectedValue().getElementModel());
                    txtFriction.setControllingModel(selector.getSelectedValue().getElementModel());
                    txtStrength.setControllingModel(selector.getSelectedValue().getElementModel());
                    btnColor.setControllingModel(selector.getSelectedValue().getElementModel());
                }
            }
        });
        
        // Add to main BorderLayout.
        add(selector,BorderLayout.WEST);
        add(editor,BorderLayout.CENTER);
        
        // Create GroupLayout of editor panel
        GroupLayout layout = new GroupLayout(editor);
        editor.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblDensity)
                    .addComponent(txtDensity)
                    .addComponent(chkFragile)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblRestitution)
                    .addComponent(txtRestitution)
                    .addComponent(lblPowerTreshold)
                    .addComponent(txtPowerTreshold)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblFriction)
                    .addComponent(txtFriction)
                    .addComponent(lblStrength)
                    .addComponent(txtStrength)
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
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPowerTreshold)
                    .addComponent(lblStrength)
                )
                .addGroup(layout.createParallelGroup()
                    .addComponent(chkFragile)
                    .addComponent(txtPowerTreshold)
                    .addComponent(txtStrength)
                )
        );
    }
    
    /**
     * Other settings applied without us knowing - update all fields! Now.
     */
    public void updateFields() {
        txtDensity.updateField();
        chkFragile.updateCheckBox();
        txtRestitution.updateField();
        txtPowerTreshold.updateField();
        txtFriction.updateField();
        txtStrength.updateField();
        btnColor.updateColorButton();
        selector.repaint();
    }
}
