/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.Target;
import bozels.elements.SmallTarget;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class SmallTargetFactory extends TargetFactory {
    
    public SmallTargetFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Target create(float x, float y) {
        return new SmallTarget(x,y,model);
    }
}