/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.vocabulary;

import implementation.support.UnitsOfMeasure;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * URIs based on: http://www.opengis.net/def/uom/OGC/1.0/ URNs based on:
 * http://portal.opengeospatial.org/files/?artifact_id=21630
 *
 * @author Gregory Albiston
 */
public class UnitsOfMeasureLookUp {

    //Units Of Measurement URI & URN
    public static final String METRE_URI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
    public static final String METRE_URN = "urn:ogc:def:uom:EPSG::9001";
    public static final Unit METRE_UNIT = SI.METER;

    public static final String DEGREE_URI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
    public static final String DEGREE_URN = "urn:ogc:def:uom:EPSG::9101";
    public static final Unit DEGREE_UNIT = NonSI.DEGREE_ANGLE;

    public static final String GRIDSPACING_URI = "http://www.opengis.net/def/uom/OGC/1.0/GridSpacing";

    public static final String RADIAN_URI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
    public static final String RADIAN_URN = "urn:ogc:def:uom:EPSG::9102";
    public static final Unit RADIAN_UNIT = SI.RADIAN;

    public static final String UNITY_URI = "http://www.opengis.net/def/uom/OGC/1.0/unity";

    public enum UnitType {
        METRE, DEGREE, GRIDSPACING, RADIAN, UNITY
    };

    //TODO Add additional measure distances such as kilometres and miles.
    public static final Unit getUnit(String unitURI) {

        Unit unit;
        switch (unitURI) {
            case METRE_URI:
                unit = METRE_UNIT;
                break;
            case RADIAN_URI:
            case RADIAN_URN:
                unit = RADIAN_UNIT;
                break;
            case DEGREE_URI:
            case DEGREE_URN:
                unit = DEGREE_UNIT;
                break;
            default:
                unit = null;
        }
        return unit;
    }

    public static final String getUnitURI(UnitsOfMeasure unitOfMeasure) {

        String unitURI;
        Unit unit = unitOfMeasure.getUnit();
        if (unit.equals(METRE_UNIT)) {
            unitURI = METRE_URI;
        } else if (unit.equals(RADIAN_UNIT)) {
            unitURI = RADIAN_URI;
        } else if (unit.equals(DEGREE_UNIT)) {
            unitURI = DEGREE_URI;
        } else {
            unitURI = "";
        }

        return unitURI;
    }

    public static final Boolean isProjected(String unitURI) {

        Boolean isProjected;
        switch (unitURI) {
            case METRE_URI:
                isProjected = true;
                break;
            case RADIAN_URI:
            case RADIAN_URN:
                isProjected = false;
                break;
            case DEGREE_URI:
            case DEGREE_URN:
                isProjected = false;
                break;
            default:
                isProjected = null;
        }
        return isProjected;
    }

}
