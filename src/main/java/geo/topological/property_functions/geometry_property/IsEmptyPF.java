/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topological.property_functions.geometry_property;

import geo.topological.GenericGeometryPropertyFunction;
import implementation.GeometryWrapper;
import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 *
 */
public class IsEmptyPF extends GenericGeometryPropertyFunction {

    @Override
    protected Literal applyPredicate(GeometryWrapper geometryWrapper) {
        Boolean isEmpty = geometryWrapper.isEmpty();
        return ResourceFactory.createTypedLiteral(isEmpty.toString(), XSDBaseNumericType.XSDboolean);
    }

}
