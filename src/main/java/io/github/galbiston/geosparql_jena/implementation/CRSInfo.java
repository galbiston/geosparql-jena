/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.implementation;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import org.apache.sis.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class CRSInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String srsURI;
    private final CoordinateReferenceSystem crs;
    private final UnitsOfMeasure unitsOfMeasure;
    private final Boolean isAxisXY;
    private final Boolean isSRSRecognised;
    public static final String DEFAULT_WKT_CRS84_CODE = "CRS:84";

    public CRSInfo(String srsURI, CoordinateReferenceSystem crs, UnitsOfMeasure unitsOfMeasure, Boolean isAxisXY, Boolean isSRSRecognised) {
        this.srsURI = srsURI;
        this.crs = crs;
        this.unitsOfMeasure = unitsOfMeasure;
        this.isAxisXY = isAxisXY;
        this.isSRSRecognised = isSRSRecognised;
    }

    public String getSrsURI() {
        return srsURI;
    }

    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    public UnitsOfMeasure getUnitsOfMeasure() {
        return unitsOfMeasure;
    }

    public Boolean isAxisXY() {
        return isAxisXY;
    }

    public Boolean isSRSRecognised() {
        return isSRSRecognised;
    }

    /**
     *
     * @param srsURI Allows alternative srsURI to be associated with CRS84.
     * @return CRSInfo with default setup for WKT without SRS URI.
     */
    public static final CRSInfo getDefaultWktCRS84(String srsURI) {

        try {
            CoordinateReferenceSystem crs = CRS.forCode(DEFAULT_WKT_CRS84_CODE);
            UnitsOfMeasure unitsOfMeasure = new UnitsOfMeasure(crs);
            return new CRSInfo(srsURI, crs, unitsOfMeasure, true, true);
        } catch (FactoryException ex) {
            LOGGER.error("Invalid CRS code: {} - {}", DEFAULT_WKT_CRS84_CODE, ex.getMessage());
            throw new CRSInfoException("Invalid CRS code: " + DEFAULT_WKT_CRS84_CODE + " - " + ex.getMessage());
        }

    }

    /**
     * Unrecognised SRS URI are assumed to follow the default CRS84 so that
     * operations do not error but may not complete as expected.
     *
     * @param srsURI
     * @return CRSInfo with default setup for WKT without SRS URI
     */
    public static final CRSInfo getUnrecognised(String srsURI) {

        try {
            CoordinateReferenceSystem crs = CRS.forCode(DEFAULT_WKT_CRS84_CODE);
            UnitsOfMeasure unitsOfMeasure = new UnitsOfMeasure(crs);
            return new CRSInfo(srsURI, crs, unitsOfMeasure, true, false);
        } catch (FactoryException ex) {
            LOGGER.error("Invalid CRS code: {} - {}", DEFAULT_WKT_CRS84_CODE, ex.getMessage());
            throw new CRSInfoException("Invalid CRS code: " + DEFAULT_WKT_CRS84_CODE + " - " + ex.getMessage());
        }
    }

    @Override
    public String toString() {
        return "CRSInfo{" + "srsURI=" + srsURI + ", crs=" + crs + ", unitsOfMeasure=" + unitsOfMeasure + ", isAxisXY=" + isAxisXY + ", isSRSRecognised=" + isSRSRecognised + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.srsURI);
        hash = 47 * hash + Objects.hashCode(this.crs);
        hash = 47 * hash + Objects.hashCode(this.unitsOfMeasure);
        hash = 47 * hash + Objects.hashCode(this.isAxisXY);
        hash = 47 * hash + Objects.hashCode(this.isSRSRecognised);
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
        final CRSInfo other = (CRSInfo) obj;
        if (!Objects.equals(this.srsURI, other.srsURI)) {
            return false;
        }
        if (!Objects.equals(this.crs, other.crs)) {
            return false;
        }
        if (!Objects.equals(this.unitsOfMeasure, other.unitsOfMeasure)) {
            return false;
        }
        if (!Objects.equals(this.isAxisXY, other.isAxisXY)) {
            return false;
        }
        return Objects.equals(this.isSRSRecognised, other.isSRSRecognised);
    }

}
