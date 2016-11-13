/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geof.topological.RelateFF;
import implementation.vocabulary.Geo;
import org.apache.jena.query.ARQ;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

/**
 *
 * @author Greg
 * @author haozhechen
 */
public class RegistryLoader {

    /**
     * Initialize all the GeoSPARQL property and filter functions.
     * <br>Use this for standard GeoSPARQL setup
     */
    public static void load() {

        final PropertyFunctionRegistry propertyRegistry = PropertyFunctionRegistry.chooseRegistry(ARQ.getContext());
        final FunctionRegistry functionRegistry = FunctionRegistry.get(ARQ.getContext());

        NonTopological.loadFilterFunctions(functionRegistry);
        functionRegistry.put(Geo.RELATE_NAME, RelateFF.class);

        SimpleFeatures.loadPropertyFunctions(propertyRegistry);
        SimpleFeatures.loadFilterFunctions(functionRegistry);
//        SimpleFeatures.loadExpressionFunctions(functionRegistry);

        Egenhofer.loadPropertyFunctions(propertyRegistry);
        Egenhofer.loadFilterFunctions(functionRegistry);
//        Egenhofer.loadExpressionFunctions(functionRegistry);

        RCC8.loadPropertyFunctions(propertyRegistry);
        RCC8.loadFilterFunctions(functionRegistry);
//        RCC8.loadExpressionFunctions(functionRegistry);

        Relate.loadRelateFunction(functionRegistry);

    }

}
