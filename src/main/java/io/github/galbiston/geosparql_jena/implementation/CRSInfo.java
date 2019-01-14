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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.sis.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class CRSInfo {

    private final String srsURI;
    private final CoordinateReferenceSystem crs;
    private final UnitsOfMeasure unitsOfMeasure;
    private final Boolean isAxisXY;
    private final Boolean isGeographic;
    private final Boolean isSRSRecognised;
    private final Envelope domainEnvelope;
    public static final String DEFAULT_WKT_CRS84_CODE = "CRS:84";

    private static final List<AxisDirection> OTHER_Y_AXIS_DIRECTIONS = Arrays.asList(AxisDirection.NORTH_EAST, AxisDirection.NORTH_WEST, AxisDirection.SOUTH_EAST, AxisDirection.SOUTH_WEST, AxisDirection.NORTH_NORTH_EAST, AxisDirection.NORTH_NORTH_WEST, AxisDirection.SOUTH_SOUTH_EAST, AxisDirection.SOUTH_SOUTH_WEST);

    public CRSInfo(String srsURI) {
        this.srsURI = srsURI;

        try {
            this.crs = CRS.forCode(srsURI);
            this.isAxisXY = checkAxisXY(crs);
            this.unitsOfMeasure = new UnitsOfMeasure(crs);
            this.isSRSRecognised = true;
            this.isGeographic = crs instanceof GeographicCRS;
            this.domainEnvelope = buildDomainEnvelope(crs, isAxisXY);
        } catch (FactoryException ex) {
            throw new CRSInfoException("Invalid CRS code: " + srsURI + " - " + ex.getMessage(), ex);
        }
    }

    private CRSInfo(String srsURI, CoordinateReferenceSystem crs, boolean isSRSRecognised) {
        this.srsURI = srsURI;
        this.crs = crs;
        this.isAxisXY = checkAxisXY(crs);
        this.unitsOfMeasure = new UnitsOfMeasure(crs);
        this.isSRSRecognised = isSRSRecognised;
        this.isGeographic = crs instanceof GeographicCRS;
        this.domainEnvelope = buildDomainEnvelope(crs, isAxisXY);
    }

    protected static final Boolean checkAxisXY(CoordinateReferenceSystem crs) {

        AxisDirection axisDirection = crs.getCoordinateSystem().getAxis(0).getDirection();

        if (axisDirection.equals(AxisDirection.NORTH) || axisDirection.equals(AxisDirection.SOUTH)) {
            return false;
        } else if (axisDirection.equals(AxisDirection.EAST) || axisDirection.equals(AxisDirection.WEST)) {
            return true;
        } else {
            return !OTHER_Y_AXIS_DIRECTIONS.contains(axisDirection);
        }
    }

    protected static final Envelope buildDomainEnvelope(CoordinateReferenceSystem crs, Boolean isAxisXY) {

        org.opengis.geometry.Envelope crsDomain = CRS.getDomainOfValidity(crs);
        DirectPosition lowerCorner = crsDomain.getLowerCorner();
        DirectPosition upperCorner = crsDomain.getUpperCorner();

        int xAxis;
        int yAxis;
        if (isAxisXY) {
            xAxis = 0;
            yAxis = 1;
        } else {
            xAxis = 1;
            yAxis = 0;
        }

        double x1 = lowerCorner.getOrdinate(xAxis);
        double y1 = lowerCorner.getOrdinate(yAxis);
        double x2 = upperCorner.getOrdinate(xAxis);
        double y2 = upperCorner.getOrdinate(yAxis);

        Envelope envelope = new Envelope(x1, x2, y1, y2);
        return envelope;
    }

    /**
     * URI of the Spatial Reference System<br>
     * Identical values to SRID.
     *
     * @return
     */
    public String getSrsURI() {
        return srsURI;
    }

    /**
     * OpenGIS Coordinate Reference System.
     *
     * @return
     */
    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    /**
     * Units of Measure for the coordinate reference system.
     *
     * @return
     */
    public UnitsOfMeasure getUnitsOfMeasure() {
        return unitsOfMeasure;
    }

    /**
     * Check if axis is in XY order.
     *
     * @return
     */
    public Boolean isAxisXY() {
        return isAxisXY;
    }

    /**
     * Check if the SRS URI is recognised as a OpenGIS coordinate reference
     * system.
     *
     * @return
     */
    public Boolean isSRSRecognised() {
        return isSRSRecognised;
    }

    /**
     * Check if the CRS is geographic (i.e. latitude, longitude on a sphere).
     *
     * @return
     */
    public Boolean isGeographic() {
        return isGeographic;
    }

    /**
     * Domain of validity in XY coordinate order.
     *
     * @return
     */
    public Envelope getDomainEnvelope() {
        return domainEnvelope;
    }

    /**
     *
     * @param srsURI Allows alternative srsURI to be associated with CRS84.
     * @return CRSInfo with default setup for WKT without SRS URI.
     */
    public static final CRSInfo getDefaultWktCRS84(String srsURI) {

        try {
            CoordinateReferenceSystem crs = CRS.forCode(DEFAULT_WKT_CRS84_CODE);
            return new CRSInfo(srsURI, crs, true);
        } catch (FactoryException ex) {
            throw new CRSInfoException("Invalid CRS code: " + DEFAULT_WKT_CRS84_CODE + " - " + ex.getMessage(), ex);
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
            return new CRSInfo(srsURI, crs, false);
        } catch (FactoryException ex) {
            throw new CRSInfoException("Invalid CRS code: " + srsURI + " - " + ex.getMessage(), ex);
        }
    }

    @Override
    public String toString() {
        return "CRSInfo{" + "srsURI=" + srsURI + ", crs=" + crs + ", unitsOfMeasure=" + unitsOfMeasure + ", isAxisXY=" + isAxisXY + ", isSRSRecognised=" + isSRSRecognised + ", domainEnvelope=" + domainEnvelope + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.srsURI);
        hash = 23 * hash + Objects.hashCode(this.crs);
        hash = 23 * hash + Objects.hashCode(this.unitsOfMeasure);
        hash = 23 * hash + Objects.hashCode(this.isAxisXY);
        hash = 23 * hash + Objects.hashCode(this.isSRSRecognised);
        hash = 23 * hash + Objects.hashCode(this.domainEnvelope);
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
        if (!Objects.equals(this.isSRSRecognised, other.isSRSRecognised)) {
            return false;
        }
        return Objects.equals(this.domainEnvelope, other.domainEnvelope);
    }

}
