/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.function_registry;

import geo.topological.simplefeatures.property_functions.SfTouchesPF;
import geo.topological.simplefeatures.property_functions.SfWithinPF;
import geo.topological.simplefeatures.property_functions.SfDisjointPF;
import geo.topological.simplefeatures.property_functions.SfOverlapsPF;
import geo.topological.simplefeatures.property_functions.SfContainsPF;
import geo.topological.simplefeatures.property_functions.SfIntersectsPF;
import geo.topological.simplefeatures.property_functions.SfCrossesPF;
import geo.topological.simplefeatures.property_functions.SfEqualsPF;
import geof.topological.simplefeatures.filter_functions.SfWithinFF;
import geof.topological.simplefeatures.filter_functions.SfOverlapsFF;
import geof.topological.simplefeatures.filter_functions.SfDisjointFF;
import geof.topological.simplefeatures.filter_functions.SfCrossesFF;
import geof.topological.simplefeatures.filter_functions.SfEqualsFF;
import geof.topological.simplefeatures.filter_functions.SfTouchesFF;
import geof.topological.simplefeatures.filter_functions.SfIntersectsFF;
import geof.topological.simplefeatures.filter_functions.SfContainsFF;
import implementation.vocabulary.Geo;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class SimpleFeatures {

    /**
     * This method loads all the Simple Feature Topological Property Functions
     * as well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        registry.put(Geo.SF_CONTAINS_NAME, SfContainsPF.class);
        registry.put(Geo.SF_CROSSES_NAME, SfCrossesPF.class);
        registry.put(Geo.SF_DISJOINT_NAME, SfDisjointPF.class);
        registry.put(Geo.SF_EQUALS_NAME, SfEqualsPF.class);
        registry.put(Geo.SF_INTERSECTS_NAME, SfIntersectsPF.class);
        registry.put(Geo.SF_OVERLAPS_NAME, SfOverlapsPF.class);
        registry.put(Geo.SF_TOUCHES_NAME, SfTouchesPF.class);
        registry.put(Geo.SF_WITHIN_NAME, SfWithinPF.class);
    }

    /**
     * This method loads all the Simple Feature Topological Expression Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.SF_CONTAINS, SfContainsFF.class);
        functionRegistry.put(Geof.SF_CROSSES, SfCrossesFF.class);
        functionRegistry.put(Geof.SF_DISJOINT, SfDisjointFF.class);
        functionRegistry.put(Geof.SF_EQUALS, SfEqualsFF.class);
        functionRegistry.put(Geof.SF_INTERSECTS, SfIntersectsFF.class);
        functionRegistry.put(Geof.SF_OVERLAPS, SfOverlapsFF.class);
        functionRegistry.put(Geof.SF_TOUCHES, SfTouchesFF.class);
        functionRegistry.put(Geof.SF_WITHIN, SfWithinFF.class);

    }
}
