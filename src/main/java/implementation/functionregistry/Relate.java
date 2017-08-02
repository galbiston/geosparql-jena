/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geof.topological.RelateFF;
import implementation.vocabulary.Geof;
import org.apache.jena.sparql.function.FunctionRegistry;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class Relate {

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
    public static void loadRelateFunction(FunctionRegistry registry) {

        registry.put(Geof.RELATE, RelateFF.class);
    }
}
