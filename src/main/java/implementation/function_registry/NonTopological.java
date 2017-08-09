/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.function_registry;

import geof.nontopological.filter_functions.IntersectionFF;
import geof.nontopological.filter_functions.GetSRIDFF;
import geof.nontopological.filter_functions.BufferFF;
import geof.nontopological.filter_functions.EnvelopFF;
import geof.nontopological.filter_functions.BoundaryFF;
import geof.nontopological.filter_functions.UnionFF;
import geof.nontopological.filter_functions.DifferenceFF;
import geof.nontopological.filter_functions.DistanceFF;
import geof.nontopological.filter_functions.ConvexHullFF;
import geof.nontopological.filter_functions.SymmetricDifferenceFF;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;

/**
 *
 * 
 * 
 */
public class NonTopological {

    /**
     * This method loads all the Non-Topological Filter Functions, such as
     * distance, buffer, envelop, etc.
     *
     * @param registry - the FunctionRegistry to be used
     */
    public static void loadFilterFunctions(FunctionRegistry registry) {

        // Simple Feature Filter Functions
        registry.put(Geof.BOUNDARY_NAME, BoundaryFF.class);
        registry.put(Geof.BUFFER_NAME, BufferFF.class);
        registry.put(Geof.CONVEXHULL_NAME, ConvexHullFF.class);
        registry.put(Geof.DIFFERENCE_NAME, DifferenceFF.class);
        registry.put(Geof.DISTANCE_NAME, DistanceFF.class);
        registry.put(Geof.ENVELOPE_NAME, EnvelopFF.class);
        registry.put(Geof.GETSRID_NAME, GetSRIDFF.class);
        registry.put(Geof.INTERSECTION_NAME, IntersectionFF.class);
        registry.put(Geof.SYMDIFFERENCE_NAME, SymmetricDifferenceFF.class);
        registry.put(Geof.UNION_NAME, UnionFF.class);

    }
}
