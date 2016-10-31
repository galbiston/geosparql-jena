/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import org.apache.jena.sparql.function.FunctionRegistry;
import implementation.support.Prefixes;
import implementation.support.Vocabulary;

/**
 *
 * @author haozhechen
 */
public class NonTopological {

    /**
     * This method loads all the Non-Topological Filter Functions, such as
     * distance, buffer, envelop, etc.
     *
     * @param registry - the FunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadFiltFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.BOUNDARY_NAME), geof.nontopological.filterfunction.Boundary.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.BUFFER_NAME), geof.nontopological.filterfunction.Buffer.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.CONVEXHULL_NAME), geof.nontopological.filterfunction.ConvexHull.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.DIFFERENCE_NAME), geof.nontopological.filterfunction.Difference.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.DISTANCE_NAME), geof.nontopological.filterfunction.Distance.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.ENVELOPE_NAME), geof.nontopological.filterfunction.Envelop.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.GETSRID_NAME), geof.nontopological.filterfunction.GetSRID.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.INTERSECTION_NAME), geof.nontopological.filterfunction.Intersection.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.SYMDIFFERENCE_NAME), geof.nontopological.filterfunction.SymmetricDifference.class);
        addFiltFunc(registry, Vocabulary.getFunctionURI(Prefixes.GEOF_URI, Vocabulary.UNION_NAME), geof.nontopological.filterfunction.Union.class);

    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
