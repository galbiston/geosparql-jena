/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological;

import implementation.GeometryWrapper;
import implementation.index.GeometryLiteralIndex.GeometryIndex;
import implementation.index.SpatialIndex;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public abstract class GenericFilterFunction extends FunctionBase2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericFilterFunction.class);

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        try {
            //Check spatial index.
            Boolean isAdded = SpatialIndex.addIfMissing(v2);
            if (isAdded == null) {
                return NodeValue.FALSE;
            }

            GeometryWrapper geometry1 = GeometryWrapper.extract(v1, GeometryIndex.PRIMARY);
            if (geometry1 == null) {
                return NodeValue.FALSE;
            }

            //Discover if an intersection.
            boolean isIntersect = SpatialIndex.query(geometry1, v2);
            //Change the intersect depending on whether Disjoint relation or not.
            if (!isIntersect && !isDisjoint()) {
                //Exit quickly if doesn't intersect and not disjoint.
                return NodeValue.FALSE;
            } else if (!isIntersect && isDisjoint()) {
                //Exit quickly if doesn't intersect and disjoint.
                return NodeValue.TRUE;
            }

            GeometryWrapper geometry2 = GeometryWrapper.extract(v2, GeometryIndex.SECONDARY);
            if (geometry2 == null) {
                return NodeValue.FALSE;
            }

            boolean result = relate(geometry1, geometry2);

            return NodeValue.makeBoolean(result);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Filter Function Exception: {}", ex.getMessage());
            return NodeValue.FALSE;
        }
    }

    public Boolean exec(Literal v1, Literal v2) {
        try {

            //Check spatial index.
            Boolean isAdded = SpatialIndex.addIfMissing(v2);
            if (isAdded == null) {
                return Boolean.FALSE;
            }

            GeometryWrapper geometry1 = GeometryWrapper.extract(v1, GeometryIndex.PRIMARY);
            if (geometry1 == null) {
                return Boolean.FALSE;
            }

            //Discover if an intersection.
            boolean isIntersect = SpatialIndex.query(geometry1, v2);
            //Change the intersect depending on whether Disjoint relation or not.
            if (!isIntersect && !isDisjoint()) {
                //Exit quickly if doesn't intersect and not disjoint.
                return Boolean.FALSE;
            } else if (!isIntersect && isDisjoint()) {
                //Exit quickly if doesn't intersect and disjoint.
                return Boolean.TRUE;
            }

            GeometryWrapper geometry2 = GeometryWrapper.extract(v2, GeometryIndex.SECONDARY);
            if (geometry2 == null) {
                return Boolean.FALSE;
            }

            boolean result = relate(geometry1, geometry2);
            return result;
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Filter Function Exception: {}", ex.getMessage());
            return Boolean.FALSE;
        }
    }

    protected abstract boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException;

    protected abstract boolean isDisjoint();
}
