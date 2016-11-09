/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.support;

import javax.measure.Measure;
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

    public static final double conversion(double distance, String distanceUnitURI, UnitsOfMeasure targetUnitsOfMeasure) {

        //TODO Test this properly.
        Unit targetUnit = targetUnitsOfMeasure.unit;
        boolean isTargetProjected = targetUnitsOfMeasure.isProjected;

        //Find the which distance type is being obtained.
        boolean isDistanceProjected;
        Unit distanceUnit;
        switch (distanceUnitURI) {
            case implementation.vocabulary.UnitsOfMeasure.METRE_URI:
                distanceUnit = implementation.vocabulary.UnitsOfMeasure.METRE_UNIT;
                isDistanceProjected = true;
                break;
            case implementation.vocabulary.UnitsOfMeasure.RADIAN_URI:
                distanceUnit = implementation.vocabulary.UnitsOfMeasure.RADIAN_UNIT;
                isDistanceProjected = false;
                break;
            case implementation.vocabulary.UnitsOfMeasure.DEGREE_URI:
                distanceUnit = implementation.vocabulary.UnitsOfMeasure.DEGREE_UNIT;
                isDistanceProjected = false;
                break;
            default:
                LOGGER.warn("Distance Unit URI Not Supported: {}", distanceUnitURI);
                LOGGER.warn("Treating as CRS units");
                distanceUnit = targetUnit;
                isDistanceProjected = isTargetProjected;
        }

        Measure<Double, ?> endDist;

        if (isTargetProjected && !isDistanceProjected) {

            //Target is Length and Distance is Angle
            UnitConverter radsConverter = distanceUnit.getConverterTo(SI.RADIAN);
            double rads = radsConverter.convert(distance);
            endDist = Measure.valueOf(rads * RADIAN_TO_METRE_RATIO, SI.METER);

        } else if (!isTargetProjected && isDistanceProjected) {

            //Target is Angle and Distance is Length
            UnitConverter metresConverter = distanceUnit.getConverterTo(SI.RADIAN);
            double metres = metresConverter.convert(distance);
            endDist = Measure.valueOf(metres / RADIAN_TO_METRE_RATIO, SI.RADIAN);

        } else {

            //Target and Distance are same type.
            endDist = Measure.valueOf(distance, distanceUnit);

        }

        return endDist.doubleValue(targetUnit);

    }

}
