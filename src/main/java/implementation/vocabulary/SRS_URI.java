/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.vocabulary;

/**
 *
 *
 */
public interface SRS_URI {

    public static final String OSGB36_CRS = "http://www.opengis.net/def/crs/EPSG/0/27700";
    public static final String GREEK_GRID_CRS = "http://www.opengis.net/def/crs/EPSG/0/2100";
    /**
     * Default SRS_URI Name as GeoSPARQL Standard. Equivalent to WGS84 with axis
     * reversed.
     */
    public static final String DEFAULT_WKT_CRS84 = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";

    /**
     * Legacy SRS_URI Name used prior to finalisation of GeoSPARQL standard 1.0.
     */
    public static final String WGS84_CRS_GEOSPARQL_LEGACY = "http://www.opengis.net/def/crs/EPSG/4326";

    /**
     * WGS84 SRS_URI Name used for Latitude, Longitude in Geographic coordinate
     * reference system with units of radians.
     */
    public static final String WGS84_CRS = "http://www.opengis.net/def/crs/EPSG/0/4326";

    /**
     * Base part of the EPSG CRS URI. Needs a specific EPSG code adding to it.
     */
    public static final String EPSG_BASE_CRS_URI = "http://www.opengis.net/def/crs/EPSG/0/";

    /**
     * An alias URI for the default GeoTools Geocentric coordinate reference
     * system with units of metres.
     */
    public static final String GEOTOOLS_GEOCENTRIC_CARTESIAN = "http://example.org/geotools#DefaultGeocentricCartesian";

    /**
     * An alias URI for the default GeoTools Geographic coordinate reference
     * system with units of radians.
     */
    public static final String GEOTOOLS_GEOGRAPHIC_WGS84 = "http://example.org/geotools#DefaultGeocentricWGS84";

}
