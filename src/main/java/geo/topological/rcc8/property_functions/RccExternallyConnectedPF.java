/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.rcc8.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.rcc8.filter_functions.RccExternallyConnectedFF;

/**
 *
 *
 *
 */
public class RccExternallyConnectedPF extends GenericPropertyFunction {

    public RccExternallyConnectedPF() {
        super(new RccExternallyConnectedFF());
    }

}
