/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.vocabulary;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * Based on: http://www.opengis.net/def/uom/OGC/1.0/
 *
 * @author Gregory Albiston
 */
public class UnitsOfMeasure {

    //Units Of Measurement URI
    public static final String METRE_URI = "http://www.opengis.net/def/uom/OGC/1.0/metre";
    public static final Unit METRE_UNIT = SI.METER;

    public static final String DEGREE_URI = "http://www.opengis.net/def/uom/OGC/1.0/degree";
    public static final Unit DEGREE_UNIT = NonSI.DEGREE_ANGLE;

    public static final String GRIDSPACING_URI = "http://www.opengis.net/def/uom/OGC/1.0/GridSpacing";

    public static final String RADIAN_URI = "http://www.opengis.net/def/uom/OGC/1.0/radian";
    public static final Unit RADIAN_UNIT = SI.RADIAN;

    public static final String UNITY_URI = "http://www.opengis.net/def/uom/OGC/1.0/unity";

    //TODO Add additional measure distances such as kilometres and miles.
}
