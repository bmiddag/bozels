/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import bozels.models.MainModel;
import java.awt.Color;
import java.awt.Graphics;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Bart Middag
 */
public class BlueBozel extends Bozel {
    private float r;
    /**
     * Create a blue Bozel.
     * @param x The X coordinate of the Bozel
     * @param y The Y coordinate of the Bozel
     * @param id The id of the Bozel
     * @param model The main model
     */
    public BlueBozel(float x, float y, int id, MainModel model) {
        super(x,y,id,model,"blueBozel",1f);
        r = 1f;
    }
    /**
     * {@inheritDoc}
     */    
    @Override
    public synchronized void init() {
        blockBD = new BodyDef();
        blockBD.position.set(new Vec2(x,y));
        blockBD.angle = angle;
        blockBD.type = BodyType.DYNAMIC;
        block = model.getWorld().createBody(blockBD);
        
        ellipseShape = new CircleShape();
        ellipseShape.m_radius = r;
        
        blockFD = new FixtureDef();
        blockFD.shape = ellipseShape;
        blockFD.density = elModel.getDensity();
        blockFD.friction = elModel.getFriction();
        blockFD.restitution = elModel.getRestitution();
        
        fixture = block.createFixture(blockFD);
        fixture.setUserData(this);
        
        launch(block);
        model.addActor(this);
    }
    /**
     * {@inheritDoc}
     * @param g The graphics context
     * @param showScenery Is the Magic Button active?
     */
    @Override
    public void draw(Graphics g, boolean showScenery) {
        super.draw(g,showScenery);
        if(showScenery) {
            g.setColor(darkColor);
        } else {
            g.setColor(color);
        }
        g.fillOval((int)(-7*r),(int)(-7*r),(int)(14*r),(int)(14*r));
        //lolol;
    }
}
