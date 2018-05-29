/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.prep.PreparedGeometry;
import com.vividsolutions.jts.geom.prep.PreparedGeometryFactory;
import implementation.datatype.DatatypeUtil;
import implementation.datatype.GMLDatatype;
import implementation.datatype.GeoDatatypeEnum;
import implementation.datatype.GeometryDatatype;
import implementation.datatype.WKTDatatype;
import implementation.index.IndexConfiguration;
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
import org.apache.commons.collections4.map.LRUMap;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 *
 *
 */
public class GeometryWrapper implements Serializable {

    private final Geometry xyGeometry;
    private final Geometry parsingGeometry;
    private PreparedGeometry preparedGeometry;
    private final String srsURI;
    private final GeoDatatypeEnum datatypeEnum;
    private final CoordinateReferenceSystem crs;
    private final UnitsOfMeasure unitsOfMeasure;
    private final DimensionInfo dimensionInfo;
    private final LRUMap<String, GeometryWrapper> crsTransfomations;
    private static Integer CRS_TRANSFORMATIONS_MAX_SIZE = IndexConfiguration.GEOMETRY_WRAPPER_CRS_TRANSFORMATIONS_MAX_SIZE_DEFAULT;
    private static Boolean IS_CRS_TRANSFORMATION_ACTIVE = true;

    public GeometryWrapper(Geometry geometry, String srsURI, GeoDatatypeEnum datatypeEnum, DimensionInfo dimensionInfo) {
        this(geometry, GeometryReverse.check(geometry, CRSRegistry.getCRS(srsURI.isEmpty() ? SRS_URI.DEFAULT_WKT_CRS84 : srsURI)), srsURI.isEmpty() ? SRS_URI.DEFAULT_WKT_CRS84 : srsURI, datatypeEnum, dimensionInfo);
    }

    private GeometryWrapper(Geometry parsingGeometry, Geometry xyGeometry, String srsURI, GeoDatatypeEnum datatypeEnum, DimensionInfo dimensionInfo) {

        this.parsingGeometry = parsingGeometry;
        this.xyGeometry = xyGeometry;
        this.preparedGeometry = null; //Initialised when required by spatial relations checkPreparedGeometry.

        this.datatypeEnum = datatypeEnum;

        if (srsURI.isEmpty()) {
            srsURI = SRS_URI.DEFAULT_WKT_CRS84;
        }
        this.srsURI = srsURI;
        this.crs = CRSRegistry.getCRS(srsURI);
        this.unitsOfMeasure = CRSRegistry.getUnitsOfMeasure(srsURI);

        this.dimensionInfo = dimensionInfo;
        this.crsTransfomations = new LRUMap<>(CRS_TRANSFORMATIONS_MAX_SIZE);
    }

    /**
     * Default to WGS84 and XY coordinate dimensions.
     *
     * @param geometry
     * @param datatypeEnum
     */
    public GeometryWrapper(Geometry geometry, GeoDatatypeEnum datatypeEnum) {
        this(geometry, "", datatypeEnum, DimensionInfo.xyPoint());
    }

    /**
     * Default to XY coordinate dimensions.
     *
     * @param geometry
     * @param srsURI
     * @param datatypeEnum
     */
    public GeometryWrapper(Geometry geometry, String srsURI, GeoDatatypeEnum datatypeEnum) {
        this(geometry, srsURI, datatypeEnum, DimensionInfo.xyPoint());
    }

    transient private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    /**
     * Empty geometry with specified parameters.
     *
     * @param srsURI
     * @param datatypeEnum
     * @param dimensionInfo
     */
    public GeometryWrapper(String srsURI, GeoDatatypeEnum datatypeEnum, DimensionInfo dimensionInfo) {
        this(new CustomCoordinateSequence(DimensionInfo.xyPoint().getDimensions()), datatypeEnum, srsURI);
    }

    /**
     * Point geometry with specified SRS.
     *
     * @param pointCoordinateSequence
     * @param datatypeEnum
     * @param srsURI
     */
    public GeometryWrapper(CustomCoordinateSequence pointCoordinateSequence, GeoDatatypeEnum datatypeEnum, String srsURI) {
        this(GEOMETRY_FACTORY.createPoint(pointCoordinateSequence), srsURI, datatypeEnum, DimensionInfo.xyPoint());
    }

    public GeometryWrapper(GeometryWrapper geometryWrapper) {

        this.xyGeometry = geometryWrapper.xyGeometry;
        this.parsingGeometry = geometryWrapper.parsingGeometry;
        this.preparedGeometry = geometryWrapper.preparedGeometry;
        this.srsURI = geometryWrapper.srsURI;
        this.datatypeEnum = geometryWrapper.datatypeEnum;

        this.crs = geometryWrapper.crs;
        this.unitsOfMeasure = geometryWrapper.unitsOfMeasure;
        this.dimensionInfo = geometryWrapper.dimensionInfo;
        this.crsTransfomations = new LRUMap<>(CRS_TRANSFORMATIONS_MAX_SIZE);
        this.crsTransfomations.putAll(geometryWrapper.crsTransfomations);
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
            transformedGeometryWrapper = targetGeometryWrapper.transform(srsURI, true);
        }

