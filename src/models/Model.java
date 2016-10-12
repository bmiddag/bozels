/* Model.java
 * ============================================================
 * Copyright (C) 2001-2012 Ghent University
 * 
 * An example used in the 'Programming 2' course.
 * 
 * Authors: Kris Coolsaet & Bart Middag
 */

package bozels.models;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Common super class of all models.
 */
public class Model {
    
    /**
     * List of registered listeners.
     */
    private EventListenerList listenerList = new EventListenerList ();
    
    /**
     * Registers a listener.
     */
    public void addChangeListener (ChangeListener l) {
        listenerList.add (ChangeListener.class, l);
    }
    
    /**
     * Unregisters a listener.
     */
    public void removeChangeListener (ChangeListener l) {
        listenerList.remove (ChangeListener.class, l);
    }
    
    /**
     * Unique change event with this object as the source.
     */
    private final ChangeEvent changeEvent = new ChangeEvent (this);
    
    /**
     * Process a new change event coming from this object by passing a new
     * event to all of the listeners. The new event has this model as the source.
     */
    protected void fireStateChanged () {
        Object[] listeners = listenerList.getListenerList ();
        for (int i=listeners.length-2; i >= 0; i-=2) {
            if (listeners[i] == ChangeListener.class)
                ((ChangeListener)listeners[i+1]).stateChanged (changeEvent);
        }
    } 
}
