/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.SolidBlock;
import bozels.elements.Block;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class SolidBlockFactory extends BlockFactory {
    
    public SolidBlockFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Block create(float x, float y, float width, float height, float angle, boolean ellipse) {
        return new SolidBlock(x,y,width,height,(float)Math.toRadians(angle),ellipse,model);
    }
}