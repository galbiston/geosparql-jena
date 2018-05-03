/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.property_functions.rcc8;

import geo.topological.GenericPropertyFunction;
import geof.topological.filter_functions.rcc8.RccEqualsFF;

/**
 *
 *
 *
 */
public class RccEqualsPF extends GenericPropertyFunction {

    public RccEqualsPF() {
        super(new RccEqualsFF());
    }

}
