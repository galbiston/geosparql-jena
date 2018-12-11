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
import io.github.galbiston.geosparql_jena.implementation.UnitsOfMeasure;
import static io.github.galbiston.geosparql_jena.implementation.UnitsOfMeasure.DEGREE_UNITS;
import static io.github.galbiston.geosparql_jena.implementation.UnitsOfMeasure.METRE_UNITS;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import java.lang.invoke.MethodHandles;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistanceToDegrees {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //https://en.wikipedia.org/wiki/Decimal_degrees
    private static final double EQUATORIAL_METRES = 111319.9;

    /**
     * Provides conversion of units to degrees.
     * <br> Conversion from linear (i.e. metres) to degrees based on equatorial
     * radius of 111.32km.
     * <br> Therefore, this should only be used for rough bounding area before
     * using more precise distance methods of GeometryWrapper.
     *
     * @param distance
     * @param unitsURI
     * @param latitude
     * @return
     */
    public static final double convert(double distance, String unitsURI, double latitude) {

        UnitsOfMeasure units = new UnitsOfMeasure(unitsURI);

        if (units.isLinearUnits()) {
            double latitudeRadians = Math.toRadians(latitude);
            double longitudeRatio = Math.cos(latitudeRadians) * EQUATORIAL_METRES;
            double metreDistance = UnitsOfMeasure.conversion(distance, units, METRE_UNITS);
            return metreDistance / longitudeRatio;
        } else {
            return UnitsOfMeasure.conversion(distance, units, DEGREE_UNITS);
        }

    }

    public static Envelope buildSearchEnvelope(GeometryWrapper geometryWrapper, double radius, String units) {

        try {

            //Get the envelope of the target GeometryWrapper and convert that to WGS84, in case it is a complex polygon.
            GeometryWrapper envelopeGeometryWrapper = geometryWrapper.envelope();
            //Convert to WGS84.
            GeometryWrapper wgsGeometryWrapper = envelopeGeometryWrapper.convertCRS(SRS_URI.WGS84_CRS);
            Envelope envelope = wgsGeometryWrapper.getEnvelope();

            //Expand the envelope by the radius distance in all directions,
            //i.e. a bigger box rather than circle. More precise checks made later.
            Envelope searchEnvelope = new Envelope(envelope);
            double latitude = findLatitude(wgsGeometryWrapper);
            double degreeRadius = DistanceToDegrees.convert(radius, units, latitude);
            searchEnvelope.expandBy(degreeRadius);

            return searchEnvelope;
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Exception: {}, {}, {}, {}", geometryWrapper.asLiteral(), ex.getMessage());
            throw new ExprEvalException(ex.getMessage() + ": " + geometryWrapper.asLiteral());
        }
    }

    private static double findLatitude(GeometryWrapper wgsGeometryWrapper) {
        //Latitude is Y in WGS84.
        Geometry geometry = wgsGeometryWrapper.getXYGeometry();
        Point point = geometry.getCentroid();
        return point.getY();
    }

}
