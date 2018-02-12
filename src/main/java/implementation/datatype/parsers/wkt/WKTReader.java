/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.wkt;

import com.vividsolutions.jts.geom.Geometry;
import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.datatype.parsers.ParseException;
import implementation.support.GeoSerialisationEnum;

/**
 *
 *
 */
public class WKTReader {

    public static final GeometryWrapper read(String wktLiteral) throws ParseException {

        WKTTextSRS wktTextSRS = new WKTTextSRS(wktLiteral);

        WKTGeometryBuilder wktGeometryBuilder = WKTGeometryBuilder.extract(wktTextSRS.getWktText());

        Geometry geometry = wktGeometryBuilder.getGeometry();
        DimensionInfo dimensionInfo = wktGeometryBuilder.getDimensionInfo();

        return new GeometryWrapper(geometry, wktTextSRS.getSrsURI(), GeoSerialisationEnum.WKT, dimensionInfo);
    }

}
