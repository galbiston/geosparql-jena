/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.filter_functions.egenhofer;

import geof.topological.GenericFilterFunction;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class EhDisjointFF extends GenericFilterFunction {

    //Simple Features and Egenhofer disjoint intersection patterns are the same, see GeoSPARQL standard page 11.
    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.disjoint(targetGeometry);
    }

    @Override
    protected boolean isDisjoint() {
        return true;
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
