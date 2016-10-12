package bozels.gui;

import bozels.models.MainModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

/**
 * The Tab Panel which takes up the bottom of the screen.
 * @author Bart Middag
 */
public class TabPanel extends JPanel {

    private final MainModel model;
    private final JPanel buttonPane;
    private final JToggleButton btnPause;
    private final JButton btnReset;
    private final JButton btnDefault;
    private final JButton btnClear;
    private final JButton btnLoad;
    private final JToggleButton btnScenery;
    private final JTabbedPane tabbedPane;
    private MainPanel card1;
    private MaterialsPanel card2;
    private BozelsPanel card3;
    private TargetsPanel card4;
    
    /**
     * Create the Tab Panel.
     * @param model the Main Model, King of all Models.
     */
    public TabPanel(MainModel model) {
        this.model = model;
        
        // Create layout: part 1
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        // Add buttons
        buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(6,1,5,5));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        btnPause = new JToggleButton(model.getPauseAction());
        btnReset = new JButton(model.getResetAction());
        btnDefault = new JButton(model.getDefaultAction());
        btnClear = new JButton(model.getClearAction());
        btnLoad = new JButton(model.getLoadAction());
        btnScenery = new JToggleButton(model.getConfigModel().getShowSceneryAction());
        buttonPane.add(btnPause);
        buttonPane.add(btnReset);
        buttonPane.add(btnDefault);
        buttonPane.add(btnClear);
        buttonPane.add(btnLoad);
        buttonPane.add(btnScenery);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
 
        //Create the different tabs.
        try {
            card1 = new MainPanel(model);
            card2 = new MaterialsPanel(model);
            card3 = new BozelsPanel(model);
            card4 = new TargetsPanel(model);
            
            tabbedPane.addTab(model.getLocaleBundle().getString("TAB_MAIN"), card1);
            tabbedPane.addTab(model.getLocaleBundle().getString("TAB_MATERIALS"), card2);
            tabbedPane.addTab(model.getLocaleBundle().getString("TAB_BOZELS"), card3);
            tabbedPane.addTab(model.getLocaleBundle().getString("TAB_TARGETS"), card4);
        } catch(NoSuchMethodException ex) {
            // Error man.
            JOptionPane.showMessageDialog(model.getWindow(),model.getLocaleBundle().getString("GUI_ERROR_INITFAIL"),model.getLocaleBundle().getString("ERROR"),JOptionPane.WARNING_MESSAGE);
        }
 
        // Create the layout: part 2
        add(buttonPane, BorderLayout.WEST);
        add(tabbedPane, BorderLayout.CENTER);
        
        model.setTabPanel(this);
    }
    
    /**
     * Updates all fields in all tabs.
     */
    public void updateTabs() {
        if(card1!=null) {
            card1.updateFields();
        }
        if(card2!=null) {
            card2.updateFields();
        }
        if(card3!=null) {
            card3.updateFields();
        }
        if(card4!=null) {
            card4.updateFields();
        }
    }
}
