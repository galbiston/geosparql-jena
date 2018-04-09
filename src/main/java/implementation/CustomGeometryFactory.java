/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.GeometryFactory;
import implementation.jts.CustomCoordinateSequenceFactory;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class CustomGeometryFactory implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomGeometryFactory.class);

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    public static final GeometryFactory theInstance() {
        return GEOMETRY_FACTORY;
    }

}
