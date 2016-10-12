/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels;

import bozels.elements.*;
import bozels.models.BonusModel;
import bozels.models.ConfigModel;
import bozels.models.MainModel;
import java.util.List;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * @author Bart Middag
 */
public class Simulation implements ChangeListener, Runnable {

    private final MainModel model;
    private final ConfigModel configModel;
    private World world;
    private JPanel panel;
    private boolean running;
    private boolean paused;
    private float dt = 1f / 60f; //seconden
    double gameHertz = 120.0;
    double timeBetweenUpdates = 1000000000 / gameHertz;
    final int maxUpdatesBeforeRender = 5;
    //If we are able to get as high as this FPS, don't render again.
    final double targetFPS = 62;
    final double targetTimeBetweenRenders = 1000000000 / targetFPS;
    private GroundBlock ground;

    /**
     * Initializes the Simulation object
     */
    public Simulation(MainModel model) {
        this.model = model;
        configModel = model.getConfigModel();
        panel = model.getGamePanel();
        newWorld(-9.81f);
        running = true;
        paused = true;
    }

    /**
     * Creates a new world
     *
     * @param gravity The desired gravity
     */
    public final synchronized World newWorld(float gravity) {
        world = new World(new Vec2(0f, gravity), true);
        ground = new GroundBlock(model);
        world.setContactListener(new ContactListener() {

            protected boolean show;

            @Override
            public void beginContact(Contact cnt) {
            }

            /**
             * Solve contacts.
             */
            @Override
            public void postSolve(Contact cnt, ContactImpulse imp) {

                int count = cnt.getManifold().pointCount;

                float maxImpulse = 0.0f;
                for (int i = 0; i < count; ++i) {
                    maxImpulse = Math.max(maxImpulse, imp.normalImpulses[i]);
                }
                Element elA = (Element)cnt.getFixtureA().getUserData();
                Element elB = (Element)cnt.getFixtureB().getUserData();
                if (elA != null) {
                    if (cnt.getFixtureA().getBody().isAwake()) {
                        if (configModel.isShowScenery() && (maxImpulse-cnt.getFixtureA().getDensity()*20 > 85f)) {
                            // Play collision sounds
                            if (elA instanceof WoodenBlock) {
                                model.getBonusModel().playCollision(2);
                            } else if ((elA instanceof ConcreteBlock) || (elA instanceof Bozel) || (elA instanceof Target)) {
                                model.getBonusModel().playCollision(0);
                            } else if (elA instanceof MetalBlock) {
                                model.getBonusModel().playCollision(1);
                            }
                        }
                        if ((elA instanceof Target) && (elB instanceof Bozel)) {
                            // Target hit by bozel - destroy dah targetz!
                            ((Target)elA).targetBreak();
                        }
                        elA.addBreakForce(maxImpulse * dt * 2);
                    }
                }
                if (elB != null) {
                    if (cnt.getFixtureB().getBody().isAwake()) {
                        if (configModel.isShowScenery() && (maxImpulse-cnt.getFixtureB().getDensity()*20 > 85f)) {
                            // Play collision sounds
                            if (elB instanceof WoodenBlock) {
                                model.getBonusModel().playCollision(2);
                            } else if ((elB instanceof ConcreteBlock) || (elB instanceof Bozel) || (elB instanceof Target)) {
                                model.getBonusModel().playCollision(0);
                            } else if (elB instanceof MetalBlock) {
                                model.getBonusModel().playCollision(1);
                            }
                        }
                        if ((elB instanceof Target) && (elA instanceof Bozel)) {
                            ((Target)elB).targetBreak();
                        }
                        elB.addBreakForce(maxImpulse * dt * 2);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        return world;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(model)) {
            panel = model.getGamePanel();
            paused = model.isPaused();
        }
        if (e.getSource().equals(model.getConfigModel())) {
            dt = model.getConfigModel().getTimeStep();
            world.setGravity(new Vec2(0f, model.getConfigModel().getGravity()));
            gameHertz = 1000 / model.getConfigModel().getGameSpeed();
            timeBetweenUpdates = 1000000000 / gameHertz;
        }
    }

    /**
     * Runs the simulation
     */
    @Override
    public void run() {
        model.setSimulator(this);
        model.addChangeListener(this);
        model.getConfigModel().addChangeListener(this);

        //We will need the last update time.
        double lastUpdateTime = System.nanoTime();
        //Store the last time we rendered.
        double lastRenderTime = System.nanoTime();
        while (running) {
            synchronized (model) {
                Stack<Element> stack = model.getInit();
                while (!stack.empty()) {
                    stack.peek().init();
                    model.popInit();
                }
            }

            double now = System.nanoTime();
            int updateCount = 0;
            //Do as many game updates as we need to, potentially playing catchup.
            while (now - lastUpdateTime > timeBetweenUpdates && updateCount < maxUpdatesBeforeRender) {
                // simuleer 1/60 seconden (Original values: dt,8,3)
                synchronized (this) {
                    if (!paused) {
                        world.step(dt, 20, 20);
                    }
                }
                lastUpdateTime += timeBetweenUpdates;
                updateCount++;
            }

            Bozel currentBozel = model.getCurrentBozel();
            Bozel lastBozel = model.getLastBozel();

            // Switch to next bozel at the right time
            if (model.getGamePanel() != null) {
                if (currentBozel != null) {
                    if (model.getGamePanel().isThrown()) {
                        if (!(currentBozel.isAwake()) && !(currentBozel instanceof WhiteBozel)) {
                            // Go to next bozel
                            model.nextBozel();
                            model.getGamePanel().setThrown(false);
                        }
                    }
                }
            }

            // Have you won the game yet, you loser?
            if (model.getWinState() == 0 && model.getTargetAmount() == 0) {
                model.setWinState(1);
            } else if (model.getWinState() == 0 && (currentBozel == null)) {
                boolean targetsAwake = false;
                List<Target> targetList = model.getTargets();
                for (Target ta : targetList) {
                    if (ta.isAwake()) {
                        targetsAwake = true;
                    }
                }
                if (!targetsAwake) {
                    if (lastBozel != null) {
                        if (!(lastBozel.isAwake())) {
                            model.setWinState(-1);
                        }
                    } else {
                        model.setWinState(-1);
                    }
                }
            }
            //System.out.println(model.getWinState()); //test

            // Update the music.
            BonusModel bonusModel = model.getBonusModel();

            if (configModel.isShowScenery()) {
                if (configModel.getEnvironment() == 0) {
                    if (bonusModel.getAmbienceVolume(0) < 1.0D) {
                        bonusModel.setAmbienceVolume(0, 1.0D);
                    }
                    if (bonusModel.getAmbienceVolume(1) > 0.0D) {
                        bonusModel.setAmbienceVolume(1, 0.0D);
                    }
                } else if (configModel.getEnvironment() == 1) {
                    if (bonusModel.getAmbienceVolume(1) < 1.0D) {
                        bonusModel.setAmbienceVolume(1, 1.0D);
                    }
                    if (bonusModel.getAmbienceVolume(0) > 0.0D) {
                        bonusModel.setAmbienceVolume(0, 0.0D);
                    }
                }
                if (bonusModel.getMusicVolume(1) < 1.0D) {
                    bonusModel.setMusicVolume(1, 1.0D);
                }
                if (!model.isPaused()) {
                    if (bonusModel.getMusicVolume(0) < 1.0D) {
                        bonusModel.setMusicVolume(0, 1.0D);
                    }
                } else {
                    if (bonusModel.getMusicVolume(0) > 0.0D) {
                        bonusModel.setMusicVolume(0, 0.0D);
                    }
                }
                if ((model.getNextBozel() == null) || (model.getTargetAmount() < 2)) {
                    if (bonusModel.getMusicVolume(2) < 1.0D) {
                        bonusModel.setMusicVolume(2, 1.0D);
                    }
                } else {
                    if (bonusModel.getMusicVolume(2) > 0.0D) {
                        bonusModel.setMusicVolume(2, 0.0D);
                    }
                }
            }
            if (!configModel.isShowScenery()) {
                if (bonusModel.getAmbienceVolume(0) > 0.0D) {
                    bonusModel.setAmbienceVolume(0, 0.0D);
                }
                if (bonusModel.getAmbienceVolume(1) > 0.0D) {
                    bonusModel.setAmbienceVolume(1, 0.0D);
                }
                if (bonusModel.getMusicVolume(2) > 0.0D) {
                    bonusModel.setMusicVolume(2, 0.0D);
                }
                if (bonusModel.getMusicVolume(1) > 0.0D) {
                    bonusModel.setMusicVolume(1, 0.0D);
                }
                if (bonusModel.getMusicVolume(0) > 0.0D) {
                    bonusModel.setMusicVolume(0, 0.0D);
                }
            }

            //If for some reason an update takes forever, we don't want to do an insane number of catchups.
            //If we would be doing something that needed to keep EXACT time, you would get rid of this.
            if (lastUpdateTime - now > timeBetweenUpdates) {
                lastUpdateTime = now - timeBetweenUpdates;
            }
            if (now - lastRenderTime >= targetTimeBetweenRenders) {
                // Update elements
                synchronized (model) {
                    for (int i = 0; i < model.getElements().size(); i++) {
                        model.getElements().get(i).update();
                    }
                }
                // Repaint panel
                try {
                    panel.repaint();
                } catch (NullPointerException ex) {
                }
                lastRenderTime = now;
            }

            //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
            while (now - lastRenderTime < targetTimeBetweenRenders && now - lastUpdateTime < timeBetweenUpdates) {
                Thread.yield();
                //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it!
                //I could remove this line and it will still work (better), but your CPU just climbs on certain OSes.
                //On some OSes this can cause pretty bad stuttering.
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
                now = System.nanoTime();
            }
        }
    }

    /**
     * Returns the current world
     */
    public World getWorld() {
        return world;
    }
}
