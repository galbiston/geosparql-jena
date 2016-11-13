/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.egenhofer.*;
import geof.topological.egenhofer.expressionfunction.*;
import geof.topological.egenhofer.filterfunction.*;
import implementation.vocabulary.Geo;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class Egenhofer {

    /**
     * This method loads all the Egenhofer Topological Property Functions as
     * well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        registry.put(Geo.EH_CONTAINS_NAME, ehContainsPF.class);
        registry.put(Geo.EH_COVERED_BY_NAME, ehCoveredByPF.class);
        registry.put(Geo.EH_COVERS_NAME, ehCoversPF.class);
        registry.put(Geo.EH_DISJOINT_NAME, ehDisjointPF.class);
        registry.put(Geo.EH_EQUALS_NAME, ehEqualsPF.class);
        registry.put(Geo.EH_INSIDE_NAME, ehInsidePF.class);
        registry.put(Geo.EH_MEET_NAME, ehMeetPF.class);
        registry.put(Geo.EH_OVERLAP_NAME, ehOverlapPF.class);
    }

    /**
     * This method loads all the Egenhofer Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFilterFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        registry.put(Geof.EH_CONTAINS, ehContainsFF.class);
        registry.put(Geof.EH_COVERED_BY, ehCoveredByFF.class);
        registry.put(Geof.EH_COVERS, ehCoversFF.class);
        registry.put(Geof.EH_DISJOINT, ehDisjointFF.class);
        registry.put(Geof.EH_EQUALS, ehEqualsFF.class);
        registry.put(Geof.EH_INSIDE, ehInsideFF.class);
        registry.put(Geof.EH_MEET, ehMeetFF.class);
        registry.put(Geof.EH_OVERLAP, ehOverlapFF.class);
    }

    /**
     * This method loads all the Egenhofer Topological Expression Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadExpressionFunctions(FunctionRegistry registry) {

        // Simple Feature Expression Functions
        registry.put(Geof.EH_CONTAINS, ehContainsEF.class);
        registry.put(Geof.EH_COVERED_BY, ehCoveredByEF.class);
        registry.put(Geof.EH_COVERS, ehCoversEF.class);
        registry.put(Geof.EH_DISJOINT, ehDisjointEF.class);
        registry.put(Geof.EH_EQUALS, ehEqualsEF.class);
        registry.put(Geof.EH_INSIDE, ehInsideEF.class);
        registry.put(Geof.EH_MEET, ehMeetEF.class);
        registry.put(Geof.EH_OVERLAP, ehOverlapEF.class);
    }
}
