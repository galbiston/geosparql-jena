/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import implementation.support.DistanceUnitsEnum;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import implementation.support.UomVocabulary;

/**
 * Default unit in JTS is central angle degrees That's why we need a
 * UnitConverter to convert it to other OGC supported units (like Meter)
 *
 * Generally there are 2 ways to treat the figure of earth: 1: As oblate
 * spheroid 2: As sphere
 *
 * TODO - The above isn't the case as JTS is cartesian (i.e. planar) and has no
 * concept of distance. - toWKT on a CRS contains the spheroid or cartesian and
 * unit attribute of the coordinate system.
 *
 * @author haozhechen
 */
public class UomConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UomConverter.class);

    /**
     * This method converts the values that come form JTS to a value in the
     * destination Unit
     *
     * @param destiUnit - the destination Unit to be used
     * @param distance - the value that come form JTS
     * @deprecated
     */
    public static double ConvertToUnit(String destiUnit, double distance) {

        //convert to radian
        double radians = Math.toRadians(distance);

        switch (destiUnit) {
            case UomVocabulary.METRE_URI:
                LOGGER.error("Current Uint of Measurement: METRE");
                //use formula: s = rθ to calculate distance in metre
                distance = radians * 6371000.0;
                break;
            case UomVocabulary.DEGREE_URI:
                LOGGER.error("Current Uint of Measurement: DEGREE");
                //the default unit is degree
                break;
            case UomVocabulary.GRIDSPACING_URI:
                LOGGER.error("GRID SPACING is not supported currently");
                break;
            case UomVocabulary.RADIAN_URI:
                LOGGER.error("Current Uint of Measurement: RADIAN");
                distance = radians;
                break;
            case UomVocabulary.UNITY_URI:
                LOGGER.error("UNITY is not supported currently");
                //unitless ratio of two quantities with the same units
                break;
            default:
                LOGGER.error("Unknow Unit Type: {}", destiUnit);
                distance = -1;
                break;
        }

        return distance;
    }

    public static final DistanceUnitsEnum extract(NodeValue nodeValue) {

        Node node3 = nodeValue.asNode();
        String distanceUnitsURI = node3.getURI();

        switch (distanceUnitsURI) {
            case UomVocabulary.METRE_URI:
                return DistanceUnitsEnum.metres;
            case UomVocabulary.DEGREE_URI:
                return DistanceUnitsEnum.degrees;
            case UomVocabulary.GRIDSPACING_URI:
                return DistanceUnitsEnum.gridSpacing;
            case UomVocabulary.RADIAN_URI:
                return DistanceUnitsEnum.radians;
            case UomVocabulary.UNITY_URI:
                return DistanceUnitsEnum.unity;
            default:
                LOGGER.error("Unknown Distance Unit URI: {}", distanceUnitsURI);
                return DistanceUnitsEnum.unknown;
        }

    }

    public static final DistanceUnitsEnum extract(String unitsString) {

        //TODO Check string values are correct.
        switch (unitsString.toUpperCase()) {
            case "METRE":
                return DistanceUnitsEnum.metres;
            case "DEGREE":
                return DistanceUnitsEnum.degrees;
            case "GRID SPACING":
                return DistanceUnitsEnum.gridSpacing;
            case "RADIAN":
                return DistanceUnitsEnum.radians;
            case "UNITY":
                return DistanceUnitsEnum.unity;
            default:
                LOGGER.error("Unknown Distance Unit: {}", unitsString);
                return DistanceUnitsEnum.unknown;
        }

    }

    public static final double conversion(double sourceDistance, DistanceUnitsEnum sourceDistanceUnits, DistanceUnitsEnum targetDistanceUnits) {

        //TODO Convert between the different combinations.
        //TODO No mention of kilometres, centimetres, millimetres???
        return;
    }

}
