/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import implementation.support.GeoSerialisationEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.rdf.model.Literal;

/**
 *
 * @author Greg Albiston
 */
public class GeometryUtil {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    public static final Literal pointsToLineString(List<Literal> points) {

        List<Coordinate> coordsList = new ArrayList<>(points.size() * 4); //Set size to maximum possible. i.e. 4 coordinate per Point.

        GeometryWrapper result;
        if (!points.isEmpty()) {

            //Extract all coordinates as single list.
            for (Literal point : points) {
                GeometryWrapper geom = GeometryWrapper.extract(point);
                Coordinate[] coords = geom.getParsingGeometry().getCoordinates();
                coordsList.addAll(Arrays.asList(coords));
            }
            GeometryWrapper geom = GeometryWrapper.extract(points.get(0));
            GeometryFactory geomFactory = geom.getParsingGeometry().getFactory();

            //Convert the list to an array and build the LineString
            Coordinate[] coordinates = new Coordinate[coordsList.size()];
            coordinates = coordsList.toArray(coordinates);
            LineString lineString = geomFactory.createLineString(coordinates);
            result = new GeometryWrapper(lineString, geom.getSrsURI(), geom.getSerialisation(), new DimensionInfo(geom.getCoordinateDimension(), geom.getSpatialDimension(), lineString.getDimension()));
        } else {

            Coordinate[] coordinates = new Coordinate[coordsList.size()];
            LineString lineString = GEOMETRY_FACTORY.createLineString(coordinates);
            result = new GeometryWrapper(lineString, "", GeoSerialisationEnum.WKT, new DimensionInfo(2, 2, 1));
        }

        return result.asLiteral();
    }
}
