/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels;

import bozels.elements.*;
import bozels.models.MainModel;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

/**
 * @author Bart Middag
 */
public class ExplosionRayCastCallback implements RayCastCallback {

    private Vec2 bomb;
    private Vec2 hitpoint;
    private Fixture closest;
    private float minf = 1.0f;
    private final MainModel model;

    public ExplosionRayCastCallback(MainModel model, Vec2 bomb) {
        this.model = model;
        //bomb location
        this.bomb = bomb.clone();
    }

    /**
     * Reports the closest fixture to the raycast function.
     * @param fixture The Raycasting fixture
     */
    @Override
    public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
        //The fraction is a number between 0 and 1 showing where the ray found this hit.
        //We only want the closest hit, so...
        if (fraction < minf) {
            minf = fraction;
            closest = fixture;
            hitpoint = point.clone();
        }

        //Limit this ray to hits closer than this one
        return fraction;
    }

    /**
     * Applies forces after the Raycast function.
     */
    public Vec2 explode() {
        if (hitpoint != null) {
            final float maxForce = 20000f;
            //Direction of the force = vector between bomb and point point
            Vec2 fDir = hitpoint.sub(bomb);

            //Determine the distance between bomb and hitpoint and make fDir length 1
            float dist = fDir.normalize();

            // What kind of object is this?
            Element el = ((Element) (closest.getUserData()));
            //Apply a force on the hit object
            //The direction is fDir, the size declines as the distance gets bigger (max for dist=0)
            if (model.getConfigModel().isShowScenery() && (maxForce / (dist + 1) > 85f)) {
                // Play collision sounds
                if (el instanceof WoodenBlock) {
                    model.getBonusModel().playCollision(2);
                } else if ((el instanceof ConcreteBlock) || (el instanceof Bozel) || (el instanceof Target)) {
                    model.getBonusModel().playCollision(0);
                } else if (el instanceof MetalBlock) {
                    model.getBonusModel().playCollision(1);
                }
            }
            closest.getBody().applyForce(fDir.mul(maxForce / (dist + 1)), hitpoint);
            el.addBreakForce(maxForce / (200f * (dist + 1)));

            return fDir.mul(maxForce / (200f * (dist + 1)));
        } else {
            return null;
        }
    }

    /**
     * Get the hit point of the ray.
     * @return the hit point vector
     */
    public Vec2 getHitPoint() {
        return hitpoint;
    }
}
