/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.apache.jena.sparql.function.FunctionRegistry;
import vocabulary.Prefixes;
import vocabulary.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class RelateFunctionRegistry {

    /**
     * This method loads the Dimensionally extended 9 intersection model
     * Function: relate
     *
     * The use of Relate (Geometry g1, Geometry g2, IntersectionMatrix matrix)
     * get the IntersectionMatrix of g1 and g2 (based on g1), then compare with
     * matrix returns true if they are same
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Dimensionally extended 9 intersection model Function: relate
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.RELATE_NAME), geof.topo.relate.RelateFilterFunc.class);

    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
