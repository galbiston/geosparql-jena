/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.apache.jena.sparql.function.FunctionRegistry;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class NonTopoFunctionsRegistry {

    /**
     * This method loads all the Non-Topological Filter Functions, such as
     * distance, buffer, envelop, etc.
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.BOUNDARY_NAME), geof.nontopo.BoundaryFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.BUFFER_NAME), geof.nontopo.BufferFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.CONVEXHULL_NAME), geof.nontopo.ConvexHullFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.DIFFERENCE_NAME), geof.nontopo.DifferenceFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.DISTANCE_NAME), geof.nontopo.DistanceFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.ENVELOPE_NAME), geof.nontopo.EnvelopFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.GETSRID_NAME), geof.nontopo.GetSRIDFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.INTERSECTION_NAME), geof.nontopo.IntersectionFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.SYMDIFFERENCE_NAME), geof.nontopo.SymDifferenceFilterFunc.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Vocabulary.GEOF_URI, Vocabulary.UNION_NAME), geof.nontopo.UnionFilterFunc.class);

    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
