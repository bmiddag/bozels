/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import bozels.GameWindow;
import bozels.ReadXML;
import bozels.Simulation;
import bozels.elements.Bozel;
import bozels.elements.Element;
import bozels.elements.Target;
import bozels.gui.TabPanel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.jbox2d.dynamics.World;

/**
 * The Main Model - King of all Models
 *
 * @author Bart Middag
 */
public class MainModel extends Model {

    private Simulation simulator;
    private GameWindow gamePanel;
    private List<Element> elList;
    private List<Target> targetList;
    private final ConfigModel configModel;
    private World world;
    private Stack<Element> initList;
    private List<Element> actorsList;
    private String levelName;
    private float catapultX;
    private float catapultY;
    private int currentBozel;
    private Map<Integer, Bozel> bozelMap;
    private boolean paused;
    private final Action pauseAction;
    private final Action resetAction;
    private final Action clearAction;
    private final Action loadAction;
    private final Action exitAction;
    private JFrame window;
    private TabPanel tabPanel;
    private List<int[]> raysList;
    private final BonusModel bonusModel;
    private int winState;
    private final Locale currentLocale;
    private final ResourceBundle localeBundle;
    private final Pattern basename = Pattern.compile(".*?s([^/]*)\\.xml$");
    private final Action defaultAction;

    /**
     * Create the Main Model, King of all Models
     */
    public MainModel() {
        world = null;
        simulator = null;

        // The game starts out paused
        paused = true;

        // Initiate locales
        currentLocale = Locale.getDefault();
        //currentLocale = Locale.forLanguageTag("nl");
        localeBundle = ResourceBundle.getBundle("bozels/game", currentLocale);

        // Initiate actions
        pauseAction = new AbstractAction(localeBundle.getString("ACTION_PAUSE")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                setPaused(!isPaused());
            }
        };
        pauseAction.putValue(Action.SELECTED_KEY, paused);
        pauseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

