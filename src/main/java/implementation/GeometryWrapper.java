/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.index.CRSRegistry;
import implementation.index.GeometryTransformIndex;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import implementation.support.GeoSerialisationEnum;
import implementation.support.UnitsOfMeasure;
import implementation.vocabulary.UnitsOfMeasureLookUp;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.expr.NodeValue;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GeometryWrapper implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Geometry xyGeometry;
    private final Geometry parsingGeometry;
    private final String srsURI;
    private final GeoSerialisationEnum serialisation;
    private final CoordinateReferenceSystem crs;
    private final UnitsOfMeasure unitsOfMeasure;
    private final DimensionInfo dimensionInfo;

    public GeometryWrapper(Geometry geometry, String srsURI, GeoSerialisationEnum serialisation, DimensionInfo dimensionInfo) {

        this.serialisation = serialisation;

        if (srsURI.isEmpty()) {
            srsURI = CRSRegistry.DEFAULT_WKT_CRS84;
        }
        this.srsURI = srsURI;
        this.crs = CRSRegistry.addCRS(srsURI);
        this.unitsOfMeasure = CRSRegistry.getUnits(srsURI);

        this.dimensionInfo = dimensionInfo;

        this.xyGeometry = GeometryReverse.check(geometry, crs);
        this.parsingGeometry = geometry;
    }

    /**
     * Default to WGS84 and XY coordinate dimensions.
     *
     * @param geometry
     * @param serialisation
     */
    public GeometryWrapper(Geometry geometry, GeoSerialisationEnum serialisation) {
        this(geometry, "", serialisation, DimensionInfo.xyPoint());
    }

    /**
     * Default to XY coordinate dimensions.
     *
     * @param geometry
     * @param srsURI
     * @param serialisation
     */
    public GeometryWrapper(Geometry geometry, String srsURI, GeoSerialisationEnum serialisation) {
        this(geometry, srsURI, serialisation, DimensionInfo.xyPoint());
    }

    transient private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    /**
     * Empty geometry with specified parameters.
     *
     * @param srsURI
     * @param serialisation
     * @param dimensionInfo
     */
    public GeometryWrapper(String srsURI, GeoSerialisationEnum serialisation, DimensionInfo dimensionInfo) {
        this(GEOMETRY_FACTORY.createPoint(new CustomCoordinateSequence(DimensionInfo.xyPoint().getDimensions())), srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper(GeometryWrapper geometryWrapper) {

        this.xyGeometry = geometryWrapper.xyGeometry;
        this.parsingGeometry = geometryWrapper.parsingGeometry;
        this.srsURI = geometryWrapper.srsURI;
        this.serialisation = geometryWrapper.serialisation;

        this.crs = geometryWrapper.crs;
        this.unitsOfMeasure = geometryWrapper.unitsOfMeasure;
        this.dimensionInfo = geometryWrapper.dimensionInfo;
    }

    /**
     * Transforms, if necessary, the provided GeometryWrapper according to the
     * current GeometryWrapper CRS.
     *
     * @param sourceGeometryWrapper
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper checkCRS(GeometryWrapper sourceGeometryWrapper) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometryWrapper;

        if (srsURI.equals(sourceGeometryWrapper.srsURI)) {
            transformedGeometryWrapper = sourceGeometryWrapper;
        } else {
            transformedGeometryWrapper = GeometryTransformIndex.transform(sourceGeometryWrapper, srsURI);
        }

        return transformedGeometryWrapper;
    }

    /**
     * Returns this geometry wrapper converted to the SRS URI.
     *
     * @param srsURI
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper convertCRS(String srsURI) throws FactoryException, MismatchedDimensionException, TransformException {
        return GeometryTransformIndex.transform(this, srsURI);
    }

    public CoordinateReferenceSystem getCRS() {
        return crs;
    }

    /**
     * Geometry with coordinates in x,y order, regardless of CRS.
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

    public GeoSerialisationEnum getGeoSerialisation() {
        return serialisation;
    }

    public GeometryWrapper buffer(double distance, String targetDistanceUnitsURI) {

        distance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, unitsOfMeasure);

        Geometry geo = this.xyGeometry.buffer(distance);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper convexHull() {

        Geometry geo = this.xyGeometry.convexHull();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        Geometry geo = this.xyGeometry.difference(transformedGeometry.xyGeometry);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
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
        return distance(targetGeometry, UnitsOfMeasureLookUp.METRE_URI);
    }

    public double distance(GeometryWrapper targetGeometry, UnitsOfMeasure unitsOfMeasure) throws FactoryException, MismatchedDimensionException, TransformException {
        return distance(targetGeometry, UnitsOfMeasureLookUp.getUnitURI(unitsOfMeasure));
    }

    public double distance(GeometryWrapper targetGeometry, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        double distance = this.xyGeometry.distance(transformedGeometry.xyGeometry);

        distance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, unitsOfMeasure);

        return distance;
    }

    public GeometryWrapper getBoundary() {

        Geometry geo = this.xyGeometry.getBoundary();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper getEnvelope() {

        Geometry geo = this.xyGeometry.getEnvelope();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.xyGeometry.intersection(transformedGeometry.xyGeometry);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public IntersectionMatrix relate(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return xyGeometry.relate(transformedGeometry.xyGeometry);
    }

    public boolean relate(GeometryWrapper targetGeometry, String intersectionPattern) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return xyGeometry.relate(transformedGeometry.xyGeometry, intersectionPattern);
    }

    public GeometryWrapper symDifference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.xyGeometry.symDifference(transformedGeometry.xyGeometry);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.xyGeometry.union(transformedGeometry.xyGeometry);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public boolean contains(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.xyGeometry.contains(transformedGeometry.xyGeometry);
    }

    public boolean crosses(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.xyGeometry.crosses(transformedGeometry.xyGeometry);
    }

    public boolean disjoint(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.xyGeometry.disjoint(transformedGeometry.xyGeometry);
    }

    public boolean equals(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.xyGeometry.equals((Geometry) transformedGeometry.xyGeometry);
    }

    public boolean intersects(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.xyGeometry.intersects(transformedGeometry.xyGeometry);
    }

    public boolean overlaps(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.xyGeometry.overlaps(transformedGeometry.xyGeometry);
    }

    public boolean touches(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.xyGeometry.touches(transformedGeometry.xyGeometry);
    }

    public boolean within(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.xyGeometry.within(transformedGeometry.xyGeometry);
    }

    public NodeValue asNode() throws DatatypeFormatException {

        Literal literal = asLiteral();
        return NodeValue.makeNode(literal.getLexicalForm(), literal.getDatatype());
    }

    public Literal asLiteral() throws DatatypeFormatException {

        RDFDatatype datatype;

        switch (serialisation) {
            case WKT:
                datatype = WKTDatatype.THE_WKT_DATATYPE;
                break;
            case GML:
                datatype = GMLDatatype.THE_GML_DATATYPE;
                break;
            default:
                throw new DatatypeFormatException("Serialisation is not WKT or GML.");
        }

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

    private static final WKTDatatype WKT_DATATYPE = WKTDatatype.THE_WKT_DATATYPE;
    private static final GMLDatatype GML_DATATYPE = GMLDatatype.THE_GML_DATATYPE;

    public static final GeometryWrapper extract(NodeValue nodeValue) throws DatatypeFormatException {

        Node node = nodeValue.asNode();

        String datatypeURI = node.getLiteralDatatypeURI();
        String lexicalForm = node.getLiteralLexicalForm();
        return extract(lexicalForm, datatypeURI);
    }

    public static final GeometryWrapper extract(Literal geometryLiteral) throws DatatypeFormatException {
        return extract(geometryLiteral.getLexicalForm(), geometryLiteral.getDatatypeURI());
    }

    private static GeometryWrapper extract(String lexicalForm, String datatypeURI) throws DatatypeFormatException {
        GeometryWrapper geometry;

        switch (datatypeURI) {
            case WKTDatatype.THE_TYPE_URI:
                geometry = WKT_DATATYPE.parse(lexicalForm);
                break;
            case GMLDatatype.THE_TYPE_URI:
                geometry = GML_DATATYPE.parse(lexicalForm);
                break;
            default:
                throw new DatatypeFormatException("Literal is not a WKT or GML Literal.");
        }

        return geometry;

    }

    public static final GeometryWrapper EMPTY_WKT = WKTDatatype.THE_WKT_DATATYPE.read("POINT EMPTY");

    /*
    //TODO - empty GML GEometryWrapper creation.
    public static final GeometryWrapper emptyGML() {

    }
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.xyGeometry);
        hash = 79 * hash + Objects.hashCode(this.srsURI);
        hash = 79 * hash + Objects.hashCode(this.serialisation);
        hash = 79 * hash + Objects.hashCode(this.crs);
        hash = 79 * hash + Objects.hashCode(this.unitsOfMeasure);
        hash = 79 * hash + Objects.hashCode(this.dimensionInfo);
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
        if (!Objects.equals(this.xyGeometry, other.xyGeometry)) {
            return false;
        }
        if (this.serialisation != other.serialisation) {
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
        return "GeometryWrapper{" + "xyGeometry=" + xyGeometry + ", parsingGeometry=" + parsingGeometry + ", srsURI=" + srsURI + ", serialisation=" + serialisation + ", crs=" + crs + ", unitsOfMeasure=" + unitsOfMeasure + ", dimensionInfo=" + dimensionInfo + '}';
    }

}
