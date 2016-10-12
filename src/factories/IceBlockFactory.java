/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.IceBlock;
import bozels.elements.Block;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class IceBlockFactory extends BlockFactory {
    
    public IceBlockFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Block create(float x, float y, float width, float height, float angle, boolean ellipse) {
        return new IceBlock(x,y,width,height,(float)Math.toRadians(angle),ellipse,model);
    }
}