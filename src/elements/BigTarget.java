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
public class BigTarget extends Target {
    public BigTarget(float x, float y, MainModel model) {
        super(x,y,4f,model,"bigTarget");
    }
}
