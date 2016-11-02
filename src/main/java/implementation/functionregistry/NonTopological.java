/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import org.apache.jena.sparql.function.FunctionRegistry;
import implementation.support.Prefixes;
import implementation.vocabulary.General;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
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
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.BOUNDARY_NAME), geof.nontopological.filterfunction.Boundary.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.BUFFER_NAME), geof.nontopological.filterfunction.Buffer.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.CONVEXHULL_NAME), geof.nontopological.filterfunction.ConvexHull.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.DIFFERENCE_NAME), geof.nontopological.filterfunction.Difference.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.DISTANCE_NAME), geof.nontopological.filterfunction.Distance.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.ENVELOPE_NAME), geof.nontopological.filterfunction.Envelop.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.GETSRID_NAME), geof.nontopological.filterfunction.GetSRID.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.INTERSECTION_NAME), geof.nontopological.filterfunction.Intersection.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.SYMDIFFERENCE_NAME), geof.nontopological.filterfunction.SymmetricDifference.class);
        addFiltFunc(registry, General.getFunctionURI(Prefixes.GEOF_URI, General.UNION_NAME), geof.nontopological.filterfunction.Union.class);

    }

    private static void addFiltFunc(FunctionRegistry registry, String uri, Class<?> funcClass) {
        registry.put(uri, funcClass);
    }
}
