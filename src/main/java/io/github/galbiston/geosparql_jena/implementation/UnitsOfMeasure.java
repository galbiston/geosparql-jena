/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.implementation;

import io.github.galbiston.geosparql_jena.implementation.registry.UnitsRegistry;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import java.io.Serializable;
import java.util.Objects;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.apache.sis.measure.Quantities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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

    private final Unit unit;
    private final String unitURI;
    private final boolean isLinearUnits;

    public static final UnitsOfMeasure METRE_UNITS = new UnitsOfMeasure(Unit_URI.METRE_URL);
    public static final UnitsOfMeasure DEGREE_UNITS = new UnitsOfMeasure(Unit_URI.DEGREE_URL);

    //https://en.wikipedia.org/wiki/Decimal_degrees
    public static final double EQUATORIAL_DEGREE_TO_METRES = 111319.8922;
    public static final double EARTH_MEAN_RADIUS = 6371008.7714; //Earth Mean Radius

    public UnitsOfMeasure(CoordinateReferenceSystem crs) {
        this.unit = crs.getCoordinateSystem().getAxis(0).getUnit();
        this.unitURI = UnitsRegistry.getUnitURI(unit);
        this.isLinearUnits = UnitsRegistry.isLinearUnits(unitURI);
    }

    public UnitsOfMeasure(String unitURI) {
        this.unit = UnitsRegistry.getUnit(unitURI);
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
     * @return Distance after conversion.
     */
    @SuppressWarnings("unchecked")
    public static final Double conversion(double sourceDistance, String sourceDistanceUnitsURI, String targetDistanceUnitsURI) {

        return conversion(sourceDistance, new UnitsOfMeasure(sourceDistanceUnitsURI), new UnitsOfMeasure(targetDistanceUnitsURI));
    }

    /**
     * Conversion from target distance in units to source Units Of Measure.
     *
     * @param sourceDistance
     * @param sourceUnits
     * @param targetUnits
     * @return Distance after conversion.
     */
    @SuppressWarnings("unchecked")
    public static final Double conversion(double sourceDistance, UnitsOfMeasure sourceUnits, UnitsOfMeasure targetUnits) {

        Boolean isSourceUnitsLinear = sourceUnits.isLinearUnits();
        Boolean isTargetUnitsLinear = targetUnits.isLinearUnits();

        if (!isSourceUnitsLinear.equals(isTargetUnitsLinear)) {
            throw new UnitsConversionException("Conversion between linear and non-linear units not supported (convertBetween method): " + sourceUnits.unitURI + " and " + targetUnits.unitURI);
        }

        //Source and Target are the same units, so return the source distance.
        if (sourceUnits.unitURI.equals(targetUnits.unitURI)) {
            return sourceDistance;
        }

        //Find which type of measure for the distance is being used.
        Unit sourceUnit = sourceUnits.getUnit();
        Unit targetUnit = targetUnits.getUnit();

        Quantity<Length> distance = Quantities.create(sourceDistance, sourceUnit);
        Quantity<Length> targetDistance = distance.to(targetUnit);

        return targetDistance.getValue().doubleValue();

    }

    /**
     * Convert between linear and non-linear units and vice versa.<br>
     * Will convert linear/linear and non-linear/non-linear units.
     *
     * @param distance
     * @param unitsURI
     * @param targetDistanceUnitsURI
     * @param isTargetUnitsLinear
     * @param latitude
     * @return Distance in target units.
     */
    public static final double convertBetween(double distance, String unitsURI, String targetDistanceUnitsURI, boolean isTargetUnitsLinear, double latitude) {
        double targetDistance;
        if (isTargetUnitsLinear) {
            double metresDistance = UnitsOfMeasure.convertToMetres(distance, unitsURI, latitude);
            targetDistance = UnitsOfMeasure.conversion(metresDistance, Unit_URI.METRE_URL, targetDistanceUnitsURI);
        } else {
            double degreesDistance = UnitsOfMeasure.convertToDegrees(distance, unitsURI, latitude);
            targetDistance = UnitsOfMeasure.conversion(degreesDistance, Unit_URI.DEGREE_URL, targetDistanceUnitsURI);
        }

        return targetDistance;
    }

    /**
     * Provides conversion of linear units to degrees.
     * <br> Conversion from linear (i.e. metres) to degrees based on equatorial
     * radius of 111.32km.
     * <br> Therefore, this should only be used for rough bounding area before
     * using more precise distance methods of GeometryWrapper.
     *
     * @param distance
     * @param unitsURI
     * @param latitude
     * @return Converted distance in the provided units.
     */
    public static final double convertToDegrees(double distance, String unitsURI, double latitude) {

        UnitsOfMeasure units = new UnitsOfMeasure(unitsURI);

        if (units.isLinearUnits()) {
            double latitudeRadians = Math.toRadians(latitude);
            double longitudeRatio = Math.cos(latitudeRadians) * EQUATORIAL_DEGREE_TO_METRES;
            double metreDistance = UnitsOfMeasure.conversion(distance, units, METRE_UNITS);
            return metreDistance / longitudeRatio;
        } else {
            return UnitsOfMeasure.conversion(distance, units, DEGREE_UNITS);
        }

    }

    public static final double convertToMetres(double distance, String unitsURI, double latitude) {
        UnitsOfMeasure units = new UnitsOfMeasure(unitsURI);
        if (!units.isLinearUnits()) {
            double latitudeRadians = Math.toRadians(latitude);
            double longitudeRatio = Math.cos(latitudeRadians) * EQUATORIAL_DEGREE_TO_METRES;
            double degreeDistance = UnitsOfMeasure.conversion(distance, units, DEGREE_UNITS);
            return degreeDistance * longitudeRatio;
        } else {
            return UnitsOfMeasure.conversion(distance, units, METRE_UNITS);
        }
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
    public boolean equals(Object obj
    ) {
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
