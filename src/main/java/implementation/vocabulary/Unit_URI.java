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
public interface Unit_URI {

    //NonLinear - degrees
    public static final String DEGREE_ANGLE_URI = Prefixes.UOM_URI + "degree";
    public static final String MINUTE_ANGLE_URI = Prefixes.UOM_URI + "minute";
    public static final String SECOND_ANGLE_URI = Prefixes.UOM_URI + "second";

    //NonLinear - radians
    public static final String RADIAN_URI = Prefixes.UOM_URI + "radian";
    public static final String CENTIRADIAN_URI = Prefixes.UOM_URI + "centiradian";
    public static final String GRIDSPACING_URI = Prefixes.UOM_URI + "GridSpacing";

    //Linear - metres
    public static final String METRE_URI = Prefixes.UOM_URI + "metre";
    public static final String METER_URI = Prefixes.UOM_URI + "meter";
    public static final String KILOMETRE_URI = Prefixes.UOM_URI + "kilometre";
    public static final String KILOMETER_URI = Prefixes.UOM_URI + "kilometer";
    public static final String CENTIMETRE_URI = Prefixes.UOM_URI + "centimetre";
    public static final String CENTIMETER_URI = Prefixes.UOM_URI + "centimeter";
    public static final String MILLIMETRE_URI = Prefixes.UOM_URI + "millimetre";
    public static final String MILLIMETER_URI = Prefixes.UOM_URI + "millimeter";

    //Linear - miles
    public static final String MILE_URI = Prefixes.UOM_URI + "mile";
    public static final String YARD_URI = Prefixes.UOM_URI + "yard";
    public static final String FOOT_URI = Prefixes.UOM_URI + "foot";
    public static final String INCH_URI = Prefixes.UOM_URI + "inch";
    public static final String FOOT_SURVEY_US_URI = Prefixes.UOM_URI + "footsurveyUS";
    public static final String NAUTICAL_MILE_URI = Prefixes.UOM_URI + "nauticalmile";

    //Unity - ??
    public static final String UNITY_URI = Prefixes.UOM_URI + "unity";

    //URN references in: http://portal.opengeospatial.org/files/?artifact_id=21630
    public static final String RADIAN_URN = "urn:ogc:def:uom:EPSG::9102";
    public static final String DEGREE_ANGLE_URN = "urn:ogc:def:uom:EPSG::9101";
    public static final String METRE_URN = "urn:ogc:def:uom:EPSG::9001";
}
