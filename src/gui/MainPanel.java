package bozels.gui;

import bozels.models.ConfigModel;
import bozels.models.MainModel;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Bart Middag
 */
public class MainPanel extends JPanel {
    private final MainModel model;
    private final ConfigModel configModel;
    private final JConfigTextField txtGravity;
    private final JLabel lblGravity;
    private final JCheckBox chkShowCentroid;
    private final JLabel lblTimeStep;
    private final JConfigTextField txtTimeStep;
    private final JCheckBox chkShowSpeed;
    private final JLabel lblGameSpeed;
    private final JConfigTextField txtGameSpeed;
    private final JCheckBox chkMarkSleeping;
    private final JLabel lblLaunchPower;
    private final JCheckBox chkShowRays;
    private final JConfigTextField txtLaunchPower;
    
    /**
     * Create the main tab of the Tab Panel.
     * @param model The Main Model, King of all Models.
     * @throws NoSuchMethodException
     */
    public MainPanel(MainModel model) throws NoSuchMethodException {
        this.model = model;
        configModel = model.getConfigModel();
        
        // Create content
        lblGravity = new JLabel (model.getLocaleBundle().getString("PROPERTY_GRAVITY"), JLabel.TRAILING);
        txtGravity = new JConfigTextField(new Float(configModel.getGravity()).toString(),20,-Float.MAX_VALUE,Float.MAX_VALUE,configModel,configModel.getClass().getMethod("getGravity"),configModel.getClass().getMethod("setGravity", float.class));
        chkShowCentroid = new JCheckBox(configModel.getShowCentroidAction());
        lblTimeStep = new JLabel (model.getLocaleBundle().getString("PROPERTY_TIME_STEP"), JLabel.TRAILING);
        txtTimeStep = new JConfigTextField(new Float(configModel.getTimeStep()).toString(),20,0f,Float.MAX_VALUE,configModel,configModel.getClass().getMethod("getTimeStep"),configModel.getClass().getMethod("setTimeStep", float.class));
        chkShowSpeed = new JCheckBox(configModel.getShowSpeedAction());
        lblGameSpeed = new JLabel (model.getLocaleBundle().getString("PROPERTY_SPEED"), JLabel.TRAILING);
        txtGameSpeed = new JConfigTextField(new Float(configModel.getGameSpeed()).toString(),20,0f,Float.MAX_VALUE,configModel,configModel.getClass().getMethod("getGameSpeed"),configModel.getClass().getMethod("setGameSpeed", float.class));
        chkMarkSleeping = new JCheckBox(configModel.getMarkSleepingAction());
        lblLaunchPower = new JLabel (model.getLocaleBundle().getString("PROPERTY_LAUNCH_POWER"), JLabel.TRAILING);
        txtLaunchPower = new JConfigTextField(new Float(configModel.getLaunchPower()).toString(),20,0f,Float.MAX_VALUE,configModel,configModel.getClass().getMethod("getLaunchPower"),configModel.getClass().getMethod("setLaunchPower", float.class));
        chkShowRays = new JCheckBox(configModel.getShowRaysAction());
        
        // Create layout
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblGravity)
                    .addComponent(txtGravity)
                    .addComponent(chkShowCentroid)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblTimeStep)
                    .addComponent(txtTimeStep)
                    .addComponent(chkShowSpeed)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblGameSpeed)
                    .addComponent(txtGameSpeed)
                    .addComponent(chkMarkSleeping)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblLaunchPower)
                    .addComponent(txtLaunchPower)
                    .addComponent(chkShowRays)
                )
        );
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(lblGravity)
                    .addComponent(lblTimeStep)
                    .addComponent(lblGameSpeed)
                    .addComponent(lblLaunchPower)
                )
                .addGroup(layout.createParallelGroup()
                    .addComponent(txtGravity)
                    .addComponent(txtTimeStep)
                    .addComponent(txtGameSpeed)
                    .addComponent(txtLaunchPower)
                )
                .addGroup(layout.createParallelGroup()
                    .addComponent(chkShowCentroid)
                    .addComponent(chkShowSpeed)
                    .addComponent(chkMarkSleeping)
                    .addComponent(chkShowRays)
                )
        );
    }
    
    /**
     * Other settings applied without us knowing - update all fields! Now.
     */
    public void updateFields() {
        txtGravity.updateField();
        txtTimeStep.updateField();
        txtGameSpeed.updateField();
        txtLaunchPower.updateField();
        chkShowCentroid.setSelected(configModel.isShowCentroid());
        chkShowSpeed.setSelected(configModel.isShowSpeed());
        chkMarkSleeping.setSelected(configModel.isMarkSleeping());
        chkShowRays.setSelected(configModel.isShowRays());
    }
}
