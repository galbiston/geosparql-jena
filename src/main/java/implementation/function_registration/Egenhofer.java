/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.function_registration;

import geo.topological.property_functions.egenhofer.EhOverlapPF;
import geo.topological.property_functions.egenhofer.EhMeetPF;
import geo.topological.property_functions.egenhofer.EhCoversPF;
import geo.topological.property_functions.egenhofer.EhEqualsPF;
import geo.topological.property_functions.egenhofer.EhDisjointPF;
import geo.topological.property_functions.egenhofer.EhContainsPF;
import geo.topological.property_functions.egenhofer.EhInsidePF;
import geo.topological.property_functions.egenhofer.EhCoveredByPF;
import geof.topological.filter_functions.egenhofer.EhEqualsFF;
import geof.topological.filter_functions.egenhofer.EhDisjointFF;
import geof.topological.filter_functions.egenhofer.EhCoveredByFF;
import geof.topological.filter_functions.egenhofer.EhInsideFF;
import geof.topological.filter_functions.egenhofer.EhOverlapFF;
import geof.topological.filter_functions.egenhofer.EhMeetFF;
import geof.topological.filter_functions.egenhofer.EhCoversFF;
import geof.topological.filter_functions.egenhofer.EhContainsFF;
import implementation.vocabulary.Geo;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 * 
 * 
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
        registry.put(Geo.EH_CONTAINS_NAME, EhContainsPF.class);
        registry.put(Geo.EH_COVERED_BY_NAME, EhCoveredByPF.class);
        registry.put(Geo.EH_COVERS_NAME, EhCoversPF.class);
        registry.put(Geo.EH_DISJOINT_NAME, EhDisjointPF.class);
        registry.put(Geo.EH_EQUALS_NAME, EhEqualsPF.class);
        registry.put(Geo.EH_INSIDE_NAME, EhInsidePF.class);
        registry.put(Geo.EH_MEET_NAME, EhMeetPF.class);
        registry.put(Geo.EH_OVERLAP_NAME, EhOverlapPF.class);
    }

    /**
     * This method loads all the Egenhofer Topological Expression Functions
     *
     * @param functionRegistry
     */
    public static void loadFilterFunctions(FunctionRegistry functionRegistry) {

        functionRegistry.put(Geof.EH_CONTAINS, EhContainsFF.class);
        functionRegistry.put(Geof.EH_COVERED_BY, EhCoveredByFF.class);
        functionRegistry.put(Geof.EH_COVERS, EhCoversFF.class);
        functionRegistry.put(Geof.EH_DISJOINT, EhDisjointFF.class);
        functionRegistry.put(Geof.EH_EQUALS, EhEqualsFF.class);
        functionRegistry.put(Geof.EH_INSIDE, EhInsideFF.class);
        functionRegistry.put(Geof.EH_MEET, EhMeetFF.class);
        functionRegistry.put(Geof.EH_OVERLAP, EhOverlapFF.class);

    }
}
