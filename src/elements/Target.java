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
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 *
 * @author Bart Middag
 */
public abstract class Target implements Element, ChangeListener {
    private float x;
    private float y;
    private float z;
    private float angle;
    private Color color;
    private final MainModel model;
    private final ConfigModel configModel;
    private final ElementModel elModel;
    private float density;
    private float restitution;
    private float friction;
    private float powerTreshold;
    private float strength;
    private boolean fragile;
    protected BodyDef blockBD;
    protected FixtureDef blockFD;
    protected PolygonShape blockShape;
    protected Body block;
    protected Fixture fixture;
    protected float speed;
    protected float breakForce;
    protected Color darkColor;
    
    public Target(float x, float y, float z, MainModel model, String type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = 0f;
        this.model = model;
        speed = 0f;
        breakForce = 0f;
        configModel = model.getConfigModel();
        elModel = configModel.getElementModel(type);
        density = elModel.getDensity();
        restitution = elModel.getRestitution();
        friction = elModel.getFriction();
        color = elModel.getColor();
        darkColor = elModel.getColor().darker().darker();
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
        
        blockShape = new PolygonShape();
        blockShape.setAsBox(z/2f, z/2f);
        
        blockFD = new FixtureDef();
        blockFD.shape = blockShape;
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
                if (configModel.isShowScenery()) model.getBonusModel().playCollision(3);
                unregister();
            }
        }
    }
    
    @Override
    public final void addBreakForce(float breakForce) {
        if(fragile&&(breakForce>powerTreshold)) {
            this.breakForce += breakForce;
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
    public final synchronized void register() {
        model.addElement(this);
        configModel.addChangeListener(this);
        elModel.addChangeListener(this);
        model.addTarget(this);
        model.addInit(this);
    }
    
    @Override
    public final synchronized void unregister() {
        if(block!=null) { model.getWorld().destroyBody(block); }
        elModel.removeChangeListener(this);
        model.removeTarget(this);
        model.removeActor(this);
        model.removeElement(this);
    }
    
    public final synchronized void targetBreak() {
        fragile = true;
        addBreakForce(powerTreshold+strength+1f);
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
        g.fillRect((int)Math.round(-z*7f/2f),(int)Math.round(-z*7f/2f),(int)Math.round(z*7f),(int)Math.round(z*7f));
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(elModel)) {
            density = elModel.getDensity();
            restitution = elModel.getRestitution();
            friction = elModel.getFriction();
            color = elModel.getColor();
            darkColor = elModel.getColor().darker().darker();
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
