package bozels;

import bozels.gui.TabPanel;
import bozels.models.MainModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

/**
 * @author Bart Middag
 */
public class MainGUIManager extends JPanel {

    //final static int extraWindowWidth = 100;
    private final MainModel model;
    
    /**
     * Initiate the GUI
     */
    public MainGUIManager(JFrame window, final MainModel model) {
        super (new BorderLayout());
        
        this.model = model;
        
        // Create main GUI parts
        TabPanel tp = new TabPanel(model);
        GameWindow gw = new GameWindow(1024,450,model);
        model.setGamePanel(gw);
        gw.setPreferredSize(new Dimension(1024,450));
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar ();
        JMenu menuBestand = new JMenu (model.getLocaleBundle().getString("MENU_FILE"));
        menuBestand.setMnemonic (model.getLocaleBundle().getString("MENU_FILE_MNEMONIC").charAt(0));
        JMenuItem menuLoad = new JMenuItem(model.getLoadAction());
        JMenuItem menuExit = new JMenuItem(model.getExitAction());
        menuBestand.add(menuLoad);
        menuBestand.add(menuExit);
        
        menuBar.add (menuBestand);
        
        JMenu menuSpel = new JMenu (model.getLocaleBundle().getString("MENU_GAME"));
        menuSpel.setMnemonic (model.getLocaleBundle().getString("MENU_GAME_MNEMONIC").charAt(0));
        JCheckBoxMenuItem menuPause = new JCheckBoxMenuItem(model.getPauseAction());
        JMenuItem menuReset = new JMenuItem(model.getResetAction());
        menuSpel.add(menuPause);
        menuSpel.add(menuReset);
        
        menuBar.add (menuSpel);
        
        JMenu menuExtra = new JMenu (model.getLocaleBundle().getString("MENU_EXTRA"));
        menuExtra.setMnemonic (model.getLocaleBundle().getString("MENU_EXTRA_MNEMONIC").charAt(0));
        JCheckBoxMenuItem menuScenery = new JCheckBoxMenuItem(model.getConfigModel().getShowSceneryAction());
        menuExtra.add(menuScenery);
        
        menuBar.add(menuExtra);
        
        // Do dat layout
        add(gw, BorderLayout.CENTER);
        add(tp, BorderLayout.SOUTH);
        
        window.setJMenuBar(menuBar);
    }
}
