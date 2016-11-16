/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import implementation.support.GeoSerialisationEnum;
import implementation.support.UnitsOfMeasure;
import java.util.Objects;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
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
    private final Integer sridInt;

    //TODO Handling of axis order. CRS.decode(crs, true) actually affects the x,y order??
    public GeometryWrapper(Geometry geometry, String srsURI, GeoSerialisationEnum serialisation) {

        this.srsURI = srsURI;
        this.serialisation = serialisation;

        this.crs = CRSRegistry.addCRS(srsURI);
        this.unitsOfMeasure = CRSRegistry.getUnits(srsURI);
        this.sridInt = CRSRegistry.getSRID(srsURI);

        this.geometry = GeometryReverse.check(geometry, crs);
    }

    public GeometryWrapper(GeometryWrapper geometryWrapper) {

        this.geometry = geometryWrapper.geometry;
        this.srsURI = geometryWrapper.srsURI;
        this.serialisation = geometryWrapper.serialisation;

        this.crs = geometryWrapper.crs;
        this.unitsOfMeasure = geometryWrapper.unitsOfMeasure;
        this.sridInt = geometryWrapper.sridInt;
    }

    public GeometryWrapper checkCRS(GeometryWrapper sourceCRSGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedCRSGeometry;
        try {
            if (!sridInt.equals(sourceCRSGeometry.sridInt)) {
                CoordinateReferenceSystem sourceCRS = sourceCRSGeometry.getCRS();

                Geometry sourceGeometry = sourceCRSGeometry.geometry;

                MathTransform transform = CRS.findMathTransform(sourceCRS, crs, false);
                Geometry targetGeometry = JTS.transform(sourceGeometry, transform);

                transformedCRSGeometry = new GeometryWrapper(targetGeometry, srsURI, serialisation);
            } else {
                transformedCRSGeometry = new GeometryWrapper(sourceCRSGeometry);
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

    public GeoSerialisationEnum getSerialisation() {
        return serialisation;
    }

    public GeometryWrapper buffer(double distance, String targetDistanceUnitsURI) {

        distance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, unitsOfMeasure);

        Geometry geo = this.geometry.buffer(distance);
        return new GeometryWrapper(geo, srsURI, serialisation);
    }

    public GeometryWrapper convexHull() {

        Geometry geo = this.geometry.convexHull();
        return new GeometryWrapper(geo, srsURI, serialisation);
    }

    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        Geometry geo = this.geometry.difference(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, srsURI, serialisation);
    }

    public double distance(GeometryWrapper targetGeometry, String targetDistanceUnitsURI) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        double distance = this.geometry.distance(transformedGeometry.getGeometry());

        distance = UnitsOfMeasure.conversion(distance, targetDistanceUnitsURI, unitsOfMeasure);

        return distance;
    }

    public GeometryWrapper getBoundary() {

        Geometry geo = this.geometry.getBoundary();
        return new GeometryWrapper(geo, srsURI, serialisation);
    }

    public GeometryWrapper getEnvelope() {

        Geometry geo = this.geometry.getEnvelope();
        return new GeometryWrapper(geo, srsURI, serialisation);
    }

    public String getSRID() {
        return CRS.toSRS(crs);
    }

    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.intersection(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, srsURI, serialisation);
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
        return new GeometryWrapper(geo, srsURI, serialisation);
    }

    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.union(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, srsURI, serialisation);
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

    public NodeValue getResultNode() {

        String lexicalForm = DatatypeSelector.unparse(this, serialisation);
        return NodeValue.makeNodeString(lexicalForm);
    }

    public static final GeometryWrapper extract(NodeValue nodeValue) throws DatatypeFormatException {

        try {
            Node node = nodeValue.asNode();

            String dataTypeURI = node.getLiteralDatatypeURI();
            String lexicalForm = node.getLiteralLexicalForm();

            GeometryWrapper geometry = DatatypeSelector.parse(lexicalForm, dataTypeURI);
            return geometry;

        } catch (DatatypeFormatException ex) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", ex);
            throw ex;
        }

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.geometry);
        hash = 89 * hash + Objects.hashCode(this.srsURI);
        hash = 89 * hash + Objects.hashCode(this.serialisation);
        hash = 89 * hash + Objects.hashCode(this.crs);
        hash = 89 * hash + Objects.hashCode(this.unitsOfMeasure);
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
        return Objects.equals(this.unitsOfMeasure, other.unitsOfMeasure);
    }

    @Override
    public String toString() {
        return "GeometryWrapper{" + "geometry=" + geometry + ", srsURI=" + srsURI + ", serialisation=" + serialisation + ", crs=" + crs + ", unitsOfMeasure=" + unitsOfMeasure + '}';
    }

}
