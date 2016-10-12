/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.models;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * The Configuration Model
 * @author Bart Middag
 */
public class ConfigModel extends Model {
    private final MainModel mainModel;
    private float gravity;
    private float timeStep;
    private float gameSpeed;
    private float launchPower;
    private boolean showCentroid;
    private boolean showSpeed;
    private boolean markSleeping;
    private boolean showRays;
    private Map<String, ElementModel> elementModelMap;
    private final Action showCentroidAction;
    private final Action showSpeedAction;
    private final Action markSleepingAction;
    private final Action showRaysAction;
    private boolean showScenery;
    private final Action showSceneryAction;
    private int environment;
    private final Random environmentRandom;

    /**
     * Create the Configuration Model.
     * @param model The Main Model.
     */
    public ConfigModel(MainModel model) {
        this.mainModel = model;
        environmentRandom = new Random();
        elementModelMap = new HashMap<String, ElementModel>();
        createElementModels();
        setDefaultConfig();
        
        // Create actions
        showCentroidAction = new AbstractAction(model.getLocaleBundle().getString("PROPERTY_SHOW_CENTROID")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setShowCentroid(!isShowCentroid());
            }
        };
        showCentroidAction.putValue(Action.SELECTED_KEY, showCentroid);
        showSpeedAction = new AbstractAction(model.getLocaleBundle().getString("PROPERTY_SHOW_SPEED")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setShowSpeed(!isShowSpeed());
            }
        };
        showSpeedAction.putValue(Action.SELECTED_KEY, showSpeed);
        markSleepingAction = new AbstractAction(model.getLocaleBundle().getString("PROPERTY_MARK_SLEEPING")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMarkSleeping(!isMarkSleeping());
            }
        };
        markSleepingAction.putValue(Action.SELECTED_KEY, markSleeping);
        showRaysAction = new AbstractAction(model.getLocaleBundle().getString("PROPERTY_SHOW_RAYS")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setShowRays(!isShowRays());
            }
        };
        showRaysAction.putValue(Action.SELECTED_KEY, showRays);
        
        showSceneryAction = new AbstractAction(model.getLocaleBundle().getString("PROPERTY_SHOW_SCENERY")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setShowScenery(!isShowScenery());
            }
        };
        showSceneryAction.putValue(Action.SELECTED_KEY, showScenery);
        showSceneryAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
    }
    
    /**
     * Create the Element Models.
     */
    public final void createElementModels() {
        elementModelMap.put("redBozel", new RedBozelModel(this));
        elementModelMap.put("blueBozel", new BlueBozelModel(this));
        elementModelMap.put("whiteBozel", new WhiteBozelModel(this));
        elementModelMap.put("yellowBozel", new YellowBozelModel(this));
        elementModelMap.put("bigTarget", new BigTargetModel(this));
        elementModelMap.put("smallTarget", new SmallTargetModel(this));
        elementModelMap.put("solidBlock", new SolidBlockModel(this));
        elementModelMap.put("concreteBlock", new ConcreteBlockModel(this));
        elementModelMap.put("woodenBlock", new WoodenBlockModel(this));
        elementModelMap.put("metalBlock", new MetalBlockModel(this));
        elementModelMap.put("iceBlock", new IceBlockModel(this));
    }
    
    /**
     * Set the default configuration for this model.
     */
    public final void setDefaultConfig() {
        gravity = -9.81f;
        timeStep = 0.016f;
        gameSpeed = 8f;
        launchPower = 50000.0f;
        showCentroid = false;
        showSpeed = false;
        markSleeping = false;
        showRays = false;
        showScenery = false;
        fireStateChanged();
    }
    
    /**
     * Get the Show Centroid Action
     * @return the show centroid action
     */
    public Action getShowCentroidAction() {
        return showCentroidAction;
    }
    
    /**
     * Get the Show Speed Action
     * @return the show speed action
     */
    public Action getShowSpeedAction() {
        return showSpeedAction;
    }
    
    /**
     * Get the Mark Sleeping Objects Action
     * @return the mark sleeping objects action
     */
    public Action getMarkSleepingAction() {
        return markSleepingAction;
    }
    
    /**
     * Get the Show Rays Action
     * @return the show rays action
     */
    public Action getShowRaysAction() {
        return showRaysAction;
    }
    
    /**
     * Get the Show Scenery (Magic Button) Action
     * @return the show scenery action
     */
    public Action getShowSceneryAction() {
        return showSceneryAction;
    }
    
    /**
     * Get the current environment.
     * @return 0 for nighttime, 1 for sea
     */
    public int getEnvironment() {
        return environment;
    }
    
    /**
     * Pick a new environment. (0 for nighttime, 1 for sea)
     */
    public void newEnvironment() {
        environment = environmentRandom.nextInt(2);
        fireStateChanged();
    }
    
    /**
     * Get the Main Model, King of All Models
     * @return The Main Model, King of All Models
     */
    public MainModel getMainModel() {
        return mainModel;
    }

    /**
     * Get the current gravity
     * @return the current gravity
     */
    public float getGravity() {
        return gravity;
    }

    /**
     * Change the gravity
     * @param gravity the new gravity
     */
    public void setGravity(float gravity) {
        if (gravity != this.gravity) {
            this.gravity = gravity;
            fireStateChanged();
        }
    }

    /**
     * Get the current time step
     * @return the time step
     */
    public float getTimeStep() {
        return timeStep;
    }

    /**
     * Change the time step
     * @param timeStep the new time step
     */
    public void setTimeStep(float timeStep) {
        if (timeStep != this.timeStep) {
            this.timeStep = timeStep;
            fireStateChanged();
        }
    }

    /**
     * Get the current game speed
     * @return the current game speed
     */
    public float getGameSpeed() {
        return gameSpeed;
    }

    /**
     * Change the game speed
     * @param gameSpeed the new game speed
     */
    public void setGameSpeed(float gameSpeed) {
        if (gameSpeed != this.gameSpeed) {
            this.gameSpeed = gameSpeed;
            fireStateChanged();
        }
    }

    /**
     * Get the current launch power
     * @return the current launch power
     */
    public float getLaunchPower() {
        return launchPower;
    }

    /**
     * Change the launch power
     * @param launchPower the new launch power
     */
    public void setLaunchPower(float launchPower) {
        if (launchPower != this.launchPower) {
            this.launchPower = launchPower;
            fireStateChanged();
        }
    }

    /**
     * Are we showing the centroid?
     * @return whether we're showing the centroid
     */
    public boolean isShowCentroid() {
        return showCentroid;
    }

    /**
     * Show or hide the centroid
     * @param showCentroid whether you want to show the centroid
     */
    public void setShowCentroid(boolean showCentroid) {
        if (showCentroid != this.showCentroid) {
            this.showCentroid = showCentroid;
            fireStateChanged();
        }
    }

    /**
     * Are we showing the speed vector?
     * @return whether we're showing the speed vector
     */
    public boolean isShowSpeed() {
        return showSpeed;
    }

    /**
     * Show or hide the speed vector.
     * @param showSpeed whether you want to show the speed vector or not
     */
    public void setShowSpeed(boolean showSpeed) {
        if (showSpeed != this.showSpeed) {
            this.showSpeed = showSpeed;
            fireStateChanged();
        }
    }

    /**
     * Are we marking sleeping objects?
     * @return whether we're marking sleeping objects
     */
    public boolean isMarkSleeping() {
        return markSleeping;
    }

    /**
     * Mark or unmark sleeping objects.
     * @param markSleeping whether or not you want to mark sleeping objects
     */
    public void setMarkSleeping(boolean markSleeping) {
        if (markSleeping != this.markSleeping) {
            this.markSleeping = markSleeping;
            fireStateChanged();
        }
    }

    /**
     * Are we showing the explosion rays?
     * @return whether or not we're showing the explosion rays
     */
    public boolean isShowRays() {
        return showRays;
    }

    /**
     * Show or hide the explosion rays.
     * @param showRays whether or not you want to show the explosion rays
     */
    public void setShowRays(boolean showRays) {
        if (showRays != this.showRays) {
            this.showRays = showRays;
            fireStateChanged();
        }
    }
    
    /**
     * Is the extra stuff active or not?
     * @return whether the scenery is active
     */
    public boolean isShowScenery() {
        return showScenery;
    }
    
    /**
     * Show or hide the extra stuff
     * @param showScenery whether or not we want to show the extra stuff
     */
    public void setShowScenery(boolean showScenery) {
        if (showScenery != this.showScenery) {
            this.showScenery = showScenery;
            fireStateChanged();
        }
    }
    
    /**
     * Get the correct element model with the given type.
     * @param type the string describing the element
     * @return the correct element model
     */
    public ElementModel getElementModel(String type) {
        return elementModelMap.get(type);
    }
    
    /**
     * Get all element models.
     * @return the element model map
     */
    public Map getElementModels() {
        return elementModelMap;
    }
    
}
