/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels.factories;

import bozels.elements.Bozel;
import bozels.elements.WhiteBozel;
import bozels.models.MainModel;

/**
 *
 * @author Bart Middag
 */
public class WhiteBozelFactory extends BozelFactory {
    
    public WhiteBozelFactory(MainModel model) {
        super(model);
    }
    
    @Override
    public Bozel create(float x, float y, int id) {
        return new WhiteBozel(x,y,id,model);
    }
}