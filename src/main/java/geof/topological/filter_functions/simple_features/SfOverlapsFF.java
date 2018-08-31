/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.filter_functions.simple_features;

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
public class SfOverlapsFF extends GenericFilterFunction {

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.overlaps(targetGeometry);
    }

    @Override
    protected boolean isDisjoint() {
        return false;
    }

    @Override
    protected boolean permittedTopology(DimensionInfo sourceDimensionInfo, DimensionInfo targetDimensionInfo) {
        return (sourceDimensionInfo.isArea() && targetDimensionInfo.isArea()) || (sourceDimensionInfo.isPoint() && targetDimensionInfo.isPoint()) || (sourceDimensionInfo.isLine() && targetDimensionInfo.isLine());
    }

    @Override
    protected boolean isDisconnected() {
        return false;
    }
}
