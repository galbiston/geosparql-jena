/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.simplefeatures.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.simplefeatures.filter_functions.SfCrossesFF;

/**
 *
 *
 *
 */
public class SfCrossesPF extends GenericPropertyFunction {

    public SfCrossesPF() {
        super(new SfCrossesFF());
    }

}
