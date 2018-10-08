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
     * @return Distance after conversion.
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
