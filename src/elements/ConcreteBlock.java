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
public class ConcreteBlock extends Block {
    public ConcreteBlock(float x, float y, float width, float height, float angle, boolean ellipse, MainModel model) {
        super(x,y,width,height,angle,ellipse,model,"concreteBlock");
    }
}
