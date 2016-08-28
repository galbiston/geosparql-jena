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
public class SFFunctionRegistry {

    /**
     * This method loads all the Simple Feature Topological Property Functions
     * as well as the Query Rewrite Property Functions
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropFunctions(PropertyFunctionRegistry registry) {

        // Simple Feature Topological Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CONTAINS_NAME), geof.topopf.sf.SFContainsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CROSSES_NAME), geof.topopf.sf.SFCrossesPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_DISJOINT_NAME), geof.topopf.sf.SFDisjointPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_EQUALS_NAME), geof.topopf.sf.SFEqualsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), geof.topopf.sf.SFIntersectsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), geof.topopf.sf.SFOverlapsPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_TOUCHES_NAME), geof.topopf.sf.SFTouchesPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_WITHIN_NAME), geof.topopf.sf.SFWithinPropertyFunc.class);

        // Simple Feature Query Rewrite Property Functions
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.SFContainsQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.SFCrossesQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.SFDisjointQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.SFEqualsQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.SFIntersectsQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.SFOverlapsQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.SFTouchesQRPropertyFunc.class);
        addPropFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOR_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.SFWithinQRPropertyFunc.class);
    }

    /**
     * This method loads all the Simple Feature Topological Filter Functions
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_CONTAINS_NAME), geof.topo.sf.SFContainsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_CROSSES_NAME), geof.topo.sf.SFCrossesFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_DISJOINT_NAME), geof.topo.sf.SFDisjointFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_EQUALS_NAME), geof.topo.sf.SFEqualsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_INTERSECTS_NAME), geof.topo.sf.SFIntersectsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_OVERLAPS_NAME), geof.topo.sf.SFOverlapsFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_TOUCHES_NAME), geof.topo.sf.SFTouchesFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_WITHIN_NAME), geof.topo.sf.SFWithinFilterFunc.class);
    }

    private static void addPropFunc(PropertyFunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
