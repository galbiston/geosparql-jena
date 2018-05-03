/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.property_functions.simple_features;

import geo.topological.GenericPropertyFunction;
import geof.topological.filter_functions.simple_features.SfDisjointFF;

/**
 *
 *
 *
 */
public class SfDisjointPF extends GenericPropertyFunction {

    public SfDisjointPF() {
        super(new SfDisjointFF());
    }

}
