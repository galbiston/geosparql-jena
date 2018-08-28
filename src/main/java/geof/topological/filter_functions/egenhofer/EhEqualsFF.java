/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.filter_functions.egenhofer;

import geof.topological.GenericFilterFunction;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.datatype.GeometryDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.sparql.expr.NodeValue;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 *
 */
public class EhEqualsFF extends GenericFilterFunction {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        if (v1.isLiteral() && v2.isLiteral()) {
            if (v1.asString().equals(v2.asString())) {
                String datatypeURI = v1.getDatatypeURI();
                boolean isGeometryDatatype = GeometryDatatype.checkURI(datatypeURI);
                return NodeValue.makeBoolean(isGeometryDatatype);
            } else {
                return super.exec(v1, v2);
            }
        }
        return NodeValue.FALSE;
    }

    @Override
    public Boolean exec(Literal v1, Literal v2) {
        if (v1.getLexicalForm().equals(v2.getLexicalForm())) {
            String datatypeURI = v1.getDatatypeURI();
            return GeometryDatatype.checkURI(datatypeURI);
        } else {
            return super.exec(v1, v2);
        }
    }

    //Simple Features and Egenhofer equals intersection patterns are the same, see GeoSPARQL standard page 11.
    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.equals(targetGeometry);
    }

    @Override
    protected boolean isDisjoint() {
        return false;
    }

    @Override
    protected boolean permittedTopology(DimensionInfo sourceDimensionInfo, DimensionInfo targetDimensionInfo) {
        return true;
    }

    @Override
    protected boolean isDisconnected() {
        return false;
    }
}
