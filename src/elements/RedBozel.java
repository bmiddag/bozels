/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import bozels.models.MainModel;
import java.awt.Graphics;
import java.awt.Polygon;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Bart Middag
 */
public class RedBozel extends Bozel {
    private float z;
    private float height;
    private final Polygon triangle;
    
    public RedBozel(float x, float y, int id, MainModel model) {
        super(x,y,id,model,"redBozel",2f);
        z = 2f;
        height = z*(float)Math.sqrt(3f)/6f;
        triangle = new Polygon();
        triangle.addPoint(0,(int)(height*14f));
        triangle.addPoint((int)(-(z*7f)/2f),(int)(-height*7f));
        triangle.addPoint((int)((z*7f)/2f),(int)(-height*7f));
    }
        
    @Override
    public synchronized void init() {
        blockBD = new BodyDef();
        blockBD.position.set(new Vec2(x,y));
        blockBD.angle = angle;
        blockBD.type = BodyType.DYNAMIC;
        block = model.getWorld().createBody(blockBD);
        
        blockShape = new PolygonShape();
        blockShape.set(new Vec2[]{
            new Vec2(0f,height*2f),
            new Vec2(-z/2f,-height),
            new Vec2(z/2f,-height)
        },3);
        
        blockFD = new FixtureDef();
        blockFD.shape = blockShape;
        blockFD.density = elModel.getDensity();
        blockFD.friction = elModel.getFriction();
        blockFD.restitution = elModel.getRestitution();
        
        fixture = block.createFixture(blockFD);
        fixture.setUserData(this);
        
        launch(block);
        model.addActor(this);
    }
    
    @Override
    public void draw(Graphics g, boolean showScenery) {
        super.draw(g,showScenery);
        if(showScenery) {
            g.setColor(darkColor);
        } else {
            g.setColor(color);
        }
        g.drawPolygon(triangle);
        //lolol;
    }
}
