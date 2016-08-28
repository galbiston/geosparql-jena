/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class EHFunctionsRegistry {

    /**
     * This method loads all the Egenhofer Topological Property Functions as
     * well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_CONTAINS_NAME), geof.topopf.eh.EHContainsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVEREDBY_NAME), geof.topopf.eh.EHCoveredByPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_COVERS_NAME), geof.topopf.eh.EHCoversPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_DISJOINT_NAME), geof.topopf.eh.EHDisjointPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_EQUALS_NAME), geof.topopf.eh.EHEqualPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_INSIDE_NAME), geof.topopf.eh.EHInsidePropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_MEET_NAME), geof.topopf.eh.EHMeetPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EH_OVERLAP_NAME), geof.topopf.eh.EHOverlapPropertyFunc.class);

        // Simple Feature Query Rewrite Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.EH_CONTAINS_NAME), queryrewrite.pf.eh.EHContainsQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.EH_COVEREDBY_NAME), queryrewrite.pf.eh.EHCoveredByQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.EH_COVERS_NAME), queryrewrite.pf.eh.EHCoversQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.EH_DISJOINT_NAME), queryrewrite.pf.eh.EHDisjointQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.EH_EQUALS_NAME), queryrewrite.pf.eh.EHEqualQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.EH_INSIDE_NAME), queryrewrite.pf.eh.EHInsideQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.EH_MEET_NAME), queryrewrite.pf.eh.EHMeetQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.EH_OVERLAP_NAME), queryrewrite.pf.eh.EHOverlapQRPropertyFunc.class);
    }

    /**
     * This method loads all the Egenhofer Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_CONTAINS_NAME), geof.topo.eh.EHContainsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_COVEREDBY_NAME), geof.topo.eh.EHCoveredByFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_COVERS_NAME), geof.topo.eh.EHCoversFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_DISJOINT_NAME), geof.topo.eh.EHDisjointFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_EQUALS_NAME), geof.topo.eh.EHEqualFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_INSIDE_NAME), geof.topo.eh.EHInsideFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_MEET_NAME), geof.topo.eh.EHMeetFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EH_OVERLAP_NAME), geof.topo.eh.EHOverlapFilterFunc.class);
    }

    private static void addPropFunc(PropertyFunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
