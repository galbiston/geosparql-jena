/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geof.nontopological.*;
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
    public static void loadFilterFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        registry.put(Geof.BOUNDARY_NAME, Boundary.class);
        registry.put(Geof.BUFFER_NAME, Buffer.class);
        registry.put(Geof.CONVEXHULL_NAME, ConvexHull.class);
        registry.put(Geof.DIFFERENCE_NAME, Difference.class);
        registry.put(Geof.DISTANCE_NAME, Distance.class);
        registry.put(Geof.ENVELOPE_NAME, Envelop.class);
        registry.put(Geof.GETSRID_NAME, GetSRID.class);
        registry.put(Geof.INTERSECTION_NAME, Intersection.class);
        registry.put(Geof.SYMDIFFERENCE_NAME, SymmetricDifference.class);
        registry.put(Geof.UNION_NAME, Union.class);

    }
}
