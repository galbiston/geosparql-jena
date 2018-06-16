/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import implementation.GeometryWrapper;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.vocabulary.Geo;
import implementation.vocabulary.SRS_URI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.base.file.Location;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gerg
 */
public class SpatialIndex implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static HashSet<String> GEOMETRY_LITERALS = new HashSet<>();
    private static Quadtree QUAD_TREE = new Quadtree();

    private static final String GEOMETRY_LITERAL_FILE = "spatial-literal.index";
    private static final String QUAD_TREE_FILE = "spatial-quad.index";

    public static final Boolean addIfMissing(NodeValue nodeValue) throws FactoryException, MismatchedDimensionException, TransformException {

        if (!nodeValue.isLiteral()) {
            return null;
        }

        Node node = nodeValue.asNode();
        String datatypeURI = node.getLiteralDatatypeURI();
        String lexicalForm = node.getLiteralLexicalForm();

        return addIfMissing(lexicalForm, datatypeURI);
    }

    public static final Boolean addIfMissing(Literal literal) throws FactoryException, MismatchedDimensionException, TransformException {

        String datatypeURI = literal.getDatatypeURI();
        String lexicalForm = literal.getLexicalForm();

        return addIfMissing(lexicalForm, datatypeURI);
    }

    public static final Boolean addIfMissing(String lexicalForm, String datatypeURI) throws FactoryException, MismatchedDimensionException, TransformException {
        if (datatypeURI.equals(WKTDatatype.URI) || datatypeURI.equals(GMLDatatype.URI)) {
            if (contains(lexicalForm)) {
                return false;
            } else {
                insert(lexicalForm, datatypeURI);
                return true;
            }
        }

        return null;
    }

    public static final Boolean contains(String geometryLiteral) {
        return GEOMETRY_LITERALS.contains(geometryLiteral);
    }

    public static final void build(Dataset dataset) {
        Model defaultModel = dataset.getDefaultModel();
        SpatialIndex.build(defaultModel);

        Model unionModel = dataset.getUnionModel();
        SpatialIndex.build(unionModel);
    }

    public static final void build(Model model) {
        LOGGER.info("Building Spatial Index: Started");
        try {
            NodeIterator nodeIt = model.listObjectsOfProperty(Geo.HAS_SERIALIZATION_PROP);
            while (nodeIt.hasNext()) {
                RDFNode node = nodeIt.nextNode();
                if (node.isLiteral()) {
                    Literal literal = node.asLiteral();
                    addIfMissing(literal);
                }
            }
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Build Spatial Index Exception: {}", ex.getMessage());
        }
        LOGGER.info("Building Spatial Index: Finished");
    }

    public static final void insert(String lexicalForm, String datatypeURI) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper geometryWrapper = GeometryWrapper.extract(lexicalForm, datatypeURI);
        Envelope envelope = extractEnvelope(geometryWrapper);
        GEOMETRY_LITERALS.add(lexicalForm);
        QUAD_TREE.insert(envelope, lexicalForm);
    }

    public static final Boolean query(GeometryWrapper sourceGeometryWrapper, NodeValue nodeValue) throws FactoryException, MismatchedDimensionException, TransformException {

        if (!nodeValue.isLiteral()) {
            return null;
        }

        Node node = nodeValue.asNode();
        String lexicalForm = node.getLiteralLexicalForm();
        return query(sourceGeometryWrapper, lexicalForm);
    }

    public static final Boolean query(GeometryWrapper sourceGeometryWrapper, Literal literal) throws FactoryException, MismatchedDimensionException, TransformException {
        String lexicalForm = literal.getLexicalForm();
        return query(sourceGeometryWrapper, lexicalForm);
    }

    public static final Boolean query(GeometryWrapper geometryWrapper, String testGeometryLiteral) throws FactoryException, MismatchedDimensionException, TransformException {
        Envelope searchEnvelope = extractEnvelope(geometryWrapper);
        List<String> intersections = QUAD_TREE.query(searchEnvelope);
        boolean isIntersected = intersections.contains(testGeometryLiteral);
        return isIntersected;
    }

    private static Envelope extractEnvelope(GeometryWrapper sourceGeometryWrapper) throws FactoryException, MismatchedDimensionException, TransformException {
        GeometryWrapper transformedGeometryWrapper = sourceGeometryWrapper.transform(SRS_URI.GEOTOOLS_GEOCENTRIC_CARTESIAN);
        Envelope envelope = transformedGeometryWrapper.getEnvelope();
        return envelope;
    }

    public static final void clear() {
        GEOMETRY_LITERALS = new HashSet<>();
        QUAD_TREE = new Quadtree();
    }

    public static final void write(File indexFolder) {
        LOGGER.info("Writing Spatial Index: Started");
        indexFolder.mkdir();

        File quadTreeFilepath = new File(indexFolder, QUAD_TREE_FILE);
        writeObject(quadTreeFilepath, QUAD_TREE);

        File geometryLiteralFilepath = new File(indexFolder, GEOMETRY_LITERAL_FILE);
        writeObject(geometryLiteralFilepath, GEOMETRY_LITERALS);
        LOGGER.info("Writing Spatial Index: Finished");
    }

    /**
     * Reads existing index files if TDB backed or creates new ones if not
     * present. Also, builds index if memory dataset.
     *
     * @param dataset
     */
    public static final void prepare(Dataset dataset) {

        if (TDBFactory.isBackedByTDB(dataset)) {
            Location location = TDBFactory.location(dataset);
            File datasetFolder = new File(location.getDirectoryPath());
            if (containsIndex(datasetFolder)) {
                read(datasetFolder);
            } else {
                build(dataset);
                write(datasetFolder);
            }

        } else {
            build(dataset);
        }

    }

    public static final void read(File indexFolder) {
        LOGGER.info("Reading Spatial Index: Started");

        File quadTreeFilepath = new File(indexFolder, QUAD_TREE_FILE);
        Quadtree quadtree = (Quadtree) readObject(quadTreeFilepath);
        if (quadtree != null) {
            QUAD_TREE = quadtree;
        }

        File geometryLiteralFilepath = new File(indexFolder, GEOMETRY_LITERAL_FILE);
        HashSet<String> geometryLiterals = (HashSet<String>) readObject(geometryLiteralFilepath);
        if (geometryLiterals != null) {
            GEOMETRY_LITERALS = geometryLiterals;
        }
        LOGGER.info("Reading Spatial Index: Finished");
    }

    private static final boolean containsIndex(File indexFolder) {
        File quadTreeFilepath = new File(indexFolder, QUAD_TREE_FILE);
        File geometryLiteralFilepath = new File(indexFolder, GEOMETRY_LITERAL_FILE);

        return quadTreeFilepath.exists() && geometryLiteralFilepath.exists();
    }

    private static final void writeObject(File indexFile, Object index) {

        LOGGER.info("Writing Index - {}: Started", indexFile.getName());
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(indexFile))) {
            objectOutputStream.writeObject(index);
        } catch (IOException ex) {
            LOGGER.error("Store Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Index - {}: Completed", indexFile.getName());
    }

    @SuppressWarnings("unchecked")
    private static final Object readObject(File indexFile) {

        if (!indexFile.exists()) {
            LOGGER.error("Index does not exist: {}", indexFile.getAbsolutePath());
            return null;
        }

        LOGGER.info("Reading Index - {}: Started", indexFile.getName());
        Object result;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(indexFile))) {
            @SuppressWarnings("unchecked")
            Object object = objectInputStream.readObject();
            result = object;
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Index exception: {}", ex.getMessage());
            result = null;
        }
        LOGGER.info("Reading Index - {}: Completed", indexFile.getName());
        return result;
    }

}
