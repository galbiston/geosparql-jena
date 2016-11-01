/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.rcc8.filterfunction;

import geof.topological.GenericFilterFunction;
import implementation.GeometryWrapper;
import implementation.intersectionpattern.RCC8IntersectionPattern;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 * RCC8DC means Disconnected or disjoint in SF
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class Disconnected extends GenericFilterFunction {

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.relate(targetGeometry, RCC8IntersectionPattern.DISCONNECTED);
    }

}
