/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequenceFactory;
import implementation.support.GeoSerialisationEnum;
import implementation.support.UnitsOfMeasure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.jena.rdf.model.Literal;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GeometryUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeometryUtil.class);

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new CustomCoordinateSequenceFactory());

    /**
     * Converts points to WKT LineString.<br>
     * Assumed that all Points have the same characteristics.
     *
     * @param points
     * @return
     */
    public static final Literal pointsToLineString(List<Literal> points) {
        return pointsToLineString(points, GeoSerialisationEnum.WKT);
    }

    /**
     * Converts points to LineString according to provided Serialisation.<br>
     * Assumed that all Points have the same characteristics.
     *
     * @param points
     * @param geoSerialisationEnum
     * @return
     */
    public static final Literal pointsToLineString(List<Literal> points, GeoSerialisationEnum geoSerialisationEnum) {

        //Check if the LineString only has one coordinate and copy: LineString must have two points.
        if (points.size() == 1) {
            Literal point = points.get(0);
            points.add(point);
            LOGGER.warn("LineString with 1 point - copying to 2: {} {}", points, geoSerialisationEnum);
        }

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
            result = new GeometryWrapper(lineString, geom.getSrsURI(), geoSerialisationEnum, new DimensionInfo(geom.getCoordinateDimension(), geom.getSpatialDimension(), lineString.getDimension()));
        } else {

            CustomCoordinateSequence coordinates = new CustomCoordinateSequence(0, 2);
            LineString lineString = GEOMETRY_FACTORY.createLineString(coordinates);
            result = new GeometryWrapper(lineString, "", geoSerialisationEnum, new DimensionInfo(2, 2, 1));
        }

        return result.asLiteral();
    }

    /**
     * Split a line string into its constituent straight lines.
     *
     * @param lineString
     * @param geoSerialisationEnum
     * @return
     */
    public static final List<Literal> splitLineString(Literal lineString, GeoSerialisationEnum geoSerialisationEnum) {

        GeometryWrapper geom = GeometryWrapper.extract(lineString);

        List<Literal> parts = new ArrayList<>();

        Coordinate[] coordinates = geom.getParsingGeometry().getCoordinates();

        String srsURI = geom.getSrsURI();
        DimensionInfo dimensionInfo = geom.getDimensionInfo();

        if (coordinates.length > 1) {
            int coordEnd = coordinates.length - 1;
            for (int i = 0; i < coordEnd; i++) {
                Coordinate[] partCoordinates = Arrays.copyOfRange(coordinates, i, i + 2);
                LineString newLineString = GEOMETRY_FACTORY.createLineString(partCoordinates);
                GeometryWrapper part = new GeometryWrapper(newLineString, srsURI, geoSerialisationEnum, dimensionInfo);
                parts.add(part.asLiteral());
            }
        } else {
            //LineString is either empty or a single line, so return as the result.
            parts.add(lineString);
        }

        return parts;
    }

    /**
     * Split a line string into its constituent straight lines, using line
     * string serialisation.
     *
     * @param lineString
     * @return
     */
    public static final List<Literal> splitLineString(Literal lineString) {
        return splitLineString(lineString, GeometryWrapper.extract(lineString).getGeoSerialisation());
    }

    /**
     * Find and select the nearest candidate to the target.
     *
     * @param targetGeometry
     * @param candidateGeometries
     * @return
     */
    public static final Literal selectNearest(Literal targetGeometry, Collection<Literal> candidateGeometries) {

        GeometryWrapper target = GeometryWrapper.extract(targetGeometry);
        UnitsOfMeasure unitsOfmeasure = target.getUnitsOfMeasure();

        Double bestDistance = Double.MAX_VALUE;
        Literal bestCandidate = null;

        for (Literal candidateGeometry : candidateGeometries) {
            try {
                GeometryWrapper candidate = GeometryWrapper.extract(candidateGeometry);
                Double candidateDistance = target.distance(candidate, unitsOfmeasure);
                if (candidateDistance < bestDistance) {
                    bestCandidate = candidateGeometry;
                    bestDistance = candidateDistance;
                }
            } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                LOGGER.error("{}: Target: {}, Candidate: {}", ex.getMessage(), targetGeometry, candidateGeometry);
                return null;
            }
        }

        return bestCandidate;
    }

    public static final Integer LEFT_OF_LINE = 1;
    public static final Integer ON_LINE = 0;
    public static final Integer RIGHT_OF_LINE = -1;

    /**
     * Checks which side (left: 1, right: -1 or on-line: 0) a point on to a
     * straight line, using the cross product.
     *
     * @param point
     * @param lineString
     * @return
     */
    public static int checkSideLineString(Literal point, Literal lineString) {

        GeometryWrapper pointGeom = GeometryWrapper.extract(point);
        GeometryWrapper lineGeom = GeometryWrapper.extract(lineString);

        Coordinate P = pointGeom.getXYGeometry().getCoordinate();
        Coordinate[] lineCoord = lineGeom.getXYGeometry().getCoordinates();

        Coordinate L0 = lineCoord[0];
        Coordinate L1 = lineCoord[1];

        double result = ((L1.x - L0.x) * (P.y - L0.y)) - ((L1.y - L0.y) * (P.x - L0.x));

        if (result > 0) {
            return LEFT_OF_LINE;
        } else if (result < 0) {
            return RIGHT_OF_LINE;
        }

        return ON_LINE;
    }

    /**
     * Check whether a point is on left, right or on a line.<br>
     * The line is broken up into many smaller lines with the nearest to the
     * point checked.
     *
     * @param point
     * @param line
     * @return
     */
    public static final Integer findSideOfLine(Literal point, Literal line) {
        //Split the linestring into several linestrings.
        List<Literal> splitLine = GeometryUtil.splitLineString(line);

        //Select the linestring that is closest to the point.
        Literal nearestLine = GeometryUtil.selectNearest(point, splitLine);

        //Check which side the point is to the nearest linestring.
        return GeometryUtil.checkSideLineString(point, nearestLine);
    }

}
