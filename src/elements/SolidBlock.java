/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import bozels.models.MainModel;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Bart Middag
 */
public class SolidBlock extends Block {
    public SolidBlock(float x, float y, float width, float height, float angle, boolean ellipse, MainModel model) {
        super(x,y,width,height,angle,ellipse,model,"solidBlock");
    }
    @Override
    public void init() {
        blockBD = new BodyDef();
        blockBD.position.set(new Vec2(x,y));
        blockBD.angle = angle;
        blockBD.type = BodyType.STATIC;
        block = model.getWorld().createBody(blockBD);
        
        if(ellipse) {
            ellipseShape = new CircleShape();
            ellipseShape.m_radius = width/2f;
            shape = ellipseShape;
        } else {
            blockShape = new PolygonShape();
            blockShape.setAsBox(width/2f, height/2f);
            shape = blockShape;
        }
        
        blockFD = new FixtureDef();
        blockFD.shape = shape;
        
        fixture = block.createFixture(blockFD);
        fixture.setUserData(this);
        model.addActor(this);
    }
}
