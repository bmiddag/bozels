/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.elements;

import bozels.models.MainModel;
import java.awt.Graphics;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * The Yellow Bozel
 * @author Bart Middag
 */
public class YellowBozel extends Bozel {

    private float h;
    private float b;

    public YellowBozel(float x, float y, int id, MainModel model) {
        super(x, y, id, model, "yellowBozel", 1f);
        h = 1f;
        b = 2f;
    }

    @Override
    public synchronized void init() {
        blockBD = new BodyDef();
        blockBD.position.set(new Vec2(x, y));
        blockBD.angle = angle;
        blockBD.type = BodyType.DYNAMIC;
        block = model.getWorld().createBody(blockBD);

        blockShape = new PolygonShape();
        blockShape.setAsBox(b / 2f, h / 2f);

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
        g.fillRect((int) (-7 * b / 2), (int) (-7 * h / 2), (int) (7 * b), (int) (7 * h));
    }

    @Override
    public void doSpecial() {
        // Multiply the velocity by 3! Woo!
        block.setLinearVelocity(block.getLinearVelocity().mul(3f));
    }
}
