/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.YellowBozel;
import bozels.elements.Bozel;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class YellowBozelFactory extends BozelFactory {
    
    public YellowBozelFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Bozel create(float x, float y, int id) {
        return new YellowBozel(x,y,id,model);
    }
}