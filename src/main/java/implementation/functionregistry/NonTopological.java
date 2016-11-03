/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;

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
        registry.put(Geof.BOUNDARY_NAME, geof.nontopological.filterfunction.Boundary.class);
        registry.put(Geof.BUFFER_NAME, geof.nontopological.filterfunction.Buffer.class);
        registry.put(Geof.CONVEXHULL_NAME, geof.nontopological.filterfunction.ConvexHull.class);
        registry.put(Geof.DIFFERENCE_NAME, geof.nontopological.filterfunction.Difference.class);
        registry.put(Geof.DISTANCE_NAME, geof.nontopological.filterfunction.Distance.class);
        registry.put(Geof.ENVELOPE_NAME, geof.nontopological.filterfunction.Envelop.class);
        registry.put(Geof.GETSRID_NAME, geof.nontopological.filterfunction.GetSRID.class);
        registry.put(Geof.INTERSECTION_NAME, geof.nontopological.filterfunction.Intersection.class);
        registry.put(Geof.SYMDIFFERENCE_NAME, geof.nontopological.filterfunction.SymmetricDifference.class);
        registry.put(Geof.UNION_NAME, geof.nontopological.filterfunction.Union.class);

    }
}
