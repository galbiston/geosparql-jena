/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.registry.UnitsRegistry;
import java.io.Serializable;
import java.util.Objects;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.apache.sis.measure.Quantities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Based on:
 * http://www.geoapi.org/3.0/javadoc/org/opengis/referencing/doc-files/WKT.html
 * Based on: http://docs.opengeospatial.org/is/12-063r5/12-063r5.html
 *
 * Based on: https://sis.apache.org/apidocs/org/apache/sis/measure/Units.html
 *
 *
 */
public class UnitsOfMeasure implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitsOfMeasure.class);

    private final Unit unit;
    private final String unitURI;
    private final boolean isLinearUnits;

    public UnitsOfMeasure(CoordinateReferenceSystem crs) {
        this.unit = crs.getCoordinateSystem().getAxis(0).getUnit();
        this.unitURI = UnitsRegistry.getUnitURI(unit);
        this.isLinearUnits = UnitsRegistry.isLinearUnits(unitURI);
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
    public static final Double conversion(double sourceDistance, String sourceDistanceUnitsURI, String targetDistanceUnitsURI) {

        Boolean isSourceUnitsLinear = UnitsRegistry.isLinearUnits(sourceDistanceUnitsURI);
        Boolean isTargetUnitsLinear = UnitsRegistry.isLinearUnits(targetDistanceUnitsURI);

        if (!isSourceUnitsLinear.equals(isTargetUnitsLinear)) {
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

        Quantity<Length> distance = Quantities.create(sourceDistance, sourceUnit);
        Quantity<Length> targetDistance = distance.to(targetUnit);

        return targetDistance.getValue().doubleValue();
    }

    @Override
    public String toString() {
        return "UnitsOfMeasure{" + "unit=" + unit + ", unitURI=" + unitURI + ", isLinearUnits=" + isLinearUnits + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.unit);
        hash = 71 * hash + Objects.hashCode(this.unitURI);
        hash = 71 * hash + (this.isLinearUnits ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UnitsOfMeasure other = (UnitsOfMeasure) obj;
        if (this.isLinearUnits != other.isLinearUnits) {
            return false;
        }
        if (!Objects.equals(this.unitURI, other.unitURI)) {
            return false;
        }
        return Objects.equals(this.unit, other.unit);
    }

}
