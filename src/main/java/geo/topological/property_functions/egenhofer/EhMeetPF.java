/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.property_functions.egenhofer;

import geo.topological.GenericPropertyFunction;
import geof.topological.filter_functions.egenhofer.EhMeetFF;

/**
 *
 *
 *
 */
public class EhMeetPF extends GenericPropertyFunction {

    public EhMeetPF() {
        super(new EhMeetFF());
    }

}