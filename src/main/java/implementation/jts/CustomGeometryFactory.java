/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.jts;

import org.locationtech.jts.geom.GeometryFactory;
import java.io.Serializable;

/**
 *
 *
 */
public class CustomGeometryFactory implements Serializable {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    public static final GeometryFactory theInstance() {
        return GEOMETRY_FACTORY;
    }

}
