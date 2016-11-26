/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.functionregistry;

import geof.topological.RelateFF;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.vocabulary.Geo;
import org.apache.jena.datatypes.TypeMapper;
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
        SimpleFeatures.loadExpressionFunctions();

        Egenhofer.loadPropertyFunctions(propertyRegistry);
        Egenhofer.loadExpressionFunctions();

        RCC8.loadPropertyFunctions(propertyRegistry);
        RCC8.loadExpressionFunctions();

        Relate.loadRelateFunction(functionRegistry);

        GeometryProperty.loadPropertyFunctions(propertyRegistry);

        TypeMapper.getInstance().registerDatatype(WKTDatatype.THE_WKT_DATATYPE);
        TypeMapper.getInstance().registerDatatype(GMLDatatype.THE_GML_DATATYPE);

    }

}
