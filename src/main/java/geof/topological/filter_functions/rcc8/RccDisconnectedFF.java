/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.filter_functions.rcc8;

import geof.topological.GenericFilterFunction;
import implementation.GeometryWrapper;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 *
 */
public class RccDisconnectedFF extends GenericFilterFunction {

    //Simple Features disconnect and RCC8 disconnected intersection patterns are the same, see GeoSPARQL standard page 11.
    //Pattern "FFTFFTTTT" stated in GeoSPARQL 11-052r4 p. 9 but means that Points are not disconnected from Polygons.
    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.disjoint(targetGeometry);
    }

    @Override
    protected boolean isDisjoint() {
        return true;
    }
}
