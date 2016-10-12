/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.RedBozel;
import bozels.elements.Bozel;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class RedBozelFactory extends BozelFactory {

    public RedBozelFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Bozel create(float x, float y, int id) {
        return new RedBozel(x,y,id,model);
    }
}