/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geof.topological.egenhofer.filterfunction;

import com.vividsolutions.jts.geom.IntersectionMatrix;
import geof.topological.GenericFilterFunction;
import implementation.GeometryWrapper;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class Meet extends GenericFilterFunction {

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        IntersectionMatrix matrix = sourceGeometry.relate(targetGeometry);
        return matrix.matches("FT*******") || matrix.matches("F**T*****") || matrix.matches("F***T****");

        //TODO Reduce DE-9IM to just ensure the interiors don't overlap
        //See http://docs.geotools.org/stable/userguide/library/jts/dim9.html
        //return sourceGeometry.relate(targetGeometry, "F********") ;
    }

}
