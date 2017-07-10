/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.support.UnitsOfMeasure;
import java.util.HashMap;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gregory Albiston
 */
public class CRSRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRSRegistry.class);

    private static final HashMap<String, CoordinateReferenceSystem> CRS_REGISTRY = new HashMap<>();
    private static final HashMap<String, UnitsOfMeasure> UNITS_REGISTRY = new HashMap<>();

    /**
     * Default SRS Name as GeoSPARQL Standard. Equivalent to WGS84 with axis
     * reversed.
     */
    public static final String DEFAULT_WKT_CRS = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

    public static final String OSGB_CRS = "http://www.opengis.net/def/crs/EPSG/0/27700";

    public static final String WGS84_CRS = "http://www.opengis.net/def/crs/EPSG/0/4326";

    static {
        String default_CRS_WKT = "GEOGCS[\"CRS 84\", \n"
                + "  DATUM[\"WGS_1984\", \n"
                + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
                + "    AUTHORITY[\"EPSG\",\"6326\"]], \n"
                + "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                + "  UNIT[\"degree\", 0.017453292519943295], \n"
                + "  AXIS[\"Geodetic longitude\", EAST], \n"
                + "  AXIS[\"Geodetic latitude\", NORTH], \n"
                + "  AUTHORITY[\"OGC\", 4326]]";

        //TODO Replace with DefaultGeographicCRS.WGS84?? HAs axis in lon, lat. Returns 4326 on EPSG.
        addCRS(DEFAULT_WKT_CRS, default_CRS_WKT);
    }

    public static final CoordinateReferenceSystem addCRS(String srsURI) {

        CoordinateReferenceSystem crs = null;
        if (!CRS_REGISTRY.containsKey(srsURI)) {

            try {
                crs = CRS.decode(srsURI);
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

}
