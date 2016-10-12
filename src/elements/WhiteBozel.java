/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import bozels.ExplosionRayCastCallback;
import bozels.GameWindow;
import bozels.models.MainModel;
import java.awt.Graphics;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * The White Bozel
 * @author Bart Middag
 */
public class WhiteBozel extends Bozel {

    private float z;

    public WhiteBozel(float x, float y, int id, MainModel model) {
        super(x, y, id, model, "whiteBozel", 1f);
        z = 2f;
    }

    @Override
    public synchronized void init() {
        blockBD = new BodyDef();
        blockBD.position.set(new Vec2(x, y));
        blockBD.angle = angle;
        blockBD.type = BodyType.DYNAMIC;
        block = model.getWorld().createBody(blockBD);

        blockShape = new PolygonShape();
        blockShape.setAsBox(z / 2f, z / 2f);

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
        super.draw(g, showScenery);
        if (showScenery) {
            g.setColor(darkColor);
        } else {
            g.setColor(color);
        }
        g.fillRect((int) (-7 * z / 2), (int) (-7 * z / 2), (int) (7 * z), (int) (7 * z));
    }

    @Override
    public void doSpecial() {
        if (block != null) {
            model.clearRays();
            Vec2 point1 = block.getPosition();
            // Raycasting happens synchronized with the simulator in order to make it more accurate.
            synchronized (model.getSimulator()) {
                for (float i = 0; i < 360; i += 4) {
                    ExplosionRayCastCallback exp = new ExplosionRayCastCallback(model,block.getPosition());
                    Vec2 angleVec = new Vec2(10.0f * (float) Math.cos(i * (Math.PI / 180f)), 10.0f * (float) Math.sin(i * (Math.PI / 180f)));
                    Vec2 point2 = point1.add(angleVec);
                    model.getWorld().raycast(exp, point1, point2);
                    Vec2 rayVec = exp.explode();
                    Vec2 hitpoint = exp.getHitPoint();
                    if (hitpoint != null) {
                        int[] ray = {(int) Math.round(hitpoint.x * 7f), 450 - (int) Math.round(hitpoint.y * 7f), (int) Math.round((hitpoint.x + rayVec.x) * 7f), 450 - (int) Math.round((hitpoint.y + rayVec.y * 7f))};
                        model.addRay(ray);
                    }
                }
            }
            // Start explosion effect
            ((GameWindow) (model.getGamePanel())).startExplode(Math.round(x * 7f), 450 - Math.round(y * 7f));
            
            // Destroy this object.
            synchronized (model) {
                unregister();
            }
        }
    }
}
