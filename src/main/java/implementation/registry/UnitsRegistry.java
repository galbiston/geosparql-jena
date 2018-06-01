/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.registry;

import implementation.UnitsOfMeasure;
import implementation.vocabulary.Unit_URI;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class UnitsRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Map<String, Unit> UNITS_REGISTRY = Collections.synchronizedMap(new HashMap<>());
    private static final Map<Unit, String> UNITS_URI_REGISTRY = Collections.synchronizedMap(new HashMap<>());

    static {

        //SI Units
        addUnit(Unit_URI.METRE_URI, SI.METER);
        addUnit(Unit_URI.METER_URI, SI.METER);
        addUnit(Unit_URI.KILOMETRE_URI, SI.KILOMETRE);
        addUnit(Unit_URI.KILOMETER_URI, SI.KILOMETRE);
        addUnit(Unit_URI.CENTIMETRE_URI, SI.CENTIMETRE);
        addUnit(Unit_URI.CENTIMETER_URI, SI.CENTIMETRE);
        addUnit(Unit_URI.MILLIMETRE_URI, SI.MILLIMETRE);
        addUnit(Unit_URI.MILLIMETER_URI, SI.MILLIMETRE);
        addUnit(Unit_URI.RADIAN_URI, SI.RADIAN);

        //NonSI Units
        addUnit(Unit_URI.DEGREE_ANGLE_URI, NonSI.DEGREE_ANGLE);
        addUnit(Unit_URI.MINUTE_ANGLE_URI, NonSI.MINUTE_ANGLE);
        addUnit(Unit_URI.SECOND_ANGLE_URI, NonSI.SECOND_ANGLE);
        addUnit(Unit_URI.CENTIRADIAN_URI, NonSI.CENTIRADIAN);
        addUnit(Unit_URI.MILE_URI, NonSI.MILE);
        addUnit(Unit_URI.YARD_URI, NonSI.YARD);
        addUnit(Unit_URI.FOOT_URI, NonSI.FOOT);
        addUnit(Unit_URI.INCH_URI, NonSI.INCH);
        addUnit(Unit_URI.FOOT_SURVEY_US_URI, NonSI.FOOT_SURVEY_US);
        addUnit(Unit_URI.NAUTICAL_MILE_URI, NonSI.NAUTICAL_MILE);

        //URN references in: http://portal.opengeospatial.org/files/?artifact_id=21630
        addUnit(Unit_URI.METRE_URN, SI.METER);
        addUnit(Unit_URI.RADIAN_URN, SI.RADIAN);
        addUnit(Unit_URI.DEGREE_ANGLE_URN, NonSI.DEGREE_ANGLE);
    }

    public static final void addUnit(String unitURI, Unit unit) {
        UNITS_REGISTRY.putIfAbsent(unitURI, unit);
        UNITS_URI_REGISTRY.putIfAbsent(unit, unitURI);
    }

    public static final void addUnit(UnitsOfMeasure unitsOfMeasure) {
        addUnit(unitsOfMeasure.getUnitURI(), unitsOfMeasure.getUnit());
    }

    public static final Unit getUnit(String unitURI) {
        return UNITS_REGISTRY.get(unitURI);
    }

    public static final String getUnitURI(UnitsOfMeasure unitOfMeasure) {
        return getUnitURI(unitOfMeasure.getUnit());
    }

    public static final String getUnitURI(Unit unit) {
        return UNITS_URI_REGISTRY.get(unit);
    }

    public static final Boolean isLinearUnits(String unitURI) {

        if (UNITS_REGISTRY.containsKey(unitURI)) {

            Unit unit = UNITS_REGISTRY.get(unitURI);
            Unit unitSI = unit.getStandardUnit();

            return unitSI.equals(SI.METER);
        } else {
            LOGGER.error("Unrecognised unit URI: {}", unitURI);
            return null;
        }
    }

}
