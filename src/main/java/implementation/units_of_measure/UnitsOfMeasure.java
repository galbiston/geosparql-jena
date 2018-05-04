/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.units_of_measure;

import implementation.registry.UnitsRegistry;
import java.io.Serializable;
import javax.measure.Measure;
import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Based on:
 * http://www.geoapi.org/3.0/javadoc/org/opengis/referencing/doc-files/WKT.html
 * Based on: http://docs.opengeospatial.org/is/12-063r5/12-063r5.html
 *
 * Based on: http://docs.geotools.org/stable/userguide/library/opengis/unit.html
 *
 *
 */
public class UnitsOfMeasure implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitsOfMeasure.class);

    private final AxisOrder axisOrder;
    private final Unit unit;
    private final String unitURI;
    private final boolean isLinearUnits;

    public UnitsOfMeasure(CoordinateReferenceSystem crs) {
        this.axisOrder = CRS.getAxisOrder(crs);
        this.unit = crs.getCoordinateSystem().getAxis(0).getUnit();
        this.unitURI = UnitsRegistry.getUnitURI(unit);
        this.isLinearUnits = UnitsRegistry.isLinearUnits(unitURI);
    }

    public AxisOrder getAxisOrder() {
        return axisOrder;
    }

    public Unit getUnit() {
        return unit;
    }

    public String getUnitURI() {
        return unitURI;
    }

    public boolean isLinearUnits() {
        return isLinearUnits;
    }

    /**
     * Conversion from target distance in units to source Units Of Measure.
     *
     * @param sourceDistance
     * @param sourceDistanceUnitsURI
     * @param targetDistanceUnitsURI
     * @return
     */
    @SuppressWarnings("unchecked")
    public static final Double conversion(double sourceDistance, String sourceDistanceUnitsURI, String targetDistanceUnitsURI) throws ConversionException {

        Boolean isSourceUnitsLinear = UnitsRegistry.isLinearUnits(sourceDistanceUnitsURI);
        Boolean isTargetUnitsLinear = UnitsRegistry.isLinearUnits(targetDistanceUnitsURI);
        Unit intermediateUnits;
        if (isSourceUnitsLinear && isTargetUnitsLinear) {
            intermediateUnits = SI.METRE;
        } else if (!isSourceUnitsLinear && !isTargetUnitsLinear) {
            intermediateUnits = SI.RADIAN;
        } else {
            LOGGER.error("Conversion between linear and non-linear units not supported: {} and {}", sourceDistanceUnitsURI, targetDistanceUnitsURI);
            return null;
        }

        //Source and Target are the same units, so return the source distance.
        if (sourceDistanceUnitsURI.equals(targetDistanceUnitsURI)) {
            return sourceDistance;
        }

        //Find which type of measure for the distance is being used.
        Unit sourceUnit = UnitsRegistry.getUnit(sourceDistanceUnitsURI);
        Unit targetUnit = UnitsRegistry.getUnit(targetDistanceUnitsURI);

        //Convert to metres.
        UnitConverter sourceConvert = sourceUnit.getConverterTo(intermediateUnits);
        double intermediateDistance = sourceConvert.convert(sourceDistance);
        Measure<Double, ?> targetMeasure = Measure.valueOf(intermediateDistance, intermediateUnits);

        //Convert back to the target units of measure.
        double targetDistance = targetMeasure.doubleValue(targetUnit);
        return targetDistance;
    }

}
