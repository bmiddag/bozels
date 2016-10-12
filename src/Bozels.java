package bozels;

import bozels.models.MainModel;
import java.awt.EventQueue;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @author Bart Middag
 */
public class Bozels {

    /**
     * Start creating the GUI
     * 
     * @param model The Main Model
     */
    public static void createGUI(MainModel model) {
        JFrame window = new JFrame("Bozels \u00a9 2012 Bart Middag");
        try {
            window.setIconImage(ImageIO.read(MainModel.class.getResource("/bozels/images/Icon.png")));
        } catch (IOException ex) {
            // Oh well, then it doesn't work. No icon for you!
        }
        model.setWindow(window);
        window.setContentPane(new MainGUIManager(window, model));
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    /**
     * Main method
     * 
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        // Create main model
        final MainModel model = new MainModel();
        // Start simulation
        Thread thread = new Thread(new Simulation(model));
        thread.start();
        // Create the GUI in the Event Queue thread
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                createGUI(model);
            }
        });
        ReadXML levelReader = new ReadXML(model);
    }
}
