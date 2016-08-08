/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vocabulary.Vocabulary;

/**
 * Default unit in JTS is central angle degrees That's why we need a
 * UnitConverter to convert it to other OGC supported units (like Meter)
 *
 * Generally there are 2 ways to treat the figure of earth: 1: As oblate
 * spheroid 2: As sphere
 *
 * @author haozhechen
 */
public class UomConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UomConverter.class);

    public static double ConvertToUnit(String destiUnit, double distance) {

        //convert to radian
        double radians = Math.toRadians(distance);

        switch (destiUnit) {
            case Vocabulary.METRE_URI:
                LOGGER.info("Current Uint of Measurement: METRE");
                //use formula: s = rθ to calculate distance in metre
                distance = radians * 6371000.0;
                break;
            case Vocabulary.DEGREE_URI:
                LOGGER.info("Current Uint of Measurement: DEGREE");
                //the default unit is degree
                break;
            case Vocabulary.GRIDSPACING_URI:
                LOGGER.info("GRID SPACING is not supported currently");
                break;
            case Vocabulary.RADIAN_URI:
                LOGGER.info("Current Uint of Measurement: RADIAN");
                distance = radians;
                break;
            case Vocabulary.UNITY_URI:
                LOGGER.info("UNITY is not supported currently");
                //unitless ratio of two quantities with the same units
                break;
            default:
                LOGGER.error("Unknow Unit Type: {}", destiUnit);
                distance = -1;
                break;
        }

        return distance;
    }

}
