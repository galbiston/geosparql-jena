/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.function_registry;

import geo.topological.rcc8.property_functions.RccTangentalProperPartInversePF;
import geo.topological.rcc8.property_functions.RccNonTangentalProperPartInversePF;
import geo.topological.rcc8.property_functions.RccTangentalProperPartPF;
import geo.topological.rcc8.property_functions.RccDisconnectedPF;
import geo.topological.rcc8.property_functions.RccPartiallyOverlappingPF;
import geo.topological.rcc8.property_functions.RccNonTangentalProperPartPF;
import geo.topological.rcc8.property_functions.RccExternallyConnectedPF;
import geo.topological.rcc8.property_functions.RccEqualsPF;
import geof.topological.rcc8.filter_functions.RccExternallyConnectedFF;
import geof.topological.rcc8.filter_functions.RccPartiallyOverlappingFF;
import geof.topological.rcc8.filter_functions.RccNonTangentialProperPartFF;
import geof.topological.rcc8.filter_functions.RccTangentialProperPartFF;
import geof.topological.rcc8.filter_functions.RccTangentialProperPartInverseFF;
import geof.topological.rcc8.filter_functions.RccNonTangentialProperPartInverseFF;
import geof.topological.rcc8.filter_functions.RccDisconnectedFF;
import geof.topological.rcc8.filter_functions.RccEqualsFF;
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
        registry.put(Geo.RCC_DISCONNECTED_NAME, RccDisconnectedPF.class);
        registry.put(Geo.RCC_EQUALS_NAME, RccEqualsPF.class);
        registry.put(Geo.RCC_EXTERNALLY_CONNECTED_NAME, RccExternallyConnectedPF.class);
        registry.put(Geo.RCC_NTANPROPERPARTINVERSE_NAME, RccNonTangentalProperPartInversePF.class);
        registry.put(Geo.RCC_NTANPROPERPART_NAME, RccNonTangentalProperPartPF.class);
        registry.put(Geo.RCC_PARTIALLY_OVERLAPPING_NAME, RccPartiallyOverlappingPF.class);
        registry.put(Geo.RCC_TANPROPERPARTINVERSE_NAME, RccTangentalProperPartInversePF.class);
        registry.put(Geo.RCC_TANPROPERPART_NAME, RccTangentalProperPartPF.class);
    }

    /**
     * This method loads all the RCC8 Topological Expression Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.RCC_DISCONNECTED, RccDisconnectedFF.class);
        functionRegistry.put(Geof.RCC_EQUALS, RccEqualsFF.class);
        functionRegistry.put(Geof.RCC_EXTERNALLY_CONNECTED, RccExternallyConnectedFF.class);
        functionRegistry.put(Geof.RCC_NON_TANGENTIAL_PROPER_PART_INVERSE, RccNonTangentialProperPartInverseFF.class);
        functionRegistry.put(Geof.RCC_NON_TANGENTIAL_PROPER_PART, RccNonTangentialProperPartFF.class);
        functionRegistry.put(Geof.RCC_PARTIALLY_OVERLAPPING, RccPartiallyOverlappingFF.class);
        functionRegistry.put(Geof.RCC_TANGENTIAL_PROPER_PART_INVERSE, RccTangentialProperPartInverseFF.class);
        functionRegistry.put(Geof.RCC_TANGENTIAL_PROPER_PART, RccTangentialProperPartFF.class);

    }
}
