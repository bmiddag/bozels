/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.BigTarget;
import bozels.elements.Target;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class BigTargetFactory extends TargetFactory {

    public BigTargetFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Target create(float x, float y) {
        return new BigTarget(x,y,model);
    }
}