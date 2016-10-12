/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.Bozel;
import bozels.elements.BlueBozel;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class BlueBozelFactory extends BozelFactory {

    public BlueBozelFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Bozel create(float x, float y, int id) {
        return new BlueBozel(x,y,id,model);
    }
}