        resetAction = new AbstractAction(localeBundle.getString("ACTION_RESTART")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                String levelName = getLevelName();
                clearWorld();
                restart(levelName);
            }
        };
        resetAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));

        defaultAction = new AbstractAction(localeBundle.getString("ACTION_DEFAULT")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                setDefaultConfig();
            }
        };
        defaultAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));

        clearAction = new AbstractAction(localeBundle.getString("ACTION_CLEAR")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                clearWorld();
            }
        };
        clearAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));

        loadAction = new AbstractAction(localeBundle.getString("ACTION_LOAD")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                clearWorld();
                readLevel();
            }
        };
        loadAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        exitAction = new AbstractAction(localeBundle.getString("ACTION_CLOSE")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
                window.dispose();
                System.exit(0);
            }
        };
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

        // Initiate 
        catapultX = 0f;
        catapultY = 0f;
        winState = 2;
        currentBozel = 1;

        // Initiate lists
        bozelMap = new HashMap<Integer, Bozel>();
        targetList = new ArrayList<Target>();
        elList = new ArrayList<Element>();
        initList = new Stack<Element>();
        actorsList = new ArrayList<Element>();
        raysList = new ArrayList<int[]>();

        // Initiate models
        configModel = new ConfigModel(this);
        bonusModel = new BonusModel(this);
        levelName = localeBundle.getString("NAME_NO_LEVEL");
    }

    /**
     * Get the current locale.
     *
     * @return the current locale
     */
    public Locale getLocale() {
        return currentLocale;
    }

    /**
     * Get the current locale bundle.
     *
     * @return the current locale bundle
     */
    public ResourceBundle getLocaleBundle() {
        return localeBundle;
    }

    /**
     * Set the settings back to the default settings.
     */
    public void setDefaultConfig() {
        configModel.setDefaultConfig();
        for (Object m : configModel.getElementModels().values()) {
            ElementModel elm = (ElementModel) m;
            elm.setDefaultConfig();
        }
        if (tabPanel != null) {
            tabPanel.updateTabs();
        }
    }

    /**
     * Registers the Tab Panel
     * @param tabPanel the tab panel
     */
    public void setTabPanel(TabPanel tabPanel) {
        this.tabPanel = tabPanel;
    }

    /**
     * Clear the world. Synchronized because we don't want any changes to happen
     * while we're clearing this world.
     */
    public synchronized void clearWorld() {
        winState = 2;
        initList.clear();
        while (!elList.isEmpty()) {
            for (int i = 0; i < elList.size(); i++) {
                elList.get(i).unregister();
            }
        }
        bozelMap.clear();
        clearRays();
        configModel.newEnvironment();
        world = simulator.newWorld(configModel.getGravity());
        setLevelName(localeBundle.getString("NAME_NO_LEVEL"));
        catapultX = 0f;
        catapultY = 0f;
        gamePanel.setThrown(false);
        currentBozel = 1;
    }

    /**
     * Read level. Synchronized because we don't want any changes to happen
     * while we're reading this level.
     */
    public synchronized void readLevel() {
        new ReadXML(this);
    }

    /**
     * Restart the current level. Synchronized because we don't want any other
     * changes to happen while we're restarting.
     *
     * @param levelName The current level URL (string)
     */
    public synchronized void restart(String levelName) {
        if (!levelName.equals(localeBundle.getString("NAME_NO_LEVEL"))) {
            new ReadXML(this, levelName);
        }
    }

    /**
     * Get the pause action.
     *
     * @return the pause action
     */
    public Action getPauseAction() {
        return pauseAction;
    }

    /**
     * Get the reset action.
     *
     * @return the reset action
     */
    public Action getResetAction() {
        return resetAction;
    }

    /**
     * Get the world clearing action.
     *
     * @return the world clearing action
     */
    public Action getClearAction() {
        return clearAction;
    }

    /**
     * Get the default settings action.
     *
     * @return the default settings action
     */
    public Action getDefaultAction() {
        return defaultAction;
    }

    /**
     * Get the level load action.
     *
     * @return the level load action
     */
    public Action getLoadAction() {
        return loadAction;
    }

    /**
     * Get the exit action.
     *
     * @return the exit action
     */
    public Action getExitAction() {
        return exitAction;
    }

    /**
     * Get the winning state of the game.
     *
     * @return the winning state
     */
    public int getWinState() {
        return winState;
    }

    /**
     * Set the winning state of the game
     *
     * @param winState the new winning state
     */
    public void setWinState(int winState) {
        if (winState != this.winState) {
            this.winState = winState;
            fireStateChanged();
        }
    }

    /**
     * Get the previous living Bozel
     *
     * @return the previous living Bozel
     */
    public Bozel getLastBozel() {
        return getLastBozel(currentBozel);
    }

    /**
     * Get the previous living Bozel before a certain ID
     *
     * @param id the ID of your current Bozel
     * @return the previous living Bozel
     */
    public Bozel getLastBozel(int id) {
        if (id < 1) {
            return null;
        }
        if (bozelMap.get(id) != null) {
            return bozelMap.get(id);
        } else {
            return getLastBozel(id - 1);
        }
    }

    /**
     * Get the Bozel that is next in line
     *
     * @return the next Bozel
     */
    public Bozel getNextBozel() {
        return bozelMap.get(currentBozel + 1);
    }

    /*
     * Register the window of this game
     */
    public void setWindow(JFrame window) {
        this.window = window;
    }

    /**
     * Get the window of this game
     *
     * @return the window of this game
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     * Register the simulator of this game
     *
     * @param simulator the simulator of the game
     */
    public void setSimulator(Simulation simulator) {
        if (!simulator.equals(this.simulator)) {
            this.simulator = simulator;
            //fireStateChanged();
        }
    }

    /**
     * Set the level name to something else.
     *
     * @param levelName the new level name
     */
    public void setLevelName(String levelName) {
        if (!levelName.equals(this.levelName)) {
            this.levelName = levelName;
            if (window != null) {
                if (this.levelName.equals(localeBundle.getString("NAME_NO_LEVEL"))) {
                    window.setTitle("Bozels \u00a9 2012 Bart Middag");
                } else {
                    Matcher matcher = basename.matcher(levelName);
                    if (matcher.matches()) {
                        window.setTitle("Bozels \u00a9 2012 Bart Middag - " + localeBundle.getString("LEVEL") + " " + matcher.group(1));
                    } else {
                        window.setTitle("Bozels \u00a9 2012 Bart Middag - " + levelName);
                    }
                }
            }
            //fireStateChanged();
        }
    }

    /**
     * Get the current level name.
     *
     * @return the current level name
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Get the simulator
     *
     * @return the simulator
     */
    public Simulation getSimulator() {
        return simulator;
    }

    /**
     * Get the Configuration Model
     *
     * @return the configuration model
     */
    public ConfigModel getConfigModel() {
        return configModel;
    }

    /**
     * Get the Bonus Model
     *
     * @return the bonus model
     */
    public BonusModel getBonusModel() {
        return bonusModel;
    }

    /**
     * Get the Fantastic Game Panel
     *
     * @return the Fantastic Game Panel
     */
    public GameWindow getGamePanel() {
        return gamePanel;
    }

    /**
     * Register the Fantastic Game Panel
     *
     * @param panel the Fantastic Game Panel
     */
    public void setGamePanel(GameWindow panel) {
        if (!panel.equals(this.gamePanel)) {
            this.gamePanel = panel;
            fireStateChanged();
        }
    }

    /**
     * Pause or unpause the game
     *
     * @param paused whether you want the game to be paused or not
     */
    public void setPaused(boolean paused) {
        if (this.paused != paused) {
            this.paused = paused;
            pauseAction.putValue(Action.SELECTED_KEY, paused);
            fireStateChanged();
        }
    }

    /**
     * Check if the game is paused.
     *
     * @return whether the game is paused
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Set the catapult position
     *
     * @param catapultX the catapult's X coordinate
     * @param catapultY the catapult's Y coordinate
     */
    public void setCatapultPosition(float catapultX, float catapultY) {
        this.catapultX = catapultX;
        this.catapultY = catapultY;
        //fireStateChanged();
    }

    /**
     * Get the catapult's X coordinate
     *
     * @return the catapult's X coordinate
     */
    public float getCatapultX() {
        return catapultX;
    }

    /**
     * Get the catapult's Y coordinate
     *
     * @return the catapult's Y coordinate
     */
    public float getCatapultY() {
        return catapultY;
    }

    /**
     * Add a Bozel to the list.
     *
     * @param id the ID of the Bozel
     * @param bo the new Bozel
     */
    public void addBozel(int id, Bozel bo) {
        bozelMap.put(id, bo);
        if (id == 1) {
            bo.toCatapult();
        }
    }

    /**
     * Add a Target to the list. Synchronized because we don't want to touch it
     * while something else is touching it.
     *
     * @param ta the new Target
     */
    public synchronized void addTarget(Target ta) {
        targetList.add(ta);
        fireStateChanged();
    }

    /**
     * Remove a Target from the list. Synchronized because we don't want to
     * touch it while something else is touching it.
     *
     * @param ta the removed Target
     */
    public synchronized void removeTarget(Target ta) {
        targetList.remove(ta);
        fireStateChanged();
    }

    /**
     * Remove a Bozel from the list. Synchronized because don't want to touch it
     * while something else is touching it.
     *
     * @param id the Bozel to be destroyed
     */
    public synchronized void removeBozel(int id) {
        bozelMap.remove(id);
    }

    /**
     * Get the Target list. Synchronized because we don't want to touch it while
     * something else is touching it.
     *
     * @return the Target list
     */
    public synchronized List getTargets() {
        return targetList;
    }

    /**
     * Get the amount of targets.
     *
     * @return the amount of targetz
     */
    public synchronized int getTargetAmount() {
        return targetList.size();
    }

    /**
     * Get the current Bozel.
     *
     * @return the current Bozel
     */
    public Bozel getCurrentBozel() {
        return bozelMap.get(currentBozel);
    }

    /**
     * Switch to the next Bozel.
     *
     * @return whether going to the next Bozel is possible or not
     */
    public boolean nextBozel() {
        boolean possible = false;
        currentBozel++;
        Bozel thisBozel = bozelMap.get(currentBozel);
        if (thisBozel != null) {
            possible = true;
            thisBozel.toCatapult();
        }
        return possible;
    }

    /**
     * Add an Element to the list. Synchronized because we don't want any other
     * changes to the list while we're adding this one.
     *
     * @param el the element to be added to the list
     */
    public synchronized void addElement(Element el) {
        elList.add(el);
    }

    /**
     * Remove an Element from the list. Synchronized because we don't want any
     * other changes to the list while we're removing this.
     *
     * @param el the element to be removed from the list
     */
    public synchronized void removeElement(Element el) {
        elList.remove(el);
    }

    /**
     * Add an Element to the initiate stack. Synchronized because we can't add
     * any other elements to the same stack at the same time. Pure logic!
     *
     * @param el the element to be added
     */
    public synchronized void addInit(Element el) {
        initList.push(el);
    }

    /**
     * Remove the last element from the initiate stack. Synchronized because we
     * don't want any changes to the list at this time. Pure logic!
     */
    public synchronized void popInit() {
        initList.pop();
    }

    /**
     * Get the initiate stack. Synchronized because we don't want any changes
     * while we're getting this damned stack!
     *
     * @return this damned initiate stack
     */
    public synchronized Stack<Element> getInit() {
        return initList;
    }

    /**
     * Add an actor to the list. Synchronized because we can't add two actors at
     * the same time! Sad face.
     *
     * @param el actor to be added
     */
    public synchronized void addActor(Element el) {
        actorsList.add(el);
    }

    /**
     * Remove an actor from the list. Synchronized because we don't want more
     * changes than this one at this time.
     *
     * @param el actor to be removed
     */
    public synchronized void removeActor(Element el) {
        actorsList.remove(el);
    }

    /**
     * Get the Element list.
     *
     * @return the element list
     */
    public List<Element> getElements() {
        return elList;
    }

    /**
     * Get the Actors list.
     *
     * @return the actors list
     */
    public List<Element> getActors() {
        return actorsList;
    }

    /**
     * Get the list of Explosion Rays.
     *
     * @return the Explosion Rays List.
     */
    public List<int[]> getRays() {
        return raysList;
    }

    /**
     * Clear the Rays List.
     */
    public void clearRays() {
        raysList.clear();
        fireStateChanged();
    }

    /**
     * Add a Ray to the list.
     *
     * @param ray the coordinates of the ray to be added
     */
    public void addRay(int[] ray) {
        raysList.add(ray);
        fireStateChanged();
    }

    /**
     * Get the current world.
     *
     * @return dat world
     */
    public World getWorld() {
        world = simulator.getWorld();
        return world;
    }
}