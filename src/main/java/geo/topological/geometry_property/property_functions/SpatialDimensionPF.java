/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.geometry_property.property_functions;

import geo.topological.GenericGeometryPropertyFunction;
import implementation.GeometryWrapper;
import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 *
 */
public class SpatialDimensionPF extends GenericGeometryPropertyFunction {

    @Override
    protected Literal applyPredicate(GeometryWrapper geometryWrapper) {
        Integer dimension = geometryWrapper.getSpatialDimension();
        return ResourceFactory.createTypedLiteral(dimension.toString(), XSDBaseNumericType.XSDinteger);
    }

}
