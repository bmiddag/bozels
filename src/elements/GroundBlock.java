/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import bozels.models.MainModel;
import javax.swing.event.ChangeEvent;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Bart Middag
 */
public class GroundBlock extends Block {
    public GroundBlock(MainModel model) {
        super(73.15f,-1000f,2000f,2000f,0,false,model,"solidBlock");
        fragile = false;
    }
    @Override
    public void init() {
        blockBD = new BodyDef();
        blockBD.position.set(new Vec2(x,y));
        blockBD.angle = angle;
        blockBD.type = BodyType.STATIC;
        block = model.getWorld().createBody(blockBD);
        block.setUserData(this);
        
        blockShape = new PolygonShape();
        blockShape.setAsBox(width/2f, height/2f);
        
        blockFD = new FixtureDef();
        blockFD.shape = blockShape;
        
        fixture = block.createFixture(blockFD);
        fixture.setUserData(this);
        model.addActor(this);
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        super.stateChanged(e);
        fragile = false;
    }
}
