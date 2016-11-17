/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.support;

import implementation.vocabulary.UnitsOfMeasureLookUp;
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
 * @author Gregory Albiston
 */
public class UnitsOfMeasure {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitsOfMeasure.class);

    private final boolean isProjected;
    private final Unit unit;
    private final AxisOrder axisOrder;

    public UnitsOfMeasure(CoordinateReferenceSystem crs) {
        this.axisOrder = CRS.getAxisOrder(crs);
        this.isProjected = !(CRS.getProjectedCRS(crs) == null);
        this.unit = CRS.getHorizontalCRS(crs).getCoordinateSystem().getAxis(0).getUnit();

    }

    public boolean isProjected() {
        return isProjected;
    }

    public Unit getUnit() {
        return unit;
    }

    public AxisOrder getAxisOrder() {
        return axisOrder;
    }

    //TODO Need authoritative source.
    //Using Earth mean radius for radian to metre ratio.
    private static final double RADIAN_TO_METRE_RATIO = 6371000.0;

    /**
     * Conversion from target distance in units to source Units Of Measure.
     *
     * @param targetDistance
     * @param targetDistanceUnitURI
     * @param sourceUnitsOfMeasure
     * @return
     */
    public static final double conversion(double targetDistance, String targetDistanceUnitURI, UnitsOfMeasure sourceUnitsOfMeasure) throws ConversionException {

        Unit sourceUnit = sourceUnitsOfMeasure.unit;
        boolean isSourceProjected = sourceUnitsOfMeasure.isProjected;

        //Find the which type of measure for the distance is being used.
        boolean isTargetProjected;
        Unit targetUnit = UnitsOfMeasureLookUp.getUnit(targetDistanceUnitURI);

        if (targetUnit != null) {
            isTargetProjected = UnitsOfMeasureLookUp.isProjected(targetDistanceUnitURI);
        } else {
            LOGGER.warn("Distance Unit URI Not Supported: {}", targetDistanceUnitURI);
            LOGGER.warn("Treating as same as source CRS units");
            targetUnit = sourceUnit;
            isTargetProjected = isSourceProjected;
        }

        //Convert the target distance into the source units of measure.
        //Done through intermediate step of converting to metres and radians if required.
        Measure<Double, ?> sourceDistance = null;

        if (targetUnit.equals(sourceUnit)) {
            //Source and Target are the same units
            return targetDistance;
        } else if (!isSourceProjected && !isTargetProjected) {

            //Source is Angle and Target is Angle
            //Convert to radians.
            UnitConverter radianConverter = targetUnit.getConverterTo(SI.RADIAN);
            double rads = radianConverter.convert(targetDistance);
            sourceDistance = Measure.valueOf(rads, SI.RADIAN);

        } else if (isSourceProjected && isTargetProjected) {

            //Source is Length and Target is Length
            //Convert to metres.
            UnitConverter metresConverter = targetUnit.getConverterTo(SI.METER);
            double metres = metresConverter.convert(targetDistance);
            sourceDistance = Measure.valueOf(metres, SI.METER);

        } else if (!isSourceProjected && isTargetProjected) {

            //Source is Angle and Target is Length
            //Convert to metres then adjust to radians.
            UnitConverter metresConverter = targetUnit.getConverterTo(SI.METER);
            double metres = metresConverter.convert(targetDistance);
            sourceDistance = Measure.valueOf(metres / RADIAN_TO_METRE_RATIO, SI.RADIAN);

        } else if (isSourceProjected && !isTargetProjected) {

            //Source is Length and Target is Angle
            //Convert to radians then adjust to metres.
            UnitConverter radsConverter = targetUnit.getConverterTo(SI.RADIAN);
            double rads = radsConverter.convert(targetDistance);
            sourceDistance = Measure.valueOf(rads * RADIAN_TO_METRE_RATIO, SI.METER);
        }

        //Finally convert back to the source units of measure.
        return sourceDistance.doubleValue(sourceUnit);

    }

}
