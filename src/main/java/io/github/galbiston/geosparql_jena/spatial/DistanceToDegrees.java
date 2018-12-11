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

import io.github.galbiston.geosparql_jena.implementation.UnitsOfMeasure;
import static io.github.galbiston.geosparql_jena.implementation.UnitsOfMeasure.DEGREE_UNITS;
import static io.github.galbiston.geosparql_jena.implementation.UnitsOfMeasure.METRE_UNITS;
import java.lang.invoke.MethodHandles;
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
     * @return Converted distance in the provided units.
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

}
