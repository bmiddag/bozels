/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class SmallTarget extends Target {
    public SmallTarget(float x, float y, MainModel model) {
        super(x,y,2.5f,model,"smallTarget");
    }
}
