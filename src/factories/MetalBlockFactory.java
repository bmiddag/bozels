/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.MetalBlock;
import bozels.elements.Block;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class MetalBlockFactory extends BlockFactory {
    
    public MetalBlockFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Block create(float x, float y, float width, float height, float angle, boolean ellipse) {
        return new MetalBlock(x,y,width,height,(float)Math.toRadians(angle),ellipse,model);
    }
}