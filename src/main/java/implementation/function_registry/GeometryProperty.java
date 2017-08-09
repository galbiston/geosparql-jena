/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.function_registry;

import geo.topological.geometryproperty.property_functions.DimensionPF;
import geo.topological.geometryproperty.property_functions.IsSimplePF;
import geo.topological.geometryproperty.property_functions.IsEmptyPF;
import geo.topological.geometryproperty.property_functions.CoordinateDimensionPF;
import geo.topological.geometryproperty.property_functions.SpatialDimensionPF;
import implementation.vocabulary.Geo;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 * 
 */
public class GeometryProperty {

    /**
     * This method loads all the Geometry property base function
     *
     * @param registry - the PropertyFunctionRegistry to be used
     */
    public static void loadPropertyFunctions(PropertyFunctionRegistry registry) {

        registry.put(Geo.DIMENSION, DimensionPF.class);
        registry.put(Geo.COORDINATE_DIMENSION, CoordinateDimensionPF.class);
        registry.put(Geo.SPATIAL_DIMENSION, SpatialDimensionPF.class);
        registry.put(Geo.IS_SIMPLE, IsSimplePF.class);
        registry.put(Geo.IS_EMPTY, IsEmptyPF.class);
    }

}
