/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.function_registry;

import geo.topological.egenhofer.property_functions.EhOverlapPF;
import geo.topological.egenhofer.property_functions.EhMeetPF;
import geo.topological.egenhofer.property_functions.EhCoversPF;
import geo.topological.egenhofer.property_functions.EhEqualsPF;
import geo.topological.egenhofer.property_functions.EhDisjointPF;
import geo.topological.egenhofer.property_functions.EhContainsPF;
import geo.topological.egenhofer.property_functions.EhInsidePF;
import geo.topological.egenhofer.property_functions.EhCoveredByPF;
import geof.topological.egenhofer.filter_functions.EhEqualsFF;
import geof.topological.egenhofer.filter_functions.EhDisjointFF;
import geof.topological.egenhofer.filter_functions.EhCoveredByFF;
import geof.topological.egenhofer.filter_functions.EhInsideFF;
import geof.topological.egenhofer.filter_functions.EhOverlapFF;
import geof.topological.egenhofer.filter_functions.EhMeetFF;
import geof.topological.egenhofer.filter_functions.EhCoversFF;
import geof.topological.egenhofer.filter_functions.EhContainsFF;
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
