/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.support.UnitsOfMeasure;
import java.util.HashMap;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gregory Albiston
 */
public class UnitRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitRegistry.class);
    private static final HashMap<String, UnitsOfMeasure> UNITS_REGISTRY = new HashMap<>();

    public static final UnitsOfMeasure addUnits(String srsURI, CoordinateReferenceSystem crs) {

        UnitsOfMeasure unitsOfMeasure;

        if (!UNITS_REGISTRY.containsKey(srsURI)) {

            unitsOfMeasure = new UnitsOfMeasure(crs);
            UNITS_REGISTRY.put(srsURI, unitsOfMeasure);

        } else {
            unitsOfMeasure = UNITS_REGISTRY.get(srsURI);
        }
        return unitsOfMeasure;
    }

    public static final UnitsOfMeasure getUnits(String srsURI) {

        return UNITS_REGISTRY.get(srsURI);
    }

}
