/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static Model MODEL;
    private static InfModel INF_MODEL;

    public static void getGeoFunctionRegistry() {
        //Property Function Registry

        //Topological Function Registry
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_CONTAINS_NAME), geof.topo.sf.SFContainsFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_CROSSES_NAME), geof.topo.sf.SFCrossesFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_DISJOINT_NAME), geof.topo.sf.SFDisjointFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_EQUALS_NAME), geof.topo.sf.SFEqualsFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_INTERSECTS_NAME), geof.topo.sf.SFIntersectsFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_OVERLAPS_NAME), geof.topo.sf.SFOverlapsFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_TOUCHES_NAME), geof.topo.sf.SFTouchesFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SF_WITHIN_NAME), geof.topo.sf.SFWithinFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.RELATE_NAME), geof.topo.sf.SFRelateFilterFunc.class);

        //Non-Topological Function Registry
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.DISTANCE_NAME), geof.nontopo.DistanceFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.BUFFER_NAME), geof.nontopo.BufferFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.CONVEXHULL_NAME), geof.nontopo.ConvexHullFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.INTERSECTION_NAME), geof.nontopo.IntersectionFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.UNION_NAME), geof.nontopo.UnionFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.DIFFERENCE_NAME), geof.nontopo.DifferenceFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SYMDIFFERENCE_NAME), geof.nontopo.SymDifferenceFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.ENVELOPE_NAME), geof.nontopo.EnvelopFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.BOUNDARY_NAME), geof.nontopo.BoundaryFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.GETSRID_NAME), geof.nontopo.GetSRIDFilterFunc.class);
    }

    public static void getQRFunctionRegistry() {
        //Query Rewrite Property Function Registry
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CONTAINS_NAME), queryrewrite.pf.sf.SFContainsQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_CROSSES_NAME), queryrewrite.pf.sf.SFCrossesQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_DISJOINT_NAME), queryrewrite.pf.sf.SFDisjointQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_EQUALS_NAME), queryrewrite.pf.sf.SFEqualsQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_INTERSECTS_NAME), queryrewrite.pf.sf.SFIntersectsQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_OVERLAPS_NAME), queryrewrite.pf.sf.SFOverlapsQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_TOUCHES_NAME), queryrewrite.pf.sf.SFTouchesQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.SF_WITHIN_NAME), queryrewrite.pf.sf.SFWithinQRPropertyFunc.class);

    }

    public static void main(String[] args) {
        System.out.println("Success");

        String testVariable = "Parsing";
        String testLocation = "Phase Three";

        LOGGER.info("Error: {} Location: {}", testVariable, testLocation);

        //Here some new stuff.
    }

}
