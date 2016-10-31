/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Geometry;
import java.util.Objects;
import java.util.logging.Level;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.geotools.geometry.GeometryBuilder;
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
    private final GeoSerialisation serialisation;

    public GeometryWrapper(Geometry geometry, String srsName, GeoSerialisation serialisation) {
        this.geometry = geometry;
        this.srsURI = srsName;
        this.serialisation = serialisation;

        CRSRegistry.addCRS(srsName);
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

                transformedCRSGeometry = new GeometryWrapper(targetGeometry, srsURI, serialisation);
            } else {
                transformedCRSGeometry = sourceCRSGeometry;
            }

        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            java.util.logging.Logger.getLogger(GeometryWrapper.class.getName()).log(Level.SEVERE, null, ex);
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

    public GeoSerialisation getSerialisation() {
        return serialisation;
    }

    public GeometryWrapper buffer(double distance) {

        Geometry geo = this.geometry.buffer(distance);
        return new GeometryWrapper(geo, this.srsURI, this.serialisation);
    }

    public GeometryWrapper convexHull() {

        Geometry geo = this.geometry.convexHull();
        return new GeometryWrapper(geo, this.srsURI, this.serialisation);
    }

    public GeometryWrapper difference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        Geometry geo = this.geometry.difference(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, this.srsURI, this.serialisation);
    }

    public double distance(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        //TODO
        //JTS assumes cartesian CRS. JTS.orthodromicDistance expresses distance in metres.
        //CordinateReferenceSystems refer to units and shape in the WKT.
        //Check the shape
        //http://docs.geotools.org/stable/userguide/library/api/jts.html
        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        CoordinateReferenceSystem crs = getCRS();
        String wktMetadata = crs.toWKT();

        double distance;
        if (wktMetadata.contains("CARTESIAN")) {
            distance = this.geometry.distance(transformedGeometry.getGeometry());
        } else {
            //Convert to ISO Geometry??
            GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);

            //Find closest points in the geometry??
            JTS.orthodromicDistance(geometry, transformedGeometry, crs);
        }

        return;
    }

    public GeometryWrapper getBoundary() {

        Geometry geo = this.geometry.getBoundary();
        return new GeometryWrapper(geo, this.srsURI, this.serialisation);
    }

    public GeometryWrapper getEnvelope() {

        Geometry geo = this.geometry.getEnvelope();
        return new GeometryWrapper(geo, this.srsURI, this.serialisation);
    }

    public String getSRID() {
        CoordinateReferenceSystem crs = CRSRegistry.getCRS(srsURI);
        return CRS.toSRS(crs);
    }

    public GeometryWrapper intersection(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.intersection(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, this.srsURI, this.serialisation);
    }

    public boolean relate(GeometryWrapper targetGeometry, String intersectionPattern) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);

        return geometry.relate(transformedGeometry.geometry, intersectionPattern);
    }

    public GeometryWrapper symDifference(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.symDifference(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, this.srsURI, this.serialisation);
    }

    public GeometryWrapper union(GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {

        GeometryWrapper transformedGeometry = checkCRS(targetGeometry);
        Geometry geo = this.geometry.union(transformedGeometry.getGeometry());
        return new GeometryWrapper(geo, this.srsURI, this.serialisation);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.geometry);
        hash = 41 * hash + Objects.hashCode(this.srsURI);
        hash = 41 * hash + Objects.hashCode(this.serialisation);
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
        return this.serialisation == other.serialisation;
    }

    @Override
    public String toString() {
        return "CRSGeometry{" + "geometry=" + geometry + ", srsName=" + srsURI + ", serialisation=" + serialisation + '}';
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

        } catch (DatatypeFormatException dfx) {
            LOGGER.error("Illegal Datatype, CANNOT parse to Geometry: {}", dfx);
            throw dfx;
        }

    }

}
