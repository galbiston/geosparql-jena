/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package implementation.registry;

import implementation.UnitsOfMeasure;
import implementation.vocabulary.SRS_URI;
import static implementation.vocabulary.SRS_URI.EPSG_BASE_CRS_URI;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sis.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class CRSRegistry implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Map<String, CoordinateReferenceSystem> CRS_REGISTRY = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Boolean> AXIS_XY_REGISTRY = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, UnitsOfMeasure> UNITS_OF_MEASURE_REGISTRY = Collections.synchronizedMap(new HashMap<>());

    public static final String DEFAULT_WKT_CRS84_CODE = "CRS:84";
    private static final List<AxisDirection> OTHER_Y_AXIS_DIRECTIONS = Arrays.asList(AxisDirection.NORTH_EAST, AxisDirection.NORTH_WEST, AxisDirection.SOUTH_EAST, AxisDirection.SOUTH_WEST, AxisDirection.NORTH_NORTH_EAST, AxisDirection.NORTH_NORTH_WEST, AxisDirection.SOUTH_SOUTH_EAST, AxisDirection.SOUTH_SOUTH_WEST);

    public static final UnitsOfMeasure getUnitsOfMeasure(String srsURI) {
        storeCRS(srsURI);
        return UNITS_OF_MEASURE_REGISTRY.get(srsURI);
    }

    public static final Boolean getAxisXY(String srsURI) {
        storeCRS(srsURI);
        return AXIS_XY_REGISTRY.get(srsURI);
    }

    public static final CoordinateReferenceSystem getCRS(String srsURI) {
        return storeCRS(srsURI);
    }

    private static CoordinateReferenceSystem storeCRS(String srsURI) {

        CoordinateReferenceSystem crs;
        if (CRS_REGISTRY.containsKey(srsURI)) {
            crs = CRS_REGISTRY.get(srsURI);
        } else {

            //Find the CRS based on the SRS.
            try {
                crs = CRS.forCode(srsURI);
            } catch (FactoryException ex) {
                LOGGER.error("SRS URI Unrecognised: {} - {}", srsURI, ex.getMessage());
                return null;
            }

            CRS_REGISTRY.put(srsURI, crs);

            Boolean isAxisXY = checkAxisXY(crs);
            AXIS_XY_REGISTRY.put(srsURI, isAxisXY);

            UnitsOfMeasure unitsOfMeasure = new UnitsOfMeasure(crs);
            UNITS_OF_MEASURE_REGISTRY.put(srsURI, unitsOfMeasure);
            UnitsRegistry.addUnit(unitsOfMeasure);
        }

        return crs;
    }

    protected static final Boolean checkAxisXY(CoordinateReferenceSystem crs) {

        AxisDirection axisDirection = crs.getCoordinateSystem().getAxis(0).getDirection();

        if (axisDirection.equals(AxisDirection.NORTH) || axisDirection.equals(AxisDirection.SOUTH)) {
            return false;
        } else if (axisDirection.equals(AxisDirection.EAST) || axisDirection.equals(AxisDirection.WEST)) {
            return true;
        } else {
            return !OTHER_Y_AXIS_DIRECTIONS.contains(axisDirection);
        }
    }

    public static final void setupDefaultCRS() {

        try {
            CoordinateReferenceSystem crs = CRS.forCode(DEFAULT_WKT_CRS84_CODE);
            UnitsOfMeasure unitsOfMeasure = new UnitsOfMeasure(crs);
            UnitsRegistry.addUnit(unitsOfMeasure);

            //CRS_84
            CRS_REGISTRY.put(SRS_URI.DEFAULT_WKT_CRS84, crs);
            AXIS_XY_REGISTRY.put(SRS_URI.DEFAULT_WKT_CRS84, Boolean.TRUE);
            UNITS_OF_MEASURE_REGISTRY.put(SRS_URI.DEFAULT_WKT_CRS84, unitsOfMeasure);

            //WGS_84 Legacy for CRS_84
            CRS_REGISTRY.put(SRS_URI.WGS84_CRS_GEOSPARQL_LEGACY, crs);
            AXIS_XY_REGISTRY.put(SRS_URI.WGS84_CRS_GEOSPARQL_LEGACY, Boolean.TRUE);
            UNITS_OF_MEASURE_REGISTRY.put(SRS_URI.WGS84_CRS_GEOSPARQL_LEGACY, unitsOfMeasure);

        } catch (FactoryException ex) {
            LOGGER.error("Invalid CRS code: {} - {}", DEFAULT_WKT_CRS84_CODE, ex.getMessage());
        }
    }

    public static final void reset() {
        CRS_REGISTRY.clear();
        AXIS_XY_REGISTRY.clear();
        UNITS_OF_MEASURE_REGISTRY.clear();
        setupDefaultCRS();
    }

    private static final String NORTH_UTM_EPSG = EPSG_BASE_CRS_URI + "326";
    private static final String SOUTH_UTM_EPSG = EPSG_BASE_CRS_URI + "327";
    private static final DecimalFormat ZONE_FORMATTER = new DecimalFormat("##");

    /**
     * Find UTM CRS from WGS84 coordinates.<br>
     * Based on calculation from Stack Overflow.
     *
     * @param latitude
     * @param longitude
     * @return
     * @see
     * <a href="https://stackoverflow.com/questions/176137/java-convert-lat-lon-to-utm">Stack
     * Overflow question relating to WGS84 to UTM conversion.</a>
     * @see
     * <a href="https://en.wikipedia.org/wiki/Universal_Transverse_Mercator_coordinate_system">Wikipedia
     * article on UTM.</a>
     * @see
     * <a href="http://epsg.io/32600">EPSG for UTM</a>
     *
     */
    public static final String findUTMZoneURIFromWGS84(double latitude, double longitude) {
        int zone = (int) Math.floor(longitude / 6 + 31);
        String zoneString = ZONE_FORMATTER.format(zone);

        boolean isNorth = latitude >= 0;

        String epsgURI;
        if (isNorth) {
            epsgURI = NORTH_UTM_EPSG + zoneString;
        } else {
            epsgURI = SOUTH_UTM_EPSG + zoneString;
        }

        return epsgURI;
    }

}
