/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.egenhofer.property_functions;

import geo.topological.GenericPropertyFunction;
import geof.topological.egenhofer.filter_functions.EhContainsFF;

/**
 *
 *
 *
 */
public class EhContainsPF extends GenericPropertyFunction {

    public EhContainsPF() {
        super(new EhContainsFF());
    }

}
