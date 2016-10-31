/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Geometry;
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
 * @author Greg
 */
public class GeometryWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeometryWrapper.class);

    private final Geometry geometry;
    private final String srsURI;
    private final GeoSerialisationEnum serialisation;
    private final DistanceUnitsEnum distanceUnits;

    public GeometryWrapper(Geometry geometry, String srsURI, GeoSerialisationEnum serialisation) {
        this.geometry = geometry;
        this.srsURI = srsURI;
        this.serialisation = serialisation;

        CoordinateReferenceSystem crs = CRSRegistry.getCRS(srsURI);
        this.distanceUnits = CRSRegistry.extractCRSDistanceUnits(crs);
    }

    public GeometryWrapper(Geometry geometry, String srsURI, GeoSerialisationEnum serialisation, DistanceUnitsEnum distanceUnits) {
        this.geometry = geometry;
        this.srsURI = srsURI;
        this.serialisation = serialisation;
        this.distanceUnits = distanceUnits;

        CRSRegistry.addCRS(srsURI);
    }

    public GeometryWrapper(GeometryWrapper geometryWrapper) {

        this.geometry = geometryWrapper.geometry;
        this.srsURI = geometryWrapper.srsURI;
        this.serialisation = geometryWrapper.serialisation;
        this.distanceUnits = geometryWrapper.distanceUnits;

    }

    public GeometryWrapper checkCRS(GeometryWrapper sourceCRSGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedCRSGeometry;
        try {
            if (!srsURI.equals(sourceCRSGeometry.srsURI)) {
                CoordinateReferenceSystem sourceCRS = sourceCRSGeometry.getCRS();
                CoordinateReferenceSystem targetCRS = getCRS();

                Geometry sourceGeometry = sourceCRSGeometry.geometry;

                MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
                Geometry targetGeometry = JTS.transform(sourceGeometry, transform);

                transformedCRSGeometry = new GeometryWrapper(targetGeometry, srsURI, serialisation, distanceUnits);
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
        return CRSRegistry.getCRS(srsURI);
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getSrsURI() {
        return srsURI;
    }

    public GeoSerialisationEnum getSerialisation() {
        return serialisation;
    }

    public GeometryWrapper buffer(double distance, DistanceUnitsEnum targetDistanceUnits) {

        if (!distanceUnits.equals(targetDistanceUnits)) {
            distance = UomConverter.conversion(distance, distanceUnits, targetDistanceUnits);
        }

        Geometry geo = this.geometry.buffer(distance);
        return new GeometryWrapper(geo, this.srsURI, this.serialisation, this.distanceUnits);
    }

    public GeometryWrapper convexHull() {

        Geometry geo = this.geometry.convexHull();
        return new GeometryWrapper(geo, this.srsURI, this.serialisation, this.distanceUnits);
    }

    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        Geometry geo = this.geometry.difference(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, this.srsURI, this.serialisation, this.distanceUnits);
    }

    public double distance(GeometryWrapper targetGeometry, DistanceUnitsEnum targetDistanceUnits) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        double distance = this.geometry.distance(transformedGeometry.getGeometry());

        if (!distanceUnits.equals(targetDistanceUnits)) {
            distance = UomConverter.conversion(distance, distanceUnits, targetDistanceUnits);
        }

        return distance;
    }

    public GeometryWrapper getBoundary() {

        Geometry geo = this.geometry.getBoundary();
        return new GeometryWrapper(geo, this.srsURI, this.serialisation, this.distanceUnits);
    }

    public GeometryWrapper getEnvelope() {

        Geometry geo = this.geometry.getEnvelope();
        return new GeometryWrapper(geo, this.srsURI, this.serialisation, this.distanceUnits);
    }

    public String getSRID() {
        CoordinateReferenceSystem crs = CRSRegistry.getCRS(srsURI);
        return CRS.toSRS(crs);
    }

    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.intersection(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, this.srsURI, this.serialisation, this.distanceUnits);
    }

    public boolean relate(GeometryWrapper targetGeometry, String intersectionPattern) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        return geometry.relate(transformedGeometry.geometry, intersectionPattern);
    }

    public GeometryWrapper symDifference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.symDifference(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, this.srsURI, this.serialisation, this.distanceUnits);
    }

    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.union(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, this.srsURI, this.serialisation, this.distanceUnits);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.geometry);
        hash = 17 * hash + Objects.hashCode(this.srsURI);
        hash = 17 * hash + Objects.hashCode(this.serialisation);
        hash = 17 * hash + Objects.hashCode(this.distanceUnits);
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
        return this.distanceUnits == other.distanceUnits;
    }

    @Override
    public String toString() {
        return "GeometryWrapper{" + "geometry=" + geometry + ", srsURI=" + srsURI + ", serialisation=" + serialisation + ", distanceUnits=" + distanceUnits + '}';
    }

    public NodeValue getResultNode() {

        String lexicalForm = GeoDatatype.unparse(geometry, serialisation);
        return NodeValue.makeNodeString(lexicalForm);
    }

    public static final GeometryWrapper extract(NodeValue nodeValue) throws DatatypeFormatException {

        try {
            Node node = nodeValue.asNode();

            String dataTypeURI = node.getLiteralDatatypeURI();
            String lexicalForm = node.getLiteralLexicalForm();

            GeometryWrapper geometry = GeoDatatype.parse(lexicalForm, dataTypeURI);
            return geometry;

        } catch (DatatypeFormatException ex) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", ex);
            throw ex;
        }

    }

}
