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
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

/**
 * A Bozel Element
 * @author Bart Middag
 */
public abstract class Bozel implements Element, ChangeListener {

    protected float x;
    protected float y;
    protected int id;
    protected final MainModel model;
    protected final ConfigModel configModel;
    protected final ElementModel elModel;
    protected float density;
    protected float restitution;
    protected float friction;
    protected Color color;
    protected float powerTreshold;
    protected float strength;
    protected boolean fragile;
    protected float angle;
    protected final float range;
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

    /**
     * Create new Bozel.
     */
    public Bozel(float x, float y, int id, MainModel model, String type, float range) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.angle = 0;
        this.range = range;
        this.model = model;
        fragile = false;
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
        if (id == 1) {
            model.setCatapultPosition(x, y + 10f);
        }
        register();
    }

    /**
     * Launch that sucker into space! Yes.
     */
    public final void launch(Body body) {
        Vec2 curPos = new Vec2(x, y);
        Vec2 cataPos = new Vec2(model.getCatapultX(), model.getCatapultY());
        Vec2 rd = cataPos.sub(curPos);
        float length = Math.min(rd.length(), 7f);
        rd.normalize();
        Vec2 el = rd.mul(length * configModel.getLaunchPower() / 7f);
        body.setAngularVelocity((x - model.getCatapultX()) / 2f);
        body.applyForce(el, new Vec2(x, y));
    }

    /**
     * Register the Bozel.
     */
    @Override
    public final synchronized void register() {
        model.addElement(this);
        configModel.addChangeListener(this);
        elModel.addChangeListener(this);
        model.addBozel(id, this);
    }

    /**
     * Destroy the Bozel.
     */
    @Override
    public final synchronized void unregister() {
        if (block != null) {
            model.getWorld().destroyBody(block);
        }
        elModel.removeChangeListener(this);
        model.removeBozel(id);
        model.removeActor(this);
        model.removeElement(this);
    }

    /**
     * To the catapult with you! Indeed.
     */
    public final void toCatapult() {
        x = model.getCatapultX();
        y = model.getCatapultY();
    }

    /**
     * Get the X coordinate of the Bozel.
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * Get the Y coordinate of the Bozel.
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     * Bozels cannot break - unused method.
     */
    @Override
    public final void addBreakForce(float breakForce) {
        //if(fragile&&(breakForce>powerTreshold)) {
        //    this.breakForce += breakForce;
        //}
    }

    /**
     * Update the Bozel's coordinates, angle, etc.
     */
    @Override
    public void update() {
        if (block != null) {
            x = block.getPosition().x;
            y = block.getPosition().y;
            angle = block.getAngle();
            speed = block.getLinearVelocity().length();
            //if(this.breakForce > strength) {
            //    unregister();
            //}
        }
    }

    /**
     * Get the X coordinate of the speed vector.
     */
    @Override
    public float getSpeedX() {
        if (block != null) {
            return x + block.getLinearVelocity().x;
        } else {
            return x;
        }
    }

    /**
     * Get the Y coordinate of the speed vector.
     */
    @Override
    public float getSpeedY() {
        if (block != null) {
            return y + block.getLinearVelocity().y;
        } else {
            return y;
        }
    }

    /**
     * Get the length of the speed vector.
     */
    @Override
    public float getSpeed() {
        return speed;
    }

    /**
     * Get the fixture of this bozel.
     */
    public Fixture getFixture() {
        return fixture;
    }

    /**
     * Do this Bozel's special action.
     */
    public void doSpecial() {
        // do nothing, default
    }

    /**
     * Check is this Bozel is awake.
     */
    @Override
    public boolean isAwake() {
        if (block != null) {
            return block.isAwake();
        } else {
            return model.getCurrentBozel().equals(this);
        }
    }

    /**
     * Sets this Bozel to the mouse position.
     */
    public void setMousePos(float newX, float newY) {
        float dragX = newX / 7f;
        float dragY = -(newY - 450) / 7f;
        Vec2 curPos = new Vec2(dragX, dragY);
        Vec2 cataPos = new Vec2(model.getCatapultX(), model.getCatapultY());
        Vec2 rd = cataPos.sub(curPos);
        float length = Math.min(rd.length(), 7f);
        rd.normalize();
        Vec2 el = rd.mul(-length);
        x = model.getCatapultX() + el.x;
        y = model.getCatapultY() + el.y;
    }

    /**
     * Get this Bozel's angle.
     */
    @Override
    public float getAngle() {
        return angle;
    }

    /**
     * Draw this Bozel.
     */
    @Override
    public void draw(Graphics g, boolean showScenery) {
        //nevermind
    }

    /**
     * Is this Bozel in range of the mouse? Check it.
     */
    public boolean isInRange(float xPos, float yPos) {
        return ((Math.pow(xPos - (x * 7f), 2) + Math.pow(yPos - (450f - (y * 7f)), 2)) <= (Math.pow(range * 7f, 2)));
    }

    /**
     * Update values using the models.
     */
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

            if (block != null) {
                fixture.setDensity(density);
                fixture.setRestitution(restitution);
                fixture.setFriction(friction);
                block.setAwake(true);
                block.resetMassData();
            }
        }
        if ((e.getSource().equals(configModel)) && (block != null)) {
            block.setAwake(true);
        }
    }
}
