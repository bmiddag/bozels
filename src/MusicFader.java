/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels;

import bozels.models.Model;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.FloatControl;

/**
 * @author Middag
 */
public class MusicFader implements Runnable {

    /**
     * Fade the volume to a new value. To shift volume while sound is playing,
     * ie. to simulate motion to or from an object, the volume has to change
     * smoothly in a short period of time. Unfortunately this makes an annoying
     * clicking noise, mostly noticeable in the browser. I reduce the click by
     * fading the volume in small increments with delays in between. This means
     * that you can't change the volume very quickly. The fade has to to take a
     * second or two to prevent clicks.
     */
    private float currDB = 0F;
    private float targetDB = 0F;
    private float fadePerStep = 0.04F;   // .1 works for applets, 1 is okay for apps
    boolean fading = false;
    private final FloatControl gainControl;
    private final Method setMethod;
    private final Model contModel;
    private final int track;

    public MusicFader(int track, float currDB, float targetDB, FloatControl gainControl, Model contModel, Method setMethod) {
        this.track = track;
        this.currDB = currDB;
        this.targetDB = targetDB;
        this.gainControl = gainControl;
        this.contModel = contModel;
        this.setMethod = setMethod;
    }

    /**
     * Run by thread, this will step the volume up or down to a target level.
     * Applets need fadePerStep=.1 to minimize clicks. Apps can get away with
     * fadePerStep=1.0 for a faster fade with no clicks.
     */
    @Override
    public void run() {
        if (currDB > targetDB) {
            while (currDB > targetDB) {
                currDB -= fadePerStep;
                // The value ranges from -80dB (no sound) to 0dB (normal sound)
                gainControl.setValue(Math.round((currDB - 1f) * 80f));
                Thread.yield();
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        } else if (currDB < targetDB) {
            while (currDB < targetDB) {
                currDB += fadePerStep;
                gainControl.setValue(Math.round((currDB - 1f) * 80f));
                Thread.yield();
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        }
        currDB = targetDB;  // now sound is at this volume level
        try {
            setMethod.invoke(contModel, track, false);
            //gainControl.setValue(currDB);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MusicFader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MusicFader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MusicFader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
