/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.egenhofer.PF.*;
import geof.topological.egenhofer.FF.*;
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
     * This method loads all the Egenhofer Topological Expression Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.EH_CONTAINS, ehContainsFF.class);
        functionRegistry.put(Geof.EH_COVERED_BY, ehCoveredByFF.class);
        functionRegistry.put(Geof.EH_COVERS, ehCoversFF.class);
        functionRegistry.put(Geof.EH_DISJOINT, ehDisjointFF.class);
        functionRegistry.put(Geof.EH_EQUALS, ehEqualsFF.class);
        functionRegistry.put(Geof.EH_INSIDE, ehInsideFF.class);
        functionRegistry.put(Geof.EH_MEET, ehMeetFF.class);
        functionRegistry.put(Geof.EH_OVERLAP, ehOverlapFF.class);

    }
}
