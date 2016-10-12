/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import bozels.models.ConfigModel;
import bozels.models.ElementModel;
import bozels.models.MainModel;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 *
 * @author Bart Middag
 */
public abstract class Block implements Element, ChangeListener {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float angle;
    protected boolean ellipse;
    protected Color color;
    protected final MainModel model;
    protected final ConfigModel configModel;
    protected final ElementModel elModel;
    protected float density;
    protected float restitution;
    protected float friction;
    protected float powerTreshold;
    protected float strength;
    protected boolean fragile;
    protected BodyDef blockBD;
    protected FixtureDef blockFD;
    protected PolygonShape blockShape;
    protected CircleShape ellipseShape;
    protected Body block;
    protected Fixture fixture;
    protected Shape shape;
    protected float speed;
    protected float breakForce;
    protected Color darkColor;
    
    public Block(float x, float y, float width, float height, float angle, boolean ellipse, MainModel model, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.ellipse = ellipse;
        //this.color = color;
        this.model = model;
        speed = 0f;
        breakForce = 0f;
        configModel = model.getConfigModel();
        elModel = configModel.getElementModel(type);
        density = elModel.getDensity();
        restitution = elModel.getRestitution();
        friction = elModel.getFriction();
        color = elModel.getColor();
        darkColor = elModel.getColor().darker().darker().darker().darker().darker();
        powerTreshold = elModel.getPowerThreshold();
        strength = elModel.getStrength();
        fragile = elModel.isFragile();
        register();
    }
    @Override
    public synchronized void init() {
        blockBD = new BodyDef();
        blockBD.position.set(new Vec2(x,y));
        blockBD.angle = angle;
        blockBD.type = BodyType.DYNAMIC;
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
        blockFD.density = elModel.getDensity();
        blockFD.friction = elModel.getFriction();
        blockFD.restitution = elModel.getRestitution();
        
        fixture = block.createFixture(blockFD);
        fixture.setUserData(this);
        model.addActor(this);
    }
    
    @Override
    public void update() {
        if(block!=null) {
            x = block.getPosition().x;
            y = block.getPosition().y;
            angle = block.getAngle();
            speed = block.getLinearVelocity().length();
            if(this.breakForce > strength) {
                unregister();
            }
        }
    }
    
    @Override
    public float getSpeedX() {
        if(block!=null) {
            return x+block.getLinearVelocity().x;
        } else {
            return x;
        }
    }
    
    @Override
    public float getSpeedY() {
        if(block!=null) {
            return y+block.getLinearVelocity().y;
        } else {
            return y;
        }
    }
    
    @Override
    public float getSpeed() {
        return speed;
    }
    
    @Override
    public boolean isAwake() {
        if(block!=null) {
            return block.isAwake();
        } else {
            return false;
        }
    }
    
    @Override
    public final void addBreakForce(float breakForce) {
        //System.out.println("This Break Force: " + breakForce + " - Total Break Force: " + this.breakForce + " - Block Strength: " + strength + " - Power Treshold: " + powerTreshold);
        if(fragile&&(breakForce>powerTreshold)) {
            this.breakForce += breakForce;
        }
    }
    
    @Override
    public final synchronized void register() {
        model.addElement(this);
        configModel.addChangeListener(this);
        elModel.addChangeListener(this);
        model.addInit(this);
    }
    
    @Override
    public final synchronized void unregister() {
        if(block!=null) { model.getWorld().destroyBody(block); }
        elModel.removeChangeListener(this);
        model.removeActor(this);
        model.removeElement(this);
    }
    
    @Override
    public float getX() {
        return x;
    }
    
    @Override
    public float getY() {
        return y;
    }
    
    @Override
    public float getAngle() {
        return angle;
    }
    
    @Override
    public void draw(Graphics g, boolean showScenery) {
        if(showScenery) {
            g.setColor(darkColor);
        } else {
            g.setColor(color);
        }
        if (ellipse) {
            g.fillOval((int)Math.round(-width*7f/2f),(int)Math.round(-height*7f/2f),(int)Math.round(width*7f),(int)Math.round(height*7f));
        } else {
            g.fillRect((int)Math.round(-width*7f/2f),(int)Math.round(-height*7f/2f),(int)Math.round(width*7f),(int)Math.round(height*7f));
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(elModel)) {
            density = elModel.getDensity();
            restitution = elModel.getRestitution();
            friction = elModel.getFriction();
            color = elModel.getColor();
            darkColor = elModel.getColor().darker().darker().darker().darker().darker();
            powerTreshold = elModel.getPowerThreshold();
            strength = elModel.getStrength();
            fragile = elModel.isFragile();
            
            if(block!=null) {
                fixture.setDensity(density);
                fixture.setRestitution(restitution);
                fixture.setFriction(friction);
                block.setAwake(true);
                block.resetMassData();
            }
        }
        if ((e.getSource().equals(configModel)) && (block!=null)) {
            block.setAwake(true);
        }
    }
}
