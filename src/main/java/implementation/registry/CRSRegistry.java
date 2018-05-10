/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.registry;

import implementation.UnitsOfMeasure;
import implementation.vocabulary.SRS_URI;
import static implementation.vocabulary.SRS_URI.EPSG_BASE_CRS_URI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Set;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class CRSRegistry implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final HashMap<String, CoordinateReferenceSystem> CRS_REGISTRY = new HashMap<>();
    private static final HashMap<String, UnitsOfMeasure> UNITS_OF_MEASURE_REGISTRY = new HashMap<>();

    public static final String DEFAULT_WKT_CRS84_STRING = "GEOGCS[\"CRS 84\", \n"
            + "  DATUM[\"WGS_1984\", \n"
            + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
            + "    AUTHORITY[\"EPSG\",\"6326\"]], \n"
            + "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
            + "  UNIT[\"degree\", 0.017453292519943295], \n"
            + "  AXIS[\"Geodetic longitude\", EAST], \n"
            + "  AXIS[\"Geodetic latitude\", NORTH], \n"
            + "  AUTHORITY[\"OGC\", 4326]]";

    static {
        //TODO Replace with DefaultGeographicCRS.WGS84?? Has axis in lon, lat. Returns 4326 on EPSG.
        addDefaultCRS();
    }

    public static final CoordinateReferenceSystem getCRS(String srsURI) {

        CoordinateReferenceSystem crs = addCRS(srsURI);
        return crs;
    }

    public static final UnitsOfMeasure getUnitsOfMeasure(String srsURI) {

        addCRS(srsURI);
        return UNITS_OF_MEASURE_REGISTRY.get(srsURI);
    }

    public static final CoordinateReferenceSystem addCRS(String srsURI) {
        return storeCRS(srsURI, null);
    }

    public static final CoordinateReferenceSystem addCRS(String srsURI, String wktString) {

        try {
            CoordinateReferenceSystem crs = CRS.parseWKT(wktString);
            return storeCRS(srsURI, crs);
        } catch (FactoryException ex) {
            LOGGER.error("Invalid WKT String: {} - {} - {}", srsURI, wktString, ex.getMessage());
            return null;
        }
    }

    public static final CoordinateReferenceSystem addCRS(String srsURI, CoordinateReferenceSystem crs) {
        return storeCRS(srsURI, crs);
    }

    private static CoordinateReferenceSystem storeCRS(String srsURI, CoordinateReferenceSystem crs) {

        if (CRS_REGISTRY.containsKey(srsURI)) {
            crs = CRS_REGISTRY.get(srsURI);
        } else {

            //Find the CRS based on the SRS.
            try {
                if (srsURI.equals(SRS_URI.WGS84_CRS_GEOPSARQL_LEGACY)) {
                    crs = CRS.decode(SRS_URI.WGS84_CRS);
                    LOGGER.warn("Legacy GeoSPARQL CRS found: {}. Using GeoSPARQL 1.0 URI version {} CRS. Dataset should be updated but no impact on operation.", SRS_URI.WGS84_CRS_GEOPSARQL_LEGACY, SRS_URI.WGS84_CRS);
                } else if (crs == null) {
                    crs = CRS.decode(srsURI);
                }
            } catch (FactoryException ex) {
                LOGGER.error("SRS URI Unrecongised: {} - {}", srsURI, ex.getMessage());
                return null;
            }

            CRS_REGISTRY.put(srsURI, crs);

            UnitsOfMeasure unitsOfMeasure = new UnitsOfMeasure(crs);
            UNITS_OF_MEASURE_REGISTRY.put(srsURI, unitsOfMeasure);
            UnitsRegistry.addUnit(unitsOfMeasure);
        }

        return crs;
    }

    private static void addDefaultCRS() {
        addCRS(SRS_URI.DEFAULT_WKT_CRS84, DEFAULT_WKT_CRS84_STRING);
        addCRS(SRS_URI.GEOTOOLS_GEOCENTRIC_CARTESIAN, DefaultGeocentricCRS.CARTESIAN);
    }

    public static final void clearAll() {
        CRS_REGISTRY.clear();
        UNITS_OF_MEASURE_REGISTRY.clear();
        addDefaultCRS();
    }

    ///////////////////////////////////////Registry Writing and Reading to File////////////////////////////
    public static final void writeCRSRegistry(File crsRegistryFile) {
        LOGGER.info("Writing CRS Registry - {}: Started", crsRegistryFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(crsRegistryFile))) {
            objectOutputStream.writeObject(CRS_REGISTRY.keySet());
        } catch (IOException ex) {
            LOGGER.error("Store CRS Registry exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing CRS Registry - {}: Completed", crsRegistryFile);
    }

    public static final void readCRSRegistry(File crsRegistryFile) {
        LOGGER.info("Reading CRS Registry - {}: Started", crsRegistryFile);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(crsRegistryFile))) {
            @SuppressWarnings("unchecked")
            Set<String> crsRegistryKeys = (Set<String>) objectInputStream.readObject();
            for (String key : crsRegistryKeys) {
                addCRS(key);
            }

        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read CRS Registry exception: {}", ex.getMessage());
        }
        LOGGER.info("Reading CRS Registry - {}: Completed", crsRegistryFile);
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
