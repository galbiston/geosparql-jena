/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.simplefeatures.*;
import geof.topological.simplefeatures.expressionfunction.*;
import geof.topological.simplefeatures.filterfunction.*;
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
    @SuppressWarnings("deprecation")
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
     * This method loads all the Simple Feature Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFilterFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        registry.put(Geof.SF_CONTAINS, sfContainsFF.class);
        registry.put(Geof.SF_CROSSES, sfCrossesFF.class);
        registry.put(Geof.SF_DISJOINT, sfDisjointFF.class);
        registry.put(Geof.SF_EQUALS, sfEqualsFF.class);
        registry.put(Geof.SF_INTERSECTS, sfIntersectsFF.class);
        registry.put(Geof.SF_OVERLAPS, sfOverlapsFF.class);
        registry.put(Geof.SF_TOUCHES, sfTouchesFF.class);
        registry.put(Geof.SF_WITHIN, sfWithinFF.class);
    }

    /**
     * This method loads all the Simple Feature Topological Expression Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadExpressionFunctions(FunctionRegistry registry) {

        // Simple Feature Expression Functions
        registry.put(Geof.SF_CONTAINS, sfContainsEF.class);
        registry.put(Geof.SF_CROSSES, sfCrossesEF.class);
        registry.put(Geof.SF_DISJOINT, sfDisjointEF.class);
        registry.put(Geof.SF_EQUALS, sfEqualsEF.class);
        registry.put(Geof.SF_INTERSECTS, sfIntersectsEF.class);
        registry.put(Geof.SF_OVERLAPS, sfOverlapsEF.class);
        registry.put(Geof.SF_TOUCHES, sfTouchesEF.class);
        registry.put(Geof.SF_WITHIN, sfWithinEF.class);
    }
}
