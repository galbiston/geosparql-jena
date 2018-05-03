/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.function_registry;

import geo.topological.property_functions.rcc8.RccTangentialProperPartInversePF;
import geo.topological.property_functions.rcc8.RccNonTangentialProperPartInversePF;
import geo.topological.property_functions.rcc8.RccTangentialProperPartPF;
import geo.topological.property_functions.rcc8.RccDisconnectedPF;
import geo.topological.property_functions.rcc8.RccPartiallyOverlappingPF;
import geo.topological.property_functions.rcc8.RccNonTangentialProperPartPF;
import geo.topological.property_functions.rcc8.RccExternallyConnectedPF;
import geo.topological.property_functions.rcc8.RccEqualsPF;
import geof.topological.filter_functions.rcc8.RccExternallyConnectedFF;
import geof.topological.filter_functions.rcc8.RccPartiallyOverlappingFF;
import geof.topological.filter_functions.rcc8.RccNonTangentialProperPartFF;
import geof.topological.filter_functions.rcc8.RccTangentialProperPartFF;
import geof.topological.filter_functions.rcc8.RccTangentialProperPartInverseFF;
import geof.topological.filter_functions.rcc8.RccNonTangentialProperPartInverseFF;
import geof.topological.filter_functions.rcc8.RccDisconnectedFF;
import geof.topological.filter_functions.rcc8.RccEqualsFF;
import implementation.vocabulary.Geo;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 * 
 * 
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
        registry.put(Geo.RCC_NTANPROPERPARTINVERSE_NAME, RccNonTangentialProperPartInversePF.class);
        registry.put(Geo.RCC_NTANPROPERPART_NAME, RccNonTangentialProperPartPF.class);
        registry.put(Geo.RCC_PARTIALLY_OVERLAPPING_NAME, RccPartiallyOverlappingPF.class);
        registry.put(Geo.RCC_TANPROPERPARTINVERSE_NAME, RccTangentialProperPartInversePF.class);
        registry.put(Geo.RCC_TANPROPERPART_NAME, RccTangentialProperPartPF.class);
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
