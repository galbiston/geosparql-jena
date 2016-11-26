/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import implementation.datatype.parsers.CustomCoordinateSequence.Dimensions;

/**
 *
 * @author Greg
 */
public class WKTWriter {

    public static final String expand(Geometry geometry) {

        StringBuilder sb = new StringBuilder();

        CustomCoordinateSequence coordSequence;

        switch (geometry.getGeometryType()) {
            case "Point":
                Point point = (Point) geometry;
                sb.append(buildWKT("POINT", point.getCoordinateSequence()));
                break;
            case "LineString":
                LineString lineString = (LineString) geometry;
                sb.append(buildWKT("LINESTRING", lineString.getCoordinateSequence()));
                break;

        }

        return sb.toString();
    }

    private static String buildWKT(String geometryType, CoordinateSequence coordSeq) {
        CustomCoordinateSequence coordSequence = (CustomCoordinateSequence) coordSeq;
        String wktText = coordSequence.toWKTText();

        return geometryType + insertDimension(coordSequence) + " " + wktText;

    }

    private static String insertDimension(CustomCoordinateSequence coordSequence) {

        Dimensions dimensions = coordSequence.getDimensions();
        switch (dimensions) {
            case XYZ:
                return " Z";
            case XYM:
                return " M";
            case XYZM:
                return " ZM";
            default:
                return "";
        }

    }

}
