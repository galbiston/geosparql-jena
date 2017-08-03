/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import implementation.support.GeoSerialisationEnum;
import implementation.support.UnitsOfMeasure;
import java.util.Objects;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.sparql.expr.NodeValue;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gregory Albiston
 */
public class GeometryWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeometryWrapper.class);

    private final Geometry geometry;
    private final String srsURI;
    private final GeoSerialisationEnum serialisation;
    private final CoordinateReferenceSystem crs;
    private final UnitsOfMeasure unitsOfMeasure;
    private final DimensionInfo dimensionInfo;

    //TODO Handling of axis order. CRS.decode(crs, true) actually affects the x,y order??
    public GeometryWrapper(Geometry geometry, String srsURI, GeoSerialisationEnum serialisation, DimensionInfo dimensionInfo) {

        this.srsURI = srsURI;
        this.serialisation = serialisation;

        this.crs = CRSRegistry.addCRS(srsURI);
        this.unitsOfMeasure = CRSRegistry.getUnits(srsURI);

        this.dimensionInfo = dimensionInfo;

        this.geometry = GeometryReverse.check(geometry, crs);

    }

    public GeometryWrapper(GeometryWrapper geometryWrapper) {

        this.geometry = geometryWrapper.geometry;
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
     * @param sourceCRSGeometry
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public GeometryWrapper checkCRS(GeometryWrapper sourceCRSGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedCRSGeometry;
        try {
            if (!srsURI.equals(sourceCRSGeometry.srsURI)) {
                CoordinateReferenceSystem sourceCRS = sourceCRSGeometry.getCRS();

                Geometry sourceGeometry = sourceCRSGeometry.getParsingGeometry();  //Retreive the original coordinate order according to the CRS.

                MathTransform transform = CRS.findMathTransform(sourceCRS, crs, false);
                Geometry targetGeometry = JTS.transform(sourceGeometry, transform);

                transformedCRSGeometry = new GeometryWrapper(targetGeometry, srsURI, serialisation, dimensionInfo);
            } else {
                transformedCRSGeometry = sourceCRSGeometry;
            }

        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("CRS Check Exception: {}", ex.getMessage());
            throw ex;
        }

        return transformedCRSGeometry;
    }

    public CoordinateReferenceSystem getCRS() {
        return crs;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Geometry getParsingGeometry() {
        return GeometryReverse.check(geometry, crs);
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

    public GeoSerialisationEnum getSerialisation() {
        return serialisation;
    }

    public GeometryWrapper buffer(double distance, String targetDistanceUnitsURI) {

        distance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, unitsOfMeasure);

        Geometry geo = this.geometry.buffer(distance);
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper convexHull() {

        Geometry geo = this.geometry.convexHull();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        Geometry geo = this.geometry.difference(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public double distance(GeometryWrapper targetGeometry, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        double distance = this.geometry.distance(transformedGeometry.getGeometry());

        distance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, unitsOfMeasure);

        return distance;
    }

    public GeometryWrapper getBoundary() {

        Geometry geo = this.geometry.getBoundary();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper getEnvelope() {

        Geometry geo = this.geometry.getEnvelope();
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.intersection(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public IntersectionMatrix relate(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return geometry.relate(transformedGeometry.geometry);
    }

    public boolean relate(GeometryWrapper targetGeometry, String intersectionPattern) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return geometry.relate(transformedGeometry.geometry, intersectionPattern);
    }

    public GeometryWrapper symDifference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.symDifference(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.union(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, srsURI, serialisation, dimensionInfo);
    }

    public boolean contains(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.geometry.contains(transformedGeometry.getGeometry());
    }

    public boolean crosses(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.geometry.crosses(transformedGeometry.getGeometry());
    }

    public boolean disjoint(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.geometry.disjoint(transformedGeometry.getGeometry());
    }

    public boolean equals(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.geometry.equals((Geometry) transformedGeometry.getGeometry());
    }

    public boolean intersects(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.geometry.intersects(transformedGeometry.getGeometry());
    }

    public boolean overlaps(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.geometry.overlaps(transformedGeometry.getGeometry());
    }

    public boolean touches(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.geometry.touches(transformedGeometry.getGeometry());
    }

    public boolean within(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        return this.geometry.within(transformedGeometry.getGeometry());
    }

    public NodeValue getResultNode() throws DatatypeFormatException {

        RDFDatatype datatype;
        String lexicalForm;

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

        lexicalForm = datatype.unparse(this);

        return NodeValue.makeNode(lexicalForm, datatype);
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

    public boolean isEmpty() {
        return this.geometry.isEmpty();
    }

    public boolean isSimple() {
        return this.geometry.isSimple();
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.geometry);
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
        if (!Objects.equals(this.geometry, other.geometry)) {
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
        return "GeometryWrapper{" + "geometry=" + geometry + ", srsURI=" + srsURI + ", serialisation=" + serialisation + ", crs=" + crs + ", unitsOfMeasure=" + unitsOfMeasure + ", dimensionInfo=" + dimensionInfo + '}';
    }

}
