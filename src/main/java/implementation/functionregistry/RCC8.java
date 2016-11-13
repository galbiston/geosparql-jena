/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.rcc8.*;
import geof.topological.rcc8.expressionfunction.*;
import geof.topological.rcc8.filterfunction.*;
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
    @SuppressWarnings("deprecation")
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
     * This method loads all the RCC8 Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFilterFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        registry.put(Geof.RCC_DISCONNECTED, rccDisconnectedFF.class);
        registry.put(Geof.RCC_EQUALS, rccEqualsFF.class);
        registry.put(Geof.RCC_EXTERNALLY_CONNECTED, rccExternallyConnectedFF.class);
        registry.put(Geof.RCC_NON_TANGENTIAL_PROPER_PART_INVERSE, rccNonTangentialProperPartInverseFF.class);
        registry.put(Geof.RCC_NON_TANGENTIAL_PROPER_PART, rccNonTangentialProperPartFF.class);
        registry.put(Geof.RCC_PARTIALLY_OVERLAPPING, rccPartiallyOverlappingFF.class);
        registry.put(Geof.RCC_TANGENTIAL_PROPER_PART_INVERSE, rccTangentialProperPartInverseFF.class);
        registry.put(Geof.RCC_TANGENTIAL_PROPER_PART, rccTangentialProperPartFF.class);
    }

    /**
     * This method loads all the RCC8 Topological Expression Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadExpressionFunctions(FunctionRegistry registry) {

        // Simple Feature Expression Functions
        registry.put(Geof.RCC_DISCONNECTED, rccDisconnectedEF.class);
        registry.put(Geof.RCC_EQUALS, rccEqualsEF.class);
        registry.put(Geof.RCC_EXTERNALLY_CONNECTED, rccExternallyConnectedEF.class);
        registry.put(Geof.RCC_NON_TANGENTIAL_PROPER_PART_INVERSE, rccNonTangentialProperPartInverseEF.class);
        registry.put(Geof.RCC_NON_TANGENTIAL_PROPER_PART, rccNonTangentialProperPartEF.class);
        registry.put(Geof.RCC_PARTIALLY_OVERLAPPING, rccPartiallyOverlappingEF.class);
        registry.put(Geof.RCC_TANGENTIAL_PROPER_PART_INVERSE, rccTangentialProperPartInverseEF.class);
        registry.put(Geof.RCC_TANGENTIAL_PROPER_PART, rccTangentialProperPartEF.class);
    }
}
