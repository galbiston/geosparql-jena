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
package implementation;

import implementation.datatype.GMLDatatype;
import implementation.datatype.GeometryDatatype;
import implementation.datatype.WKTDatatype;
import implementation.index.GeometryLiteralIndex.GeometryIndex;
import implementation.index.GeometryTransformIndex;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import implementation.jts.CustomGeometryFactory;
import implementation.registry.CRSRegistry;
import implementation.registry.MathTransformRegistry;
import implementation.registry.UnitsRegistry;
import implementation.vocabulary.SRS_URI;
import implementation.vocabulary.Unit_URI;
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
    private final String srsURI;
    private final Boolean isAxisXY;
    private final UnitsOfMeasure unitsOfMeasure;
    private final CoordinateReferenceSystem crs;
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
        this.srsURI = srsURI;
        this.crs = CRSRegistry.getCRS(srsURI);
        this.isAxisXY = CRSRegistry.getAxisXY(srsURI);
        this.unitsOfMeasure = CRSRegistry.getUnitsOfMeasure(srsURI);

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
        this(geometry, "", geometryDatatypeURI, DimensionInfo.xyPoint());
    }

    /**
     * Default to XY coordinate dimensions.
     *
     * @param geometry
     * @param srsURI
     * @param geometryDatatypeURI
     */
    public GeometryWrapper(Geometry geometry, String srsURI, String geometryDatatypeURI) {
        this(geometry, srsURI, geometryDatatypeURI, DimensionInfo.xyPoint());
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
        this(new CustomCoordinateSequence(DimensionInfo.xyPoint().getDimensions()), geometryDatatypeURI, srsURI);
    }

    /**
     * Point geometry with specified SRS.
     *
     * @param pointCoordinateSequence
     * @param geometryDatatypeURI
     * @param srsURI
     */
    public GeometryWrapper(CustomCoordinateSequence pointCoordinateSequence, String geometryDatatypeURI, String srsURI) {
        this(GEOMETRY_FACTORY.createPoint(pointCoordinateSequence), srsURI, geometryDatatypeURI, DimensionInfo.xyPoint());
    }

    public GeometryWrapper(GeometryWrapper geometryWrapper) {

        this.xyGeometry = geometryWrapper.xyGeometry;
        this.parsingGeometry = geometryWrapper.parsingGeometry;
        this.preparedGeometry = geometryWrapper.preparedGeometry;
        this.srsURI = geometryWrapper.srsURI;
        this.utmURI = geometryWrapper.utmURI;
        this.geometryDatatypeURI = geometryWrapper.geometryDatatypeURI;

        this.crs = geometryWrapper.crs;
        this.isAxisXY = geometryWrapper.isAxisXY;
        this.unitsOfMeasure = geometryWrapper.unitsOfMeasure;
        this.dimensionInfo = geometryWrapper.dimensionInfo;
        this.lexicalForm = geometryWrapper.lexicalForm;
    }

    /**
     * Transforms, if necessary, the provided GeometryWrapper according to the
     * current GeometryWrapper SRS_URI.
     *
     * @param targetGeometryWrapper
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper checkTransformCRS(GeometryWrapper targetGeometryWrapper) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometryWrapper;

        if (srsURI.equals(targetGeometryWrapper.srsURI)) {
            transformedGeometryWrapper = targetGeometryWrapper;
        } else {
            transformedGeometryWrapper = targetGeometryWrapper.transform(srsURI);
        }

        return transformedGeometryWrapper;
    }

    public GeometryWrapper transform(String srsURI) throws MismatchedDimensionException, TransformException, FactoryException {
        return transform(srsURI, true);
    }

    public GeometryWrapper transform(String srsURI, Boolean storeCRSTransform) throws MismatchedDimensionException, TransformException, FactoryException {
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
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper convertCRS(String srsURI) throws FactoryException, MismatchedDimensionException, TransformException {
        return transform(srsURI);
    }

    public CoordinateReferenceSystem getCRS() {
        return crs;
    }

    /**
     * Geometry with coordinates in x,y order, regardless of SRS_URI.
     *
     * @return
     */
    public Geometry getXYGeometry() {
        return xyGeometry;
    }

    /**
     * Geometry with coordinates as originally provided.
     *
     * @return
     */
    public Geometry getParsingGeometry() {
        return parsingGeometry;
    }

    public String getSrsURI() {
        return srsURI;
    }

    /**
     * getSRID used in GeoSPARQL Standard page 22 to refer to srsURI. i.e.
     * getSrsURI and getSRID are the same.
     *
     * @return
     */
    public String getSRID() {
        return srsURI;
    }

    public String getGeometryDatatypeURI() {
        return geometryDatatypeURI;
    }

    public GeometryWrapper buffer(double distance, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        //Check whether the source geometry is linear units for cartesian calculation. If not then transform to relevant UTM CRS GeometryWrapper.
        Boolean isTargetUnitsLinear = UnitsRegistry.isLinearUnits(targetDistanceUnitsURI);
        GeometryWrapper transformedGeometryWrapper;
        Boolean isTransformNeeded;

        if (unitsOfMeasure.isLinearUnits() == isTargetUnitsLinear) {
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
        double transformedDistance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, transformedGeometryWrapper.unitsOfMeasure.getUnitURI());

        //Buffer the transformed geometry
        Geometry xyGeo = transformedGeometryWrapper.xyGeometry.buffer(transformedDistance);
        DimensionInfo bufferedDimensionInfo = new DimensionInfo(dimensionInfo.getCoordinate(), dimensionInfo.getSpatial(), xyGeo.getDimension());
        Geometry parsingGeo = GeometryReverse.check(xyGeo, transformedGeometryWrapper.isAxisXY);
        GeometryWrapper bufferedGeometryWrapper = new GeometryWrapper(parsingGeo, xyGeo, transformedGeometryWrapper.srsURI, transformedGeometryWrapper.geometryDatatypeURI, bufferedDimensionInfo, null);

        //Check whether need to transform back to the original srsURI.
        if (isTransformNeeded) {
            //Don't store the buffered geometry as it is dependent upon the target distance and so likely to vary beween calls.
            return bufferedGeometryWrapper.transform(srsURI, false);
        } else {
            return bufferedGeometryWrapper;
        }
    }

    public String getUTMZoneURI() throws FactoryException, MismatchedDimensionException, TransformException {

        if (utmURI == null) {

            //Find a point in the parsing geometry so can directly apply the CRS.
            Coordinate coord = parsingGeometry.getCoordinate();
            DirectPosition2D point = new DirectPosition2D(coord.x, coord.y);

            //Convert to WGS84.
            CoordinateReferenceSystem wgs84CRS = CRSRegistry.getCRS(SRS_URI.WGS84_CRS);
            MathTransform transform = MathTransformRegistry.getMathTransform(crs, wgs84CRS);

            DirectPosition wgs84Point = transform.transform(point, null);

            //Find the UTM zone.
            utmURI = CRSRegistry.findUTMZoneURIFromWGS84(wgs84Point.getOrdinate(0), wgs84Point.getOrdinate(1));

        }
        return utmURI;
    }

    public GeometryWrapper convexHull() {
        Geometry xyGeo = this.xyGeometry.convexHull();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, isAxisXY);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, geometryDatatypeURI, dimensionInfo, null);
    }

    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.difference(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, isAxisXY);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, geometryDatatypeURI, dimensionInfo, null);
    }

    /**
     * Distance defaulting to metres.
     *
     * @param targetGeometry
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public double distance(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return distance(targetGeometry, Unit_URI.METRE_URL);
    }

    public double distance(GeometryWrapper targetGeometry, UnitsOfMeasure unitsOfMeasure) throws FactoryException, MismatchedDimensionException, TransformException {
        return distance(targetGeometry, unitsOfMeasure.getUnitURI());
    }

    public double distance(GeometryWrapper targetGeometry, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        Boolean isUnitsLinear = unitsOfMeasure.isLinearUnits();
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
        String unitsURI = preparedSourceGeometry.unitsOfMeasure.getUnitURI();
        double targetDistance = UnitsOfMeasure.conversion(distance, unitsURI, targetDistanceUnitsURI);

        return targetDistance;
    }

    public GeometryWrapper boundary() {
        Geometry xyGeo = this.xyGeometry.getBoundary();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, isAxisXY);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, geometryDatatypeURI, dimensionInfo, null);
    }

    public GeometryWrapper envelope() {
        Geometry xyGeo = this.xyGeometry.getEnvelope();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, isAxisXY);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, geometryDatatypeURI, dimensionInfo, null);
    }

    public Envelope getEnvelope() {
        return this.xyGeometry.getEnvelopeInternal();
    }

    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.intersection(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, isAxisXY);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, geometryDatatypeURI, dimensionInfo, null);
    }

    public IntersectionMatrix relate(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return xyGeometry.relate(transformedGeometry.xyGeometry);
    }

    public boolean relate(GeometryWrapper targetGeometry, String intersectionPattern) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return xyGeometry.relate(transformedGeometry.xyGeometry, intersectionPattern);
    }

    public GeometryWrapper symDifference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.symDifference(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, isAxisXY);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, geometryDatatypeURI, dimensionInfo, null);
    }

    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.union(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, isAxisXY);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, geometryDatatypeURI, dimensionInfo, null);
    }

    public boolean contains(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.contains(transformedGeometry.xyGeometry);
    }

    public boolean crosses(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.crosses(transformedGeometry.xyGeometry);
    }

    public boolean disjoint(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.disjoint(transformedGeometry.xyGeometry);
    }

    public boolean equals(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.equalsTopo(transformedGeometry.xyGeometry);
    }

    public boolean equals(GeometryWrapper targetGeometry, double tolerance) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.xyGeometry.equalsExact(transformedGeometry.xyGeometry, tolerance);
    }

    public boolean intersects(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.intersects(transformedGeometry.xyGeometry);
    }

    public boolean overlaps(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.overlaps(transformedGeometry.xyGeometry);
    }

    public boolean touches(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.touches(transformedGeometry.xyGeometry);
    }

    public boolean within(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        this.checkPreparedGeometry();
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        return this.preparedGeometry.within(transformedGeometry.xyGeometry);
    }

    public NodeValue asNode() throws DatatypeFormatException {
        Literal literal = asLiteral();
        return NodeValue.makeNode(literal.getLexicalForm(), literal.getDatatype());
    }

    public Literal asLiteral() throws DatatypeFormatException {

        GeometryDatatype datatype = GeometryDatatype.get(geometryDatatypeURI);
        if (lexicalForm != null) {
            return ResourceFactory.createTypedLiteral(lexicalForm, datatype);
        }

        Literal literal = asLiteral(datatype);
        lexicalForm = literal.getLexicalForm();
        return literal;
    }

    public Literal asLiteral(String outputGeometryDatatypeURI) throws DatatypeFormatException {
        GeometryDatatype datatype = GeometryDatatype.get(outputGeometryDatatypeURI);
        return asLiteral(datatype);
    }

    public Literal asLiteral(GeometryDatatype datatype) {
        String tempLexicalForm = datatype.unparse(this);
        return ResourceFactory.createTypedLiteral(tempLexicalForm, datatype);
    }

    public int getCoordinateDimension() {
        return dimensionInfo.getCoordinate();
    }

    public int getSpatialDimension() {
        return dimensionInfo.getSpatial();
    }

    public int getTopologicalDimension() {
        return dimensionInfo.getTopological();
    }

    public CoordinateSequenceDimensions getCoordinateSequenceDimensions() {
        return dimensionInfo.getDimensions();
    }

    public UnitsOfMeasure getUnitsOfMeasure() {
        return unitsOfMeasure;
    }

    public DimensionInfo getDimensionInfo() {
        return dimensionInfo;
    }

    public String getLexicalForm() {

        if (lexicalForm != null) {
            return lexicalForm;
        } else {
            Literal literal = asLiteral();
            return literal.getLexicalForm();
        }
    }

    public boolean isEmpty() {
        return this.xyGeometry.isEmpty();
    }

    public boolean isSimple() {
        return this.xyGeometry.isSimple();
    }

    /**
     * Returns null if invalid node value provided.
     *
     * @param nodeValue
     * @param targetIndex
     * @return
     */
    public static final GeometryWrapper extract(NodeValue nodeValue, GeometryIndex targetIndex) {

        if (!nodeValue.isLiteral()) {
            return null;
        }

        Node node = nodeValue.asNode();
        String datatypeURI = node.getLiteralDatatypeURI();
        String lexicalForm = node.getLiteralLexicalForm();
        return extract(lexicalForm, datatypeURI, targetIndex);
    }

    public static final GeometryWrapper extract(NodeValue nodeValue) {
        return extract(nodeValue, GeometryIndex.PRIMARY);
    }

    /**
     * Returns null if invalid literal provided.
     *
     * @param geometryLiteral
     * @param targetIndex
     * @return
     */
    public static final GeometryWrapper extract(Literal geometryLiteral, GeometryIndex targetIndex) {
        return extract(geometryLiteral.getLexicalForm(), geometryLiteral.getDatatypeURI(), targetIndex);
    }

    public static final GeometryWrapper extract(Literal geometryLiteral) {
        return extract(geometryLiteral, GeometryIndex.PRIMARY);
    }

    public static GeometryWrapper extract(String lexicalForm, String datatypeURI) {
        return extract(lexicalForm, datatypeURI, GeometryIndex.PRIMARY);
    }

    public static GeometryWrapper extract(String lexicalForm, String datatypeURI, GeometryIndex targetIndex) {

        GeometryDatatype datatype = GeometryDatatype.get(datatypeURI);
        GeometryWrapper geometry = datatype.parse(lexicalForm, targetIndex);
        return geometry;
    }

    public static final GeometryWrapper getEmptyWKT() {
        return WKTDatatype.INSTANCE.read("");
    }

    public static final GeometryWrapper getEmptyGML() {
        return GMLDatatype.INSTANCE.read("");
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.dimensionInfo);
        hash = 47 * hash + Objects.hashCode(this.srsURI);
        hash = 47 * hash + Objects.hashCode(this.isAxisXY);
        hash = 47 * hash + Objects.hashCode(this.unitsOfMeasure);
        hash = 47 * hash + Objects.hashCode(this.crs);
        hash = 47 * hash + Objects.hashCode(this.xyGeometry);
        hash = 47 * hash + Objects.hashCode(this.geometryDatatypeURI);
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
        if (!Objects.equals(this.srsURI, other.srsURI)) {
            return false;
        }
        if (!Objects.equals(this.geometryDatatypeURI, other.geometryDatatypeURI)) {
            return false;
        }
        if (!Objects.equals(this.dimensionInfo, other.dimensionInfo)) {
            return false;
        }
        if (!Objects.equals(this.isAxisXY, other.isAxisXY)) {
            return false;
        }
        if (!Objects.equals(this.unitsOfMeasure, other.unitsOfMeasure)) {
            return false;
        }
        if (!Objects.equals(this.crs, other.crs)) {
            return false;
        }
        return Objects.equals(this.xyGeometry, other.xyGeometry);
    }

    @Override
    public String toString() {
        return "GeometryWrapper{" + "lexicalForm=" + lexicalForm + ", dimensionInfo=" + dimensionInfo + ", srsURI=" + srsURI + ", isAxisXY=" + isAxisXY + ", utmURI=" + utmURI + ", geometryDatatypeURI=" + geometryDatatypeURI + ", unitsOfMeasure=" + unitsOfMeasure + ", crs=" + crs + ", xyGeometry=" + xyGeometry + ", parsingGeometry=" + parsingGeometry + '}';
    }
}
