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
        //Topological Function Registry
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.CONTAINS_NAME), geof.topo.ContainsFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.CROSSES_NAME), geof.topo.CrossesFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.DISJOINT_NAME), geof.topo.DisjointFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.EQUALS_NAME), geof.topo.EqualsFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.INTERSECTS_NAME), geof.topo.IntersectsFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.OVERLAPS_NAME), geof.topo.OverlapsFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.TOUCHES_NAME), geof.topo.TouchesFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.WITHIN_NAME), geof.topo.WithinFilterFunc.class);
        FunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.RELATE_NAME), geof.topo.RelateFilterFunc.class);

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
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.CONTAINS_NAME), queryrewrite.pf.ContainsQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.CROSSES_NAME), queryrewrite.pf.CrossesQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.DISJOINT_NAME), queryrewrite.pf.DisjointQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.EQUALS_NAME), queryrewrite.pf.EqualsQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.INTERSECTS_NAME), queryrewrite.pf.IntersectsQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.OVERLAPS_NAME), queryrewrite.pf.OverlapsQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.TOUCHES_NAME), queryrewrite.pf.TouchesQRPropertyFunc.class);
        PropertyFunctionRegistry.get().put(Vocabulary.getFunctionURI(Vocabulary.GEO_URI, Vocabulary.WITHIN_NAME), queryrewrite.pf.WithinQRPropertyFunc.class);

    }

    public static void main(String[] args) {
        System.out.println("Success");

        String testVariable = "Parsing";
        String testLocation = "Phase Three";

        LOGGER.info("Error: {} Location: {}", testVariable, testLocation);

        //Here some new stuff.
    }

}
