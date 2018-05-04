/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package implementation.vocabulary;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class CRS_URI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String OSGB_CRS = "http://www.opengis.net/def/crs/EPSG/0/27700";
    /**
     * Default SRS Name as GeoSPARQL Standard. Equivalent to WGS84 with axis
     * reversed.
     */
    public static final String DEFAULT_WKT_CRS84 = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";
    public static final String WGS84_CRS_GEOPSARQL_LEGACY = "http://www.opengis.net/def/crs/EPSG/4326";
    public static final String WGS84_CRS = "http://www.opengis.net/def/crs/EPSG/0/4326";

}
