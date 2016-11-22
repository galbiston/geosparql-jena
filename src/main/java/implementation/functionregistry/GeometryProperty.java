/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geo.topological.geometrypeoperty.*;
import implementation.vocabulary.Geo;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 * @author haozhechen
 */
public class GeometryProperty {

    /**
     * This method loads all the Geometry property base function
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    @SuppressWarnings("deprecation")
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        registry.put(Geo.DIMENSION, Dimension.class);
        registry.put(Geo.IS_SIMPLE, IsSimple.class);
        registry.put(Geo.IS_EMPTY, IsEmpty.class);
    }

}
