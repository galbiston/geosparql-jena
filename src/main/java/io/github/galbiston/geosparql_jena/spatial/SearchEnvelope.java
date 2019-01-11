/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.spatial;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.GreatCirclePointDistance;
import io.github.galbiston.geosparql_jena.implementation.UnitsOfMeasure;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import java.lang.invoke.MethodHandles;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class SearchEnvelope {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final double OUT_BOUNDS = -9999;

    public static Envelope build(GeometryWrapper geometryWrapper, double radius, String unitsURI) {

        try {
            //Get the envelope of the target GeometryWrapper and convert that to WGS84, in case it is a complex polygon.
            GeometryWrapper envelopeGeometryWrapper = geometryWrapper.envelope();
            //Convert to WGS84.
            GeometryWrapper wgsGeometryWrapper = envelopeGeometryWrapper.convertCRS(SRS_URI.WGS84_CRS);
            Envelope envelope = wgsGeometryWrapper.getEnvelope();
            double radiusMetres = UnitsOfMeasure.convertToMetres(radius, unitsURI, wgsGeometryWrapper.getLatitude());

            //Expand the envelope by the radius distance in all directions,
            //i.e. a bigger box rather than circle. More precise checks made later.
            Envelope searchEnvelope = expandEnvelope(envelope, radiusMetres);

            return searchEnvelope;
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            throw new ExprEvalException(ex.getMessage() + ": " + geometryWrapper.asLiteral(), ex);
        }
    }

    private static final double NORTH_BEARING = 0;
    private static final double SOUTH_BEARING = Math.toRadians(180);
    private static final double EAST_BEARING = Math.toRadians(90);
    private static final double WEST_BEARING = Math.toRadians(270);

    private static Envelope expandEnvelope(Envelope envelope, double distance) {
        //Travel out by the radius in the cardinal directions.

        //Envelope is in X/Y coordinate order.
        double minLon = envelope.getMinX();
        double minLat = envelope.getMinY();
        double maxLon = envelope.getMaxX();
        double maxLat = envelope.getMaxY();

        //Find the extreme values for Lat and Lon.
        double extLat = Math.abs(maxLat) > Math.abs(minLat) ? maxLat : minLat;
        double extLon = Math.abs(maxLon) > Math.abs(minLon) ? maxLon : minLon;

        //Find the greatest change: in North then use North bearing or in South then use South bearing.
        double latBearing;
        if (extLat > 0) {
            latBearing = NORTH_BEARING;
        } else {
            latBearing = SOUTH_BEARING;
        }

        //Find the greatest change: in East then use East bearing or in West then use West bearing.
        double lonBearing;
        if (extLon > 0) {
            lonBearing = EAST_BEARING;
        } else {
            lonBearing = WEST_BEARING;
        }

        //Find the new latitiude and longitude by moving the radius distance.
        //Splitting the calculation will oversize the bounding box by up to a few kilometres for a 100km distance.
        //However, only calculating what is needed. More precise checks done later.
        GreatCirclePointDistance pointDistance = new GreatCirclePointDistance(extLat, extLon, distance);
        double latRad = pointDistance.latitude(latBearing);
        double lonRad = pointDistance.longitude(latRad, lonBearing);

        Point point = GreatCirclePointDistance.radToPoint(latRad, lonRad);

        //Find the difference between the outer point and the extreme values.
        double latDiff = Math.abs(extLat - point.getX());
        double lonDiff = Math.abs(extLon - point.getY());

        //Apply the differences to expand the envelope.
        Envelope expandedEnvelope = new Envelope(minLon - lonDiff, maxLon + lonDiff, minLat - latDiff, maxLat + latDiff);
        return expandedEnvelope;
    }

    public static Envelope build(GeometryWrapper geometryWrapper) {

        try {
            //Get the envelope of the target GeometryWrapper and convert that to WGS84, in case it is a complex polygon.
            GeometryWrapper envelopeGeometryWrapper = geometryWrapper.envelope();
            //Convert to WGS84.
            GeometryWrapper wgsGeometryWrapper = envelopeGeometryWrapper.convertCRS(SRS_URI.WGS84_CRS);
            Envelope envelope = wgsGeometryWrapper.getEnvelope();
            return new Envelope(envelope);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            throw new ExprEvalException(ex.getMessage() + ": " + geometryWrapper.asLiteral(), ex);
        }
    }

    /**
     * East and West don't wrap across the 180/-180 boundary, see buildWrap().
     *
     * @param geometryWrapper
     * @param direction
     * @return Envelope covering rest of the world.
     */
    public static Envelope build(GeometryWrapper geometryWrapper, CardinalDirection direction) {

        try {
            //Get the envelope of the target GeometryWrapper and convert that to WGS84, in case it is a complex polygon.
            GeometryWrapper envelopeGeometryWrapper = geometryWrapper.envelope();
            //Convert to WGS84.
            GeometryWrapper wgsGeometryWrapper = envelopeGeometryWrapper.convertCRS(SRS_URI.WGS84_CRS);
            Envelope envelope = wgsGeometryWrapper.getEnvelope();

            double x1 = -180;
            double x2 = 180;
            double y1 = -90;
            double y2 = 90;
            switch (direction) {
                case NORTH:
                    y1 = envelope.getMinY();
                    break;
                case SOUTH:
                    y2 = envelope.getMaxY();
                    break;
                case EAST:
                    x1 = envelope.getMinX();
                    double spread = x1 + 180;
                    x2 = spread > x2 ? x2 : spread;
                    break;
                case WEST:
                    x2 = envelope.getMaxX();
                    spread = x2 - 180;
                    x1 = spread < x1 ? x1 : spread;
                    break;
            }

            return new Envelope(x1, x2, y1, y2);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            throw new ExprEvalException(ex.getMessage() + ": " + geometryWrapper.asLiteral(), ex);
        }
    }

    /**
     * East and West don't wrap across the 180/-180 boundary, see wrapBuild().
     *
     * @param geometryWrapper
     * @param direction
     * @return Envelope covering rest of the world.
     */
    public static Envelope buildWrap(GeometryWrapper geometryWrapper, CardinalDirection direction) {

        try {
            //Get the envelope of the target GeometryWrapper and convert that to WGS84, in case it is a complex polygon.
            GeometryWrapper envelopeGeometryWrapper = geometryWrapper.envelope();
            //Convert to WGS84.
            GeometryWrapper wgsGeometryWrapper = envelopeGeometryWrapper.convertCRS(SRS_URI.WGS84_CRS);
            Envelope envelope = wgsGeometryWrapper.getEnvelope();

            double x1 = -180;
            double x2 = 180;
            double y1 = -90;
            double y2 = 90;

            switch (direction) {
                case EAST:
                    double x = envelope.getMaxX();
                    if (x <= 0) {
                        x2 = x1;
                    } else {
                        x2 = x1 + x;
                    }
                    break;
                case WEST:
                    x = envelope.getMinX();
                    if (x >= 0) {
                        x1 = x2;
                    } else {
                        x1 = x2 + x;
                    }
                    break;
                default:
                    LOGGER.warn("Only EAST and WEST permitted for buildWrap. Direction: {}", direction);
            }

            if (x1 != x2) {
                return new Envelope(x1, x2, y1, y2);
            } else {
                return new Envelope(OUT_BOUNDS, OUT_BOUNDS, OUT_BOUNDS, OUT_BOUNDS);
            }

        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            throw new ExprEvalException(ex.getMessage() + ": " + geometryWrapper.asLiteral(), ex);
        }
    }

}
