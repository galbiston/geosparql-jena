/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.gml;

import com.vividsolutions.jts.geom.Geometry;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.support.GeoSerialisationEnum;
import java.io.IOException;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GMLReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GMLReader.class);

    public static final GeometryWrapper read(String gmlLiteral) throws JDOMException, IOException {
        GMLGeometryBuilder gmlGeometryBuilder = GMLGeometryBuilder.extract(gmlLiteral);
        Geometry geometry = gmlGeometryBuilder.getGeometry();
        DimensionInfo dimensionInfo = gmlGeometryBuilder.getDimensionInfo();

        return new GeometryWrapper(geometry, gmlGeometryBuilder.getSrsName(), GeoSerialisationEnum.GML, dimensionInfo);
    }

}
