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
import java.util.HashSet;
import java.util.Objects;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class SearchEnvelope {

    private final Envelope mainEnvelope;
    private final Envelope wrapEnvelope;

    protected SearchEnvelope(Envelope envelope) {

        //Check whether Envelope exceeds bounds so requires wrapping.
        double minX = envelope.getMinX();
        double maxX = envelope.getMaxX();
        if (minX < -180) {
            this.mainEnvelope = new Envelope(-180, maxX, envelope.getMinY(), envelope.getMaxY());
            this.wrapEnvelope = new Envelope(minX + 360, 180, envelope.getMinY(), envelope.getMaxY());
        } else if (maxX > 180) {
            this.mainEnvelope = new Envelope(minX, 180, envelope.getMinY(), envelope.getMaxY());
            this.wrapEnvelope = new Envelope(-180, maxX - 360, envelope.getMinY(), envelope.getMaxY());
        } else {
            this.mainEnvelope = envelope;
            this.wrapEnvelope = null;
        }
    }

    public Envelope getMainEnvelope() {
        return mainEnvelope;
    }

    public Envelope getWrapEnvelope() {
        return wrapEnvelope;
    }

    public HashSet<Resource> check(SpatialIndex spatialIndex) {
        HashSet<Resource> features = spatialIndex.query(mainEnvelope);

        if (wrapEnvelope != null) {
            HashSet<Resource> wrapFeatures = spatialIndex.query(wrapEnvelope);
            features.addAll(wrapFeatures);
        }
        return features;
    }

    public boolean check(Envelope envelope) {
        boolean result = mainEnvelope.intersects(envelope);

        if (!result && wrapEnvelope != null) {
            result = wrapEnvelope.intersects(envelope);
        }
        return result;
    }

    @Override
    public String toString() {
        return "SearchEnvelope{" + "envelope=" + mainEnvelope + ", wrappedEnvelope=" + wrapEnvelope + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.mainEnvelope);
        hash = 47 * hash + Objects.hashCode(this.wrapEnvelope);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SearchEnvelope other = (SearchEnvelope) obj;
        if (!Objects.equals(this.mainEnvelope, other.mainEnvelope)) {
            return false;
        }
        return Objects.equals(this.wrapEnvelope, other.wrapEnvelope);
    }

    public static SearchEnvelope build(GeometryWrapper geometryWrapper, double radius, String unitsURI) {

        try {
            //Get the envelope of the target GeometryWrapper and convert that to WGS84, in case it is a complex polygon.
            GeometryWrapper envelopeGeometryWrapper = geometryWrapper.envelope();
            //Convert to WGS84.
            GeometryWrapper wgsGeometryWrapper = envelopeGeometryWrapper.convertCRS(SRS_URI.WGS84_CRS);
            Envelope envelope = wgsGeometryWrapper.getEnvelope();
            double radiusMetres = UnitsOfMeasure.convertToMetres(radius, unitsURI, wgsGeometryWrapper.getLatitude());

            //Expand the envelope by the radius distance in all directions,
            //i.e. a bigger box rather than circle. More precise checks made later.
            SearchEnvelope searchEnvelope = expandEnvelope(envelope, radiusMetres);

            return searchEnvelope;
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            throw new ExprEvalException(ex.getMessage() + ": " + geometryWrapper.asLiteral(), ex);
        }
    }

    private static final double NORTH_BEARING = 0;
    private static final double EAST_BEARING = Math.toRadians(90);
    private static final double SOUTH_BEARING = Math.toRadians(180);
    private static final double WEST_BEARING = Math.toRadians(270);

    private static SearchEnvelope expandEnvelope(Envelope envelope, double distance) {
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

        Point point = GreatCirclePointDistance.radToPoint(latRad, lonRad, false);

        //Find the difference between the outer point and the extreme values.
        double latDiff = Math.abs(extLat - point.getX());
        double lonDiff = Math.abs(extLon - point.getY());

        //Find differences of the longitude and wrap the values if required.
        double normMinLon = GreatCirclePointDistance.normaliseLongitude(minLon - lonDiff);
        double normMaxLon = GreatCirclePointDistance.normaliseLongitude(maxLon + lonDiff);

        //Apply the differences to expand the envelope.
        Envelope expandedEnvelope = new Envelope(normMinLon, normMaxLon, minLat - latDiff, maxLat + latDiff);

        SearchEnvelope searchEnvelope = new SearchEnvelope(expandedEnvelope);
        return searchEnvelope;
    }

    public static SearchEnvelope build(GeometryWrapper geometryWrapper) {

        try {
            //Get the envelope of the target GeometryWrapper and convert that to WGS84, in case it is a complex polygon.
            GeometryWrapper envelopeGeometryWrapper = geometryWrapper.envelope();
            //Convert to WGS84.
            GeometryWrapper wgsGeometryWrapper = envelopeGeometryWrapper.convertCRS(SRS_URI.WGS84_CRS);
            Envelope envelope = wgsGeometryWrapper.getEnvelope();
            SearchEnvelope searchEnvelope = new SearchEnvelope(envelope);
            return searchEnvelope;
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
    public static SearchEnvelope build(GeometryWrapper geometryWrapper, CardinalDirection direction) {

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

            //Exclusive search so anything within or on same axis as a Polygon is excluded.
            switch (direction) {
                case NORTH:
                    y1 = envelope.getMaxY();
                    break;
                case SOUTH:
                    y2 = envelope.getMinY();
                    break;
                case EAST:
                    x1 = envelope.getMaxX();
                    x2 = x1 + 180;
                    break;
                case WEST:
                    x2 = envelope.getMinX();
                    x1 = x2 - 180;
                    break;
            }

            Envelope cardinalEnvelope = new Envelope(x1, x2, y1, y2);

            return new SearchEnvelope(cardinalEnvelope);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            throw new ExprEvalException(ex.getMessage() + ": " + geometryWrapper.asLiteral(), ex);
        }
    }

}
