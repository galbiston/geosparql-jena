/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.support.UnitsOfMeasure;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import org.apache.commons.collections4.map.LRUMap;
import org.geotools.referencing.CRS;
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

    private static final LRUMap<String, CoordinateReferenceSystem> CRS_REGISTRY = new LRUMap<>(GeoSPARQLSupport.CRS_REGISTRY_MAX_SIZE);
    private static final LRUMap<String, UnitsOfMeasure> UNITS_REGISTRY = new LRUMap<>(GeoSPARQLSupport.UNITS_REGISTRY_MAX_SIZE);

    /**
     * Default SRS Name as GeoSPARQL Standard. Equivalent to WGS84 with axis
     * reversed.
     */
    public static final String DEFAULT_WKT_CRS84 = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

    public static final String OSGB_CRS = "http://www.opengis.net/def/crs/EPSG/0/27700";

    public static final String WGS84_CRS = "http://www.opengis.net/def/crs/EPSG/0/4326";

    public static final String WGS84_CRS_GEOPSARQL_LEGACY = "http://www.opengis.net/def/crs/EPSG/4326";

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
        addCRS(DEFAULT_WKT_CRS84, DEFAULT_WKT_CRS84_STRING);
    }

    public static final CoordinateReferenceSystem addCRS(String srsURI) {

        CoordinateReferenceSystem crs = null;
        if (!CRS_REGISTRY.containsKey(srsURI)) {

            try {
                if (srsURI.equals(WGS84_CRS_GEOPSARQL_LEGACY)) {
                    crs = CRS.decode(WGS84_CRS);
                    LOGGER.warn("Legacy GeoSPARQL CRS found: {}. Using GeoSPARQL 1.0 URI version {} CRS. Dataset should be updated but no impact on operation.", WGS84_CRS_GEOPSARQL_LEGACY, WGS84_CRS);
                } else {
                    crs = CRS.decode(srsURI);
                }
                CRS_REGISTRY.put(srsURI, crs);

                UnitsOfMeasure unitsOfMeasure = new UnitsOfMeasure(crs);
                UNITS_REGISTRY.put(srsURI, unitsOfMeasure);

            } catch (FactoryException ex) {
                LOGGER.error("CRS Parse Error: {} {}", srsURI, ex.getMessage());
            }
        } else {
            crs = CRS_REGISTRY.get(srsURI);
        }
        return crs;
    }

    public static final CoordinateReferenceSystem addCRS(String srsURI, String wktString) {

        CoordinateReferenceSystem crs = null;
        if (!CRS_REGISTRY.containsKey(srsURI)) {

            try {
                crs = CRS.parseWKT(wktString);

                CRS_REGISTRY.put(srsURI, crs);

                UnitsOfMeasure unitsOfMeasure = new UnitsOfMeasure(crs);
                UNITS_REGISTRY.put(srsURI, unitsOfMeasure);

            } catch (FactoryException ex) {
                LOGGER.error("CRS Parse Error: {} {}", srsURI, ex.getMessage());
            }
        } else {
            crs = CRS_REGISTRY.get(srsURI);
        }
        return crs;
    }

    public static final CoordinateReferenceSystem getCRS(String srsURI) {

        CoordinateReferenceSystem crs = addCRS(srsURI);
        return crs;
    }

    public static final UnitsOfMeasure getUnits(String srsURI) {

        addCRS(srsURI);
        return UNITS_REGISTRY.get(srsURI);
    }

    public static final void writeCRSRegistry(File crsRegistryFile) {
        LOGGER.info("Writing CRS Registry - {}: Started", crsRegistryFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(crsRegistryFile))) {
            objectOutputStream.writeObject(CRS_REGISTRY);
        } catch (IOException ex) {
            LOGGER.error("Store CRS Registry exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing CRS Registry - {}: Completed", crsRegistryFile);
    }

    public static final void readCRSRegistry(File crsRegistryFile) {
        LOGGER.info("Reading CRS Registry - {}: Started", crsRegistryFile);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(crsRegistryFile))) {
            @SuppressWarnings("unchecked")
            LRUMap<String, CoordinateReferenceSystem> crsRegistry = (LRUMap<String, CoordinateReferenceSystem>) objectInputStream.readObject();
            CRS_REGISTRY.putAll(crsRegistry);
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read CRS Registry exception: {}", ex.getMessage());
        }
        LOGGER.info("Reading CRS Registry - {}: Completed", crsRegistryFile);
    }

    public static final void writeUnitsRegistry(File unitsRegistryFile) {
        LOGGER.info("Writing Units Registry - {}: Started", unitsRegistryFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(unitsRegistryFile))) {
            objectOutputStream.writeObject(UNITS_REGISTRY);
        } catch (IOException ex) {
            LOGGER.error("Store Units Registry exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Units Registry - {}: Completed", unitsRegistryFile);
    }

    public static final void readUnitsRegistry(File unitsRegistryFile) {
        LOGGER.info("Reading Units Registry - {}: Started", unitsRegistryFile);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(unitsRegistryFile))) {
            @SuppressWarnings("unchecked")
            LRUMap<String, UnitsOfMeasure> unitsRegistry = (LRUMap<String, UnitsOfMeasure>) objectInputStream.readObject();
            UNITS_REGISTRY.putAll(unitsRegistry);
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Units Registry exception: {}", ex.getMessage());
        }
        LOGGER.info("Reading Units Registry - {}: Completed", unitsRegistryFile);
    }

    public static final void clearAll() {
        CRS_REGISTRY.clear();
        addCRS(DEFAULT_WKT_CRS84, DEFAULT_WKT_CRS84_STRING);
        UNITS_REGISTRY.clear();
    }

}
