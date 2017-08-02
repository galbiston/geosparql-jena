/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.simplefeatures.PF.*;
import geof.topological.simplefeatures.FF.*;
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
        registry.put(Geo.SF_CONTAINS_NAME, sfContainsPF.class);
        registry.put(Geo.SF_CROSSES_NAME, sfCrossesPF.class);
        registry.put(Geo.SF_DISJOINT_NAME, sfDisjointPF.class);
        registry.put(Geo.SF_EQUALS_NAME, sfEqualsPF.class);
        registry.put(Geo.SF_INTERSECTS_NAME, sfIntersectsPF.class);
        registry.put(Geo.SF_OVERLAPS_NAME, sfOverlapsPF.class);
        registry.put(Geo.SF_TOUCHES_NAME, sfTouchesPF.class);
        registry.put(Geo.SF_WITHIN_NAME, sfWithinPF.class);
    }

    /**
     * This method loads all the Simple Feature Topological Expression Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.SF_CONTAINS, sfContainsFF.class);
        functionRegistry.put(Geof.SF_CROSSES, sfCrossesFF.class);
        functionRegistry.put(Geof.SF_DISJOINT, sfDisjointFF.class);
        functionRegistry.put(Geof.SF_EQUALS, sfEqualsFF.class);
        functionRegistry.put(Geof.SF_INTERSECTS, sfIntersectsFF.class);
        functionRegistry.put(Geof.SF_OVERLAPS, sfOverlapsFF.class);
        functionRegistry.put(Geof.SF_TOUCHES, sfTouchesFF.class);
        functionRegistry.put(Geof.SF_WITHIN, sfWithinFF.class);

    }
}
