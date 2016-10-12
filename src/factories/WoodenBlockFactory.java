/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.Block;
import bozels.elements.WoodenBlock;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class WoodenBlockFactory extends BlockFactory {
    
    public WoodenBlockFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Block create(float x, float y, float width, float height, float angle, boolean ellipse) {
        return new WoodenBlock(x,y,width,height,(float)Math.toRadians(angle),ellipse,model);
    }
}