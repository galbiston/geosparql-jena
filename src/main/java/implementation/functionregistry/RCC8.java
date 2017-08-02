/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.rcc8.PF.*;
import geof.topological.rcc8.FF.*;
import implementation.vocabulary.Geo;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class RCC8 {

    /**
     * This method loads all the RCC8 Topological Property Functions as well as
     * the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        registry.put(Geo.RCC_DISCONNECTED_NAME, rccDisconnectedPF.class);
        registry.put(Geo.RCC_EQUALS_NAME, rccEqualsPF.class);
        registry.put(Geo.RCC_EXTERNALLY_CONNECTED_NAME, rccExternallyConnectedPF.class);
        registry.put(Geo.RCC_NTANPROPERPARTINVERSE_NAME, rccNonTangentalProperPartInversePF.class);
        registry.put(Geo.RCC_NTANPROPERPART_NAME, rccNonTangentalProperPartPF.class);
        registry.put(Geo.RCC_PARTIALLY_OVERLAPPING_NAME, rccPartiallyOverlappingPF.class);
        registry.put(Geo.RCC_TANPROPERPARTINVERSE_NAME, rccTangentalProperPartInversePF.class);
        registry.put(Geo.RCC_TANPROPERPART_NAME, rccTangentalProperPartPF.class);
    }

    /**
     * This method loads all the RCC8 Topological Expression Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.RCC_DISCONNECTED, rccDisconnectedFF.class);
        functionRegistry.put(Geof.RCC_EQUALS, rccEqualsFF.class);
        functionRegistry.put(Geof.RCC_EXTERNALLY_CONNECTED, rccExternallyConnectedFF.class);
        functionRegistry.put(Geof.RCC_NON_TANGENTIAL_PROPER_PART_INVERSE, rccNonTangentialProperPartInverseFF.class);
        functionRegistry.put(Geof.RCC_NON_TANGENTIAL_PROPER_PART, rccNonTangentialProperPartFF.class);
        functionRegistry.put(Geof.RCC_PARTIALLY_OVERLAPPING, rccPartiallyOverlappingFF.class);
        functionRegistry.put(Geof.RCC_TANGENTIAL_PROPER_PART_INVERSE, rccTangentialProperPartInverseFF.class);
        functionRegistry.put(Geof.RCC_TANGENTIAL_PROPER_PART, rccTangentialProperPartFF.class);

    }
}