        return transformedGeometryWrapper;
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
        return transform(srsURI, true);
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

    public GeoDatatypeEnum getGeoDatatypeEnum() {
        return datatypeEnum;
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
            String utmURI = findUTMZoneURI();
            transformedGeometryWrapper = transform(utmURI, true);
            isTransformNeeded = true;
        } else {
            //Source geometry is linear but targets are not so convert to nonlinear CRS.
            transformedGeometryWrapper = transform(SRS_URI.DEFAULT_WKT_CRS84, true);
            isTransformNeeded = true;
        }

        //Check whether the units of the distance need converting.
        double transformedDistance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, transformedGeometryWrapper.unitsOfMeasure.getUnitURI());

        //Buffer the transformed geometry
        Geometry xyGeo = transformedGeometryWrapper.xyGeometry.buffer(transformedDistance);
        DimensionInfo bufferedDimensionInfo = new DimensionInfo(dimensionInfo.getCoordinate(), dimensionInfo.getSpatial(), xyGeo.getDimension());
        Geometry parsingGeo = GeometryReverse.check(xyGeo, transformedGeometryWrapper.crs);
        GeometryWrapper bufferedGeometryWrapper = new GeometryWrapper(parsingGeo, xyGeo, transformedGeometryWrapper.srsURI, transformedGeometryWrapper.datatypeEnum, bufferedDimensionInfo);

        //Check whether need to transform back to the original srsURI.
        if (isTransformNeeded) {
            return bufferedGeometryWrapper.transform(srsURI, false);
        } else {
            return bufferedGeometryWrapper;
        }
    }

    private String findUTMZoneURI() throws FactoryException, MismatchedDimensionException, TransformException {

        //Find a point in the parsing geometry.
        Coordinate coord = parsingGeometry.getCoordinate();
        Point point = GEOMETRY_FACTORY.createPoint(coord);
        //Convert to WGS84.
        CoordinateReferenceSystem wgs84CRS = CRSRegistry.getCRS(SRS_URI.WGS84_CRS);
        MathTransform transform = MathTransformRegistry.getMathTransform(crs, wgs84CRS);

        Point wgs84Point = (Point) JTS.transform(point, transform);

        //Find the UTM zone.
        return CRSRegistry.findUTMZoneURIFromWGS84(wgs84Point.getX(), wgs84Point.getY());
    }

    public GeometryWrapper convexHull() {
        Geometry xyGeo = this.xyGeometry.convexHull();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crs);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, datatypeEnum, dimensionInfo);
    }

    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.difference(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crs);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, datatypeEnum, dimensionInfo);
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
        return distance(targetGeometry, Unit_URI.METRE_URI);
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
            preparedSourceGeometry = transform(SRS_URI.GEOTOOLS_GEOCENTRIC_CARTESIAN, true);
            preparedTargetGeometry = targetGeometry.transform(SRS_URI.GEOTOOLS_GEOCENTRIC_CARTESIAN, true);
        } else {
            //Source geometry is linear but targets are not so convert to nonlinear CRS.
            preparedSourceGeometry = transform(SRS_URI.DEFAULT_WKT_CRS84, true);
            preparedTargetGeometry = targetGeometry.transform(SRS_URI.DEFAULT_WKT_CRS84, true);
        }

        double distance = preparedSourceGeometry.xyGeometry.distance(preparedTargetGeometry.xyGeometry);
        String unitsURI = preparedSourceGeometry.unitsOfMeasure.getUnitURI();
        double targetDistance = UnitsOfMeasure.conversion(distance, unitsURI, targetDistanceUnitsURI);

        return targetDistance;
    }

    public GeometryWrapper boundary() {
        Geometry xyGeo = this.xyGeometry.getBoundary();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crs);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, datatypeEnum, dimensionInfo);
    }

    public GeometryWrapper envelope() {
        Geometry xyGeo = this.xyGeometry.getEnvelope();
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crs);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, datatypeEnum, dimensionInfo);
    }

    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.intersection(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crs);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, datatypeEnum, dimensionInfo);
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
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crs);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, datatypeEnum, dimensionInfo);
    }

    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkTransformCRS(targetGeometry);
        Geometry xyGeo = this.xyGeometry.union(transformedGeometry.xyGeometry);
        Geometry parsingGeo = GeometryReverse.check(xyGeo, crs);
        return new GeometryWrapper(parsingGeo, xyGeo, srsURI, datatypeEnum, dimensionInfo);
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
        return asLiteral(datatypeEnum);
    }

    public Literal asLiteral(GeoDatatypeEnum outputDatatypeEnum) throws DatatypeFormatException {
        GeometryDatatype datatype = DatatypeUtil.getDatatype(outputDatatypeEnum);
        return asLiteral(datatype);
    }

    public Literal asLiteral(GeometryDatatype datatype) {
        String lexicalForm = datatype.unparse(this);
        return ResourceFactory.createTypedLiteral(lexicalForm, datatype);
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

    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    public UnitsOfMeasure getUnitsOfMeasure() {
        return unitsOfMeasure;
    }

    public DimensionInfo getDimensionInfo() {
        return dimensionInfo;
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
     * @return
     */
    public static final GeometryWrapper extract(NodeValue nodeValue) {

        if (!nodeValue.isLiteral()) {
            return null;
        }

        Node node = nodeValue.asNode();
        String datatypeURI = node.getLiteralDatatypeURI();
        String lexicalForm = node.getLiteralLexicalForm();
        return extract(lexicalForm, datatypeURI);
    }

    /**
     * Returns null if invalid literal provided.
     *
     * @param geometryLiteral
     * @return
     */
    public static final GeometryWrapper extract(Literal geometryLiteral) {
        return extract(geometryLiteral.getLexicalForm(), geometryLiteral.getDatatypeURI());
    }

    private static GeometryWrapper extract(String lexicalForm, String datatypeURI) {

        GeometryDatatype datatype = DatatypeUtil.getDatatype(datatypeURI);
        GeometryWrapper geometry = datatype.parse(lexicalForm);
        return geometry;
    }

    public GeometryWrapper transform(String srsURI, boolean storeCRSTransform) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometryWrapper;

        if (crsTransfomations.containsKey(srsURI)) {
            transformedGeometryWrapper = crsTransfomations.get(srsURI);
        } else {
            CoordinateReferenceSystem targetCRS = CRSRegistry.getCRS(srsURI);
            MathTransform transform = MathTransformRegistry.getMathTransform(crs, targetCRS);
            Geometry transformedGeometry = JTS.transform(parsingGeometry, transform);

            transformedGeometryWrapper = new GeometryWrapper(transformedGeometry, srsURI, datatypeEnum, dimensionInfo);
            if (IS_CRS_TRANSFORMATION_ACTIVE && storeCRSTransform) {
                crsTransfomations.put(srsURI, transformedGeometryWrapper);
            }
        }
        return transformedGeometryWrapper;
    }

    /**
     * Sets the maximum number of CRS Transformations that each GeometryWrapper
     * will retain.<br>
     * Only applies to future GeometryWrappers created.
     *
     * @param maxSize
     */
    public static final void setCRSTransformationsMaxSize(Integer maxSize) {

        IS_CRS_TRANSFORMATION_ACTIVE = maxSize != 0;

        if (IS_CRS_TRANSFORMATION_ACTIVE) {
            CRS_TRANSFORMATIONS_MAX_SIZE = maxSize;
        } else {
            CRS_TRANSFORMATIONS_MAX_SIZE = IndexConfiguration.GEOMETRY_WRAPPER_CRS_TRANSFORMATIONS_MAX_SIZE_DEFAULT;
        }

    }

    public static final GeometryWrapper getEmptyWKT() {
        return WKTDatatype.INSTANCE.read("");
    }

    public static final GeometryWrapper getEmptyGML() {
        return GMLDatatype.INSTANCE.read("");
    }

    //If parsingGeometry are equal then xyGeometry will be equal.
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.parsingGeometry);
        hash = 71 * hash + Objects.hashCode(this.srsURI);
        hash = 71 * hash + Objects.hashCode(this.datatypeEnum);
        hash = 71 * hash + Objects.hashCode(this.crs);
        hash = 71 * hash + Objects.hashCode(this.unitsOfMeasure);
        hash = 71 * hash + Objects.hashCode(this.dimensionInfo);
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
        if (this.datatypeEnum != other.datatypeEnum) {
            return false;
        }
        if (!Objects.equals(this.parsingGeometry, other.parsingGeometry)) {
            return false;
        }
        if (!Objects.equals(this.crs, other.crs)) {
            return false;
        }
        if (!Objects.equals(this.unitsOfMeasure, other.unitsOfMeasure)) {
            return false;
        }
        return Objects.equals(this.dimensionInfo, other.dimensionInfo);
    }

    @Override
    public String toString() {
        return "GeometryWrapper{" + "xyGeometry=" + xyGeometry + ", parsingGeometry=" + parsingGeometry + ", srsURI=" + srsURI + ", datatypeEnum=" + datatypeEnum + ", crs=" + crs + ", unitsOfMeasure=" + unitsOfMeasure + ", dimensionInfo=" + dimensionInfo + '}';
    }

}
