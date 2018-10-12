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

import io.github.galbiston.geosparql_jena.implementation.datatype.GMLDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.GeometryDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.geosparql_jena.implementation.index.GeometryLiteralIndex.GeometryIndex;
import io.github.galbiston.geosparql_jena.implementation.index.GeometryTransformIndex;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomCoordinateSequence;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomGeometryFactory;
import io.github.galbiston.geosparql_jena.implementation.registry.CRSRegistry;
import io.github.galbiston.geosparql_jena.implementation.registry.MathTransformRegistry;
import io.github.galbiston.geosparql_jena.implementation.registry.UnitsRegistry;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI;
import java.io.Serializable;
import java.util.Objects;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.sis.geometry.DirectPosition2D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.IntersectionMatrix;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class GeometryWrapper implements Serializable {

    private final DimensionInfo dimensionInfo;
    private final CRSInfo crsInfo;
    private final Geometry xyGeometry;
    private final Geometry parsingGeometry;
    private PreparedGeometry preparedGeometry;
    private final String geometryDatatypeURI;
    private String lexicalForm;
    private String utmURI = null;

    public GeometryWrapper(Geometry geometry, String srsURI, String geometryDatatypeURI, DimensionInfo dimensionInfo) {
        this(geometry, srsURI, geometryDatatypeURI, dimensionInfo, null);
    }

    public GeometryWrapper(Geometry geometry, String srsURI, String geometryDatatypeURI, DimensionInfo dimensionInfo, String geometryLiteral) {
        this(geometry, GeometryReverse.check(geometry, srsURI.isEmpty() ? SRS_URI.DEFAULT_WKT_CRS84 : srsURI), srsURI.isEmpty() ? SRS_URI.DEFAULT_WKT_CRS84 : srsURI, geometryDatatypeURI, dimensionInfo, geometryLiteral);
    }

    private GeometryWrapper(Geometry parsingGeometry, Geometry xyGeometry, String srsURI, String geometryDatatypeURI, DimensionInfo dimensionInfo, String lexicalForm) {

        this.parsingGeometry = parsingGeometry;
        this.xyGeometry = xyGeometry;
        this.preparedGeometry = null; //Initialised when required by spatial relations checkPreparedGeometry.

        this.geometryDatatypeURI = geometryDatatypeURI;

        if (srsURI.isEmpty()) {
            srsURI = SRS_URI.DEFAULT_WKT_CRS84;
        }

        this.crsInfo = CRSRegistry.getCRSInfo(srsURI);

        this.dimensionInfo = dimensionInfo;
        this.lexicalForm = lexicalForm;
    }

    /**
     * Default to WGS84 and XY coordinate dimensions.
     *
     * @param geometry
     * @param geometryDatatypeURI
     */
    public GeometryWrapper(Geometry geometry, String geometryDatatypeURI) {
        this(geometry, "", geometryDatatypeURI, DimensionInfo.XY_POINT());
    }

    /**
     * Default to XY coordinate dimensions.
     *
     * @param geometry
     * @param srsURI
     * @param geometryDatatypeURI
     */
    public GeometryWrapper(Geometry geometry, String srsURI, String geometryDatatypeURI) {
        this(geometry, srsURI, geometryDatatypeURI, DimensionInfo.XY_POINT());
    }

    transient private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    /**
     * Empty geometry with specified parameters.
     *
     * @param srsURI
     * @param geometryDatatypeURI
     * @param dimensionInfo
     */
    public GeometryWrapper(String srsURI, String geometryDatatypeURI, DimensionInfo dimensionInfo) {
        this(new CustomCoordinateSequence(DimensionInfo.XY_POINT().getDimensions()), geometryDatatypeURI, srsURI);
    }

    /**
     * Point geometry with specified SRS.
     *
     * @param pointCoordinateSequence
     * @param geometryDatatypeURI
     * @param srsURI
     */
    public GeometryWrapper(CustomCoordinateSequence pointCoordinateSequence, String geometryDatatypeURI, String srsURI) {
        this(GEOMETRY_FACTORY.createPoint(pointCoordinateSequence), srsURI, geometryDatatypeURI, DimensionInfo.XY_POINT());
    }

    /**
     * Copy GeometryWrapper.
     *
     * @param geometryWrapper
     */
    public GeometryWrapper(GeometryWrapper geometryWrapper) {

        this.xyGeometry = geometryWrapper.xyGeometry;
        this.parsingGeometry = geometryWrapper.parsingGeometry;
        this.preparedGeometry = geometryWrapper.preparedGeometry;
        this.utmURI = geometryWrapper.utmURI;
        this.geometryDatatypeURI = geometryWrapper.geometryDatatypeURI;

        this.crsInfo = geometryWrapper.crsInfo;
        this.dimensionInfo = geometryWrapper.dimensionInfo;
        this.lexicalForm = geometryWrapper.lexicalForm;
    }

    /**
     * Transforms, if necessary, the provided GeometryWrapper according to the
     * current GeometryWrapper SRS_URI.
     *
     * @param targetGeometryWrapper
     * @return GeometryWrapper after transformation.
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper checkTransformCRS(GeometryWrapper targetGeometryWrapper) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometryWrapper;
        String srsURI = crsInfo.getSrsURI();
        if (srsURI.equals(targetGeometryWrapper.crsInfo.getSrsURI())) {
            transformedGeometryWrapper = targetGeometryWrapper;
        } else {
            transformedGeometryWrapper = targetGeometryWrapper.transform(srsURI);
        }

        return transformedGeometryWrapper;
    }

    /**
     * Transform the GeometryWrapper into another spatial reference system.
     *
     * @param srsURI
     * @return GeometryWrapper after transformation.
     * @throws MismatchedDimensionException
     * @throws TransformException
     * @throws FactoryException
     */
    protected GeometryWrapper transform(String srsURI) throws MismatchedDimensionException, TransformException, FactoryException {
        return transform(srsURI, true);
    }

    /**
     * Transform the GeometryWrapper into another spatial reference system.<br>
     * Option to store the resulting GeometryWrapper in the index.
     *
     * @param srsURI
     * @param storeCRSTransform
     * @return GeometryWrapper after transformation.
     * @throws MismatchedDimensionException
     * @throws TransformException
     * @throws FactoryException
     */
    protected GeometryWrapper transform(String srsURI, Boolean storeCRSTransform) throws MismatchedDimensionException, TransformException, FactoryException {
        return GeometryTransformIndex.transform(this, srsURI, storeCRSTransform);
    }

    /**
     * Checks whether the prepared geometry has been initialised.
     * <br>Done lazily as expensive.
     */
    private void checkPreparedGeometry() {
        if (preparedGeometry == null) {
            this.preparedGeometry = PreparedGeometryFactory.prepare(xyGeometry);
        }
    }

    /**
     * Returns this geometry wrapper converted to the SRS_URI URI.
     *
     * @param srsURI
     * @return GeometryWrapper after conversion.
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper convertCRS(String srsURI) throws FactoryException, MismatchedDimensionException, TransformException {
        return transform(srsURI);
    }

    /**
     *
     * @return Coordinate/Spatial reference system of the GeometryWrapper.
     */
    public CoordinateReferenceSystem getCRS() {
        return crsInfo.getCrs();
    }

    /**
     *
     * @return Geometry with coordinates in x,y order, regardless of SRS_URI.
     */
    public Geometry getXYGeometry() {
        return xyGeometry;
    }

    /**
     *
     * @return Geometry with coordinates as originally provided.
     */
    public Geometry getParsingGeometry() {
        return parsingGeometry;
    }

    /**
     *
     * @return Coordinate/Spatial reference system URI.
     */
    public String getSrsURI() {
        return crsInfo.getSrsURI();
    }

    /**
     *
     * @return getSRID used in GeoSPARQL Standard page 22 to refer to srsURI.
     * i.e. getSrsURI and getSRID are the same.
     */
    public String getSRID() {
        return crsInfo.getSrsURI();
    }

    /**
     *
     * @return Whether the SRS URI has been recognised. Operations may fail or
     * not perform correctly when false.
     */
    public Boolean isSRSRecognised() {
        return crsInfo.isSRSRecognised();
    }

    /**
     *
     * @return Datatype URI of the literal.
     */
    public String getGeometryDatatypeURI() {
        return geometryDatatypeURI;
    }

    /**
     *
     * @param distance
     * @param targetDistanceUnitsURI
     * @return Buffer around GeometryWrapper according the provided distance.
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper buffer(double distance, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        //Check whether the source geometry is linear units for cartesian calculation. If not then transform to relevant UTM CRS GeometryWrapper.
        Boolean isTargetUnitsLinear = UnitsRegistry.isLinearUnits(targetDistanceUnitsURI);
        GeometryWrapper transformedGeometryWrapper;
        Boolean isTransformNeeded;

        if (crsInfo.getUnitsOfMeasure().isLinearUnits() == isTargetUnitsLinear) {
            //Source geometry and target units are both the same.
            transformedGeometryWrapper = this;
            isTransformNeeded = false;
        } else if (isTargetUnitsLinear) {
            //Source geometry is not linear but targets are so convert to linear CRS.
            String sourceUtmURI = getUTMZoneURI();
            transformedGeometryWrapper = transform(sourceUtmURI);
            isTransformNeeded = true;
        } else {
            //Source geometry is linear but targets are not so convert to nonlinear CRS.
            transformedGeometryWrapper = transform(SRS_URI.DEFAULT_WKT_CRS84);
            isTransformNeeded = true;
        }

        //Check whether the units of the distance need converting.
        double transformedDistance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, transformedGeometryWrapper.crsInfo.getUnitsOfMeasure().getUnitURI());

        //Buffer the transformed geometry
        Geometry xyGeo = transformedGeometryWrapper.xyGeometry.buffer(transformedDistance);
        DimensionInfo bufferedDimensionInfo = new DimensionInfo(dimensionInfo.getCoordinate(), dimensionInfo.getSpatial(), xyGeo.getDimension());
        Geometry parsingGeo = GeometryReverse.check(xyGeo, transformedGeometryWrapper.crsInfo);
        GeometryWrapper bufferedGeometryWrapper = new GeometryWrapper(parsingGeo, xyGeo, transformedGeometryWrapper.crsInfo.getSrsURI(), transformedGeometryWrapper.geometryDatatypeURI, bufferedDimensionInfo, null);

        //Check whether need to transform back to the original srsURI.
        if (isTransformNeeded) {
            //Don't store the buffered geometry as it is dependent upon the target distance and so likely to vary beween calls.
            return bufferedGeometryWrapper.transform(crsInfo.getSrsURI(), false);
        } else {
            return bufferedGeometryWrapper;
        }
    }

    /**
     *
     * @return URI of the GeometryWrapper's UTM zone
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public String getUTMZoneURI() throws FactoryException, MismatchedDimensionException, TransformException {

        if (utmURI == null) {

            //Find a point in the parsing geometry so can directly apply the CRS.
            Coordinate coord = parsingGeometry.getCoordinate();
            DirectPosition2D point = new DirectPosition2D(coord.x, coord.y);

            //Convert to WGS84.
            CoordinateReferenceSystem wgs84CRS = CRSRegistry.getCRS(SRS_URI.WGS84_CRS);
            MathTransform transform = MathTransformRegistry.getMathTransform(crsInfo.getCrs(), wgs84CRS);

            DirectPosition wgs84Point = transform.transform(point, null);

            //Find the UTM zone.
            utmURI = CRSRegistry.findUTMZoneURIFromWGS84(wgs84Point.getOrdinate(0), wgs84Point.getOrdinate(1));

        }
        return utmURI;
    }


    /**
     * Distance defaulting to metres.
     *
     * @param targetGeometry
     * @return Distance
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public double distance(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return distance(targetGeometry, Unit_URI.METRE_URL);
    }

    /**
     * Distance in the Units of Measure.
     *
     * @param targetGeometry
     * @param unitsOfMeasure
     * @return Distance
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public double distance(GeometryWrapper targetGeometry, UnitsOfMeasure unitsOfMeasure) throws FactoryException, MismatchedDimensionException, TransformException {
        return distance(targetGeometry, unitsOfMeasure.getUnitURI());
    }

    /**
     * Distance in the Units of Measure stated in URI.
     *
     * @param targetGeometry
     * @param targetDistanceUnitsURI
     * @return Distance
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public double distance(GeometryWrapper targetGeometry, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        Boolean isUnitsLinear = crsInfo.getUnitsOfMeasure().isLinearUnits();
        Boolean isTargetUnitsLinear = UnitsRegistry.isLinearUnits(targetDistanceUnitsURI);

        GeometryWrapper preparedSourceGeometry;
        GeometryWrapper preparedTargetGeometry;

        if (isUnitsLinear.equals(isTargetUnitsLinear)) {
            //Source geometry and target units are both the same. Convert the target geometry if required.
            preparedSourceGeometry = this;
            preparedTargetGeometry = checkTransformCRS(targetGeometry);
        } else if (isTargetUnitsLinear) {
            //Source geometry is not linear but targets are so convert to linear CRS.
            preparedSourceGeometry = transform(SRS_URI.WGS84_WORLD_MERCATOR_CRS);
            preparedTargetGeometry = targetGeometry.transform(SRS_URI.WGS84_WORLD_MERCATOR_CRS);
        } else {
            //Source geometry is linear but targets are not so convert to nonlinear CRS.
            preparedSourceGeometry = transform(SRS_URI.DEFAULT_WKT_CRS84);
            preparedTargetGeometry = targetGeometry.transform(SRS_URI.DEFAULT_WKT_CRS84);
        }

        double distance = preparedSourceGeometry.xyGeometry.distance(preparedTargetGeometry.xyGeometry);
        String unitsURI = preparedSourceGeometry.crsInfo.getUnitsOfMeasure().getUnitURI();
        double targetDistance = UnitsOfMeasure.conversion(distance, unitsURI, targetDistanceUnitsURI);

        return targetDistance;
    }

    /**
     *
     * @return Boundary of GeometryWrapper
     */
    public GeometryWrapper boundary() {
        Geometry xyGeo = this.xyGeometry.getBoundary();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crsInfo);
        return new GeometryWrapper(parsingGeo, xyGeo, crsInfo.getSrsURI(), geometryDatatypeURI, dimensionInfo, null);
    }

    /**
     *
     * @return Convex Hull of GeometryWrapper
     */
    public GeometryWrapper convexHull() {
        Geometry xyGeo = this.xyGeometry.convexHull();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crsInfo);
        return new GeometryWrapper(parsingGeo, xyGeo, crsInfo.getSrsURI(), geometryDatatypeURI, dimensionInfo, null);
    }

    /**
     *
     * @param targetGeometry
     * @return Difference of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.difference(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crsInfo);
        return new GeometryWrapper(parsingGeo, xyGeo, crsInfo.getSrsURI(), geometryDatatypeURI, dimensionInfo, null);
    }

    /**
     *
     * @return Envelope of GeometryWrapper
     */
    public GeometryWrapper envelope() {
        Geometry xyGeo = this.xyGeometry.getEnvelope();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crsInfo);
        return new GeometryWrapper(parsingGeo, xyGeo, crsInfo.getSrsURI(), geometryDatatypeURI, dimensionInfo, null);
    }

    /**
     *
     * @return Envelope of GeometryWrapper
     */
    public Envelope getEnvelope() {
        return this.xyGeometry.getEnvelopeInternal();
    }

    /**
     *
     * @param targetGeometry
     * @return Intersection of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.intersection(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crsInfo);
        return new GeometryWrapper(parsingGeo, xyGeo, crsInfo.getSrsURI(), geometryDatatypeURI, dimensionInfo, null);
    }

    /**
     *
     * @param targetGeometry
     * @return Intersection Matrix of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public IntersectionMatrix relate(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return xyGeometry.relate(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @param intersectionPattern
     * @return Relation of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean relate(GeometryWrapper targetGeometry, String intersectionPattern) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return xyGeometry.relate(transformedGeometry.xyGeometry, intersectionPattern);
    }

    /**
     *
     * @param targetGeometry
     * @return Symmetric Difference of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public GeometryWrapper symDifference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.symDifference(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crsInfo);
        return new GeometryWrapper(parsingGeo, xyGeo, crsInfo.getSrsURI(), geometryDatatypeURI, dimensionInfo, null);
    }

    /**
     *
     * @param targetGeometry
     * @return Union of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.union(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crsInfo);
        return new GeometryWrapper(parsingGeo, xyGeo, crsInfo.getSrsURI(), geometryDatatypeURI, dimensionInfo, null);
    }

    /**
     *
     * @param targetGeometry
     * @return sfContains of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean contains(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.contains(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @return sfCrosses of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean crosses(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.crosses(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @return sfDisjoint of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean disjoint(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.disjoint(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @return sfEquals of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean equals(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.equalsTopo(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @return Equals exactly of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean equalsExact(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.equalsExact(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @param tolerance
     * @return Equals exactly of GeometryWrapper with target using provided
     * tolerance.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean equalsExact(GeometryWrapper targetGeometry, double tolerance) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.equalsExact(transformedGeometry.xyGeometry, tolerance);
    }

    /**
     *
     * @param targetGeometry
     * @return sfIntersects of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean intersects(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.intersects(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @return sfOverlaps of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean overlaps(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.overlaps(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @return sfTouches of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean touches(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.touches(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @param targetGeometry
     * @return sfWithin of GeometryWrapper with target.
     * @throws org.opengis.util.FactoryException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public boolean within(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.within(transformedGeometry.xyGeometry);
    }

    /**
     *
     * @return GeometryWrapper as NodeValue
     */
    public NodeValue asNode() throws DatatypeFormatException {
        Literal literal = asLiteral();
        return NodeValue.makeNode(literal.getLexicalForm(), literal.getDatatype());
    }

    /**
     *
     * @return GeometryWrapper as Literal
     */
    public Literal asLiteral() throws DatatypeFormatException {

        GeometryDatatype datatype = GeometryDatatype.get(geometryDatatypeURI);
        if (lexicalForm != null) {
            return ResourceFactory.createTypedLiteral(lexicalForm, datatype);
        }

        Literal literal = asLiteral(datatype);
        lexicalForm = literal.getLexicalForm();
        return literal;
    }

    /**
     *
     * @param outputGeometryDatatypeURI
     * @return GeometryWrapper as Literal in datatype form.
     */
    public Literal asLiteral(String outputGeometryDatatypeURI) throws DatatypeFormatException {
        GeometryDatatype datatype = GeometryDatatype.get(outputGeometryDatatypeURI);
        return asLiteral(datatype);
    }

    /**
     *
     * @param datatype
     * @return GeometryWrapper as Literal
     */
    public Literal asLiteral(GeometryDatatype datatype) {
        String tempLexicalForm = datatype.unparse(this);
        return ResourceFactory.createTypedLiteral(tempLexicalForm, datatype);
    }

    /**
     *
     * @return Coordinate dimension, i.e. 2 (x,y), 3 (x,y,z or x,y,m) or 4
     * (x,y,z,m)
     */
    public int getCoordinateDimension() {
        return dimensionInfo.getCoordinate();
    }

    /**
     *
     * @return Spatial dimension, i.e. 2 or 3
     */
    public int getSpatialDimension() {
        return dimensionInfo.getSpatial();
    }

    /**
     *
     * @return Topological dimension, i.e. 0, 1 or 2
     */
    public int getTopologicalDimension() {
        return dimensionInfo.getTopological();
    }

    /**
     *
     * @return Enum of coordinate dimensions.
     */
    public CoordinateSequenceDimensions getCoordinateSequenceDimensions() {
        return dimensionInfo.getDimensions();
    }

    /**
     *
     * @return Units of Measure for the GeometryWrapper SRS.
     */
    public UnitsOfMeasure getUnitsOfMeasure() {
        return crsInfo.getUnitsOfMeasure();
    }

    /**
     *
     * @return GeometryWrapper's coordinate, spatial and topological dimensions.
     */
    public DimensionInfo getDimensionInfo() {
        return dimensionInfo;
    }

    /**
     *
     * @return String literal of Geometry Wrapper.
     */
    public String getLexicalForm() {

        if (lexicalForm != null) {
            return lexicalForm;
        } else {
            Literal literal = asLiteral();
            return literal.getLexicalForm();
        }
    }

    /**
     *
     * @return GeometryWrapper is empty of coordinates.
     */
    public boolean isEmpty() {
        return this.xyGeometry.isEmpty();
    }

    /**
     *
     * @return GeometryWrapper is in simple form.
     */
    public boolean isSimple() {
        return this.xyGeometry.isSimple();
    }

    /**
     * Extract Geometry Wrapper from Geometry Literal.<br>
     * Returns null if invalid node value provided.
     *
     * @param nodeValue
     * @param targetIndex
     * @return Geometry Wrapper of the Geometry Literal.
     */
    public static final GeometryWrapper extract(NodeValue nodeValue, GeometryIndex targetIndex) {

        if (!nodeValue.isLiteral()) {
            return null;
        }
        Node node = nodeValue.asNode();

        return extract(node, targetIndex);
    }

    /**
     * Extract Geometry Wrapper from Geometry Literal.<br>
     * Returns null if invalid node value provided.
     *
     * @param node
     * @param targetIndex
     * @return Geometry Wrapper of the Geometry Literal.
     */
    public static final GeometryWrapper extract(Node node, GeometryIndex targetIndex) {

        String datatypeURI = node.getLiteralDatatypeURI();
        String lexicalForm = node.getLiteralLexicalForm();
        return extract(lexicalForm, datatypeURI, targetIndex);
    }

    /**
     * Extract Geometry Wrapper from Geometry Literal.<br>
     * Returns null if invalid node value provided.
     *
     * @param nodeValue
     * @return Geometry Wrapper of the Geometry Literal.
     */
    public static final GeometryWrapper extract(NodeValue nodeValue) {
        return extract(nodeValue, GeometryIndex.PRIMARY);
    }

    /**
     * Extract Geometry Wrapper from Geometry Literal.<br>
     * Returns null if invalid node value provided.
     *
     * @param node
     * @return Geometry Wrapper of the Geometry Literal.
     */
    public static final GeometryWrapper extract(Node node) {
        return extract(node, GeometryIndex.PRIMARY);
    }

    /**
     * Extract Geometry Wrapper from Geometry Literal.<br>
     * Returns null if invalid literal provided.
     *
     * @param geometryLiteral
     * @param targetIndex
     * @return Geometry Wrapper of the Geometry Literal.
     */
    public static final GeometryWrapper extract(Literal geometryLiteral, GeometryIndex targetIndex) {
        return extract(geometryLiteral.getLexicalForm(), geometryLiteral.getDatatypeURI(), targetIndex);
    }

    /**
     * Returns null if invalid literal provided.
     *
     * @param geometryLiteral
     * @return Geometry Wrapper of the Geometry Literal.
     */
    public static final GeometryWrapper extract(Literal geometryLiteral) {
        return extract(geometryLiteral, GeometryIndex.PRIMARY);
    }

    /**
     * Extract Geometry Wrapper from Geometry Literal.<br>
     * Returns null if invalid literal provided.
     *
     * @param lexicalForm
     * @param datatypeURI
     * @return Geometry Wrapper of the Geometry Literal.
     */
    public static GeometryWrapper extract(String lexicalForm, String datatypeURI) {
        return extract(lexicalForm, datatypeURI, GeometryIndex.PRIMARY);
    }

    /**
     * Extract Geometry Wrapper from Geometry Literal.<br>
     * Returns null if invalid literal provided.
     *
     * @param lexicalForm
     * @param datatypeURI
     * @param targetIndex
     * @return Geometry Wrapper of the Geometry Literal.
     */
    public static GeometryWrapper extract(String lexicalForm, String datatypeURI, GeometryIndex targetIndex) {

        GeometryDatatype datatype = GeometryDatatype.get(datatypeURI);
        GeometryWrapper geometry = datatype.parse(lexicalForm, targetIndex);
        return geometry;
    }

    /**
     *
     * @return Empty GeometryWrapper in WKT datatype.
     */
    public static final GeometryWrapper getEmptyWKT() {
        return WKTDatatype.INSTANCE.read("");
    }

    /**
     *
     * @return Empty GeometryWrapper in GML datatype.
     */
    public static final GeometryWrapper getEmptyGML() {
        return GMLDatatype.INSTANCE.read("");
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.dimensionInfo);
        hash = 23 * hash + Objects.hashCode(this.crsInfo);
        hash = 23 * hash + Objects.hashCode(this.xyGeometry);
        hash = 23 * hash + Objects.hashCode(this.geometryDatatypeURI);
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
        final GeometryWrapper other = (GeometryWrapper) obj;
        if (!Objects.equals(this.geometryDatatypeURI, other.geometryDatatypeURI)) {
            return false;
        }
        if (!Objects.equals(this.dimensionInfo, other.dimensionInfo)) {
            return false;
        }
        if (!Objects.equals(this.crsInfo, other.crsInfo)) {
            return false;
        }
        return Objects.equals(this.xyGeometry, other.xyGeometry);
    }

    @Override
    public String toString() {
        return "GeometryWrapper{" + "dimensionInfo=" + dimensionInfo + ", crsInfo=" + crsInfo + ", xyGeometry=" + xyGeometry + ", parsingGeometry=" + parsingGeometry + ", preparedGeometry=" + preparedGeometry + ", geometryDatatypeURI=" + geometryDatatypeURI + ", lexicalForm=" + lexicalForm + ", utmURI=" + utmURI + '}';
    }

}
