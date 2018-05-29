/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import implementation.GeometryWrapper;
import implementation.datatype.DatatypeReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.TxnType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gerg
 */
public class GeometryLiteralIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static LRUMap<String, GeometryWrapper> GEOMETRY_LITERAL_INDEX = new LRUMap<>(IndexDefaultValues.GEOMETRY_LITERAL_INDEX_MAX_SIZE_DEFAULT);
    private static Boolean IS_INDEX_ACTIVE = true;
    private static Dataset DATASET = null;

    public static final GeometryWrapper retrieve(String geometryLiteral, DatatypeReader datatypeReader) {
        GeometryWrapper geometryWrapper;

        switch (IndexConfiguration.getIndexOption()) {
            case TDB:
                geometryWrapper = retrieveTDBIndex(geometryLiteral, datatypeReader);
                break;
            default:
                geometryWrapper = retrieveMemoryIndex(geometryLiteral, datatypeReader);
        }

        return geometryWrapper;
    }

    private static GeometryWrapper retrieveMemoryIndex(String geometryLiteral, DatatypeReader datatypeReader) {
        GeometryWrapper geometryWrapper;
        if (GEOMETRY_LITERAL_INDEX.containsKey(geometryLiteral)) {
            geometryWrapper = GEOMETRY_LITERAL_INDEX.get(geometryLiteral);
        } else {
            geometryWrapper = datatypeReader.read(geometryLiteral);
            if (IS_INDEX_ACTIVE) {
                GEOMETRY_LITERAL_INDEX.put(geometryLiteral, geometryWrapper);
            }
        }
        return geometryWrapper;
    }

    public static final void write(File geometryLiteralIndexFile) {

        LOGGER.info("Writing Geometry Literal Index - {}: Started", geometryLiteralIndexFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(geometryLiteralIndexFile))) {
            objectOutputStream.writeObject(GEOMETRY_LITERAL_INDEX);
        } catch (IOException ex) {
            LOGGER.error("Store Geometry Literal Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Geometry Literal Index - {}: Completed", geometryLiteralIndexFile);
    }

    public static final void read(File geometryLiteralIndexFile) {
        LOGGER.info("Reading Geometry Literal Index - {}: Started", geometryLiteralIndexFile);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(geometryLiteralIndexFile))) {
            @SuppressWarnings("unchecked")
            LRUMap<String, GeometryWrapper> geometryLiteralIndex = (LRUMap<String, GeometryWrapper>) objectInputStream.readObject();
            GEOMETRY_LITERAL_INDEX.putAll(geometryLiteralIndex);
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Geometry Literal Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Reading Geometry Literal Index - {}: Completed", geometryLiteralIndexFile);
    }

    /**
     * Empty the Geometry Literal Index.
     */
    public static final void clear() {
        GEOMETRY_LITERAL_INDEX.clear();
    }

    /**
     * Changes the max size of the Geometry Literal Index.
     * <br> The index will be empty after this process.
     *
     * @param maxSize
     */
    public static final void setMaxSize(Integer maxSize) {

        IS_INDEX_ACTIVE = maxSize != 0;

        LRUMap<String, GeometryWrapper> newGeometryIndex;
        if (IS_INDEX_ACTIVE) {
            newGeometryIndex = new LRUMap<>(maxSize);
        } else {
            newGeometryIndex = new LRUMap<>();
        }
        GEOMETRY_LITERAL_INDEX.clear();
        GEOMETRY_LITERAL_INDEX = newGeometryIndex;
    }

    private static final String INDEX_URI = "http://example.org/geometryIndex#";
    private static final String INDEX_GRAPH_URI = "http://example.org/geometryIndex#Graph";

    private static final Property GEOMETRY_PROPERTY = ResourceFactory.createProperty(INDEX_URI + "hasLiteral");
    private static final Property WRAPPER_PROPERTY = ResourceFactory.createProperty(INDEX_URI + "hasWrapper");

    public static final void setupTDBIndex(Dataset dataset) {

        DATASET = dataset;
        DATASET.begin(TxnType.READ_PROMOTE);
        if (!DATASET.containsNamedModel(INDEX_GRAPH_URI)) {
            Model model = ModelFactory.createDefaultModel();
            DATASET.promote();
            DATASET.addNamedModel(INDEX_GRAPH_URI, model);
            DATASET.commit();
        }
        DATASET.end();
    }

    public static final void clearTDBIndex() {

        DATASET.begin(TxnType.WRITE);
        Model model = DATASET.getNamedModel(INDEX_GRAPH_URI);
        model.removeAll();
        DATASET.commit();
        DATASET.end();
    }

    private static GeometryWrapper retrieveTDBIndex(String geometryLiteral, DatatypeReader datatypeReader) {
        GeometryWrapper geometryWrapper;
        Literal geometryString = ResourceFactory.createPlainLiteral(geometryLiteral);
        DATASET.begin(TxnType.READ_PROMOTE);
        Model indexModel = DATASET.getNamedModel(INDEX_GRAPH_URI);

        if (indexModel.contains(null, GEOMETRY_PROPERTY, geometryString)) {
            Resource resource = indexModel.listResourcesWithProperty(GEOMETRY_PROPERTY, geometryString).nextResource();
            Literal wrapper = indexModel.getProperty(resource, WRAPPER_PROPERTY).getLiteral();
            geometryWrapper = decodeBase64(wrapper.getLexicalForm());
        } else {
            geometryWrapper = datatypeReader.read(geometryLiteral);
            Resource resource = ResourceFactory.createResource(IndexConfiguration.createURI(INDEX_URI, "GeometryLiteral"));
            Literal wrapperString = encodeBase64(geometryWrapper);
            DATASET.promote();
            indexModel.add(resource, GEOMETRY_PROPERTY, geometryString);
            indexModel.add(resource, WRAPPER_PROPERTY, wrapperString);
            DATASET.commit();
        }
        DATASET.end();

        return geometryWrapper;
    }

    private static final Encoder ENCODER = Base64.getEncoder();

    private static Literal encodeBase64(GeometryWrapper geometryWrapper) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos)) {
            objectOutputStream.writeObject(geometryWrapper);
            String encodedStr = ENCODER.encodeToString(baos.toByteArray());
            Literal wrapperString = ResourceFactory.createPlainLiteral(encodedStr);
            baos.close();
            return wrapperString;
        } catch (IOException ex) {
            LOGGER.error("IOException: {}", ex.getMessage());
            return null;
        }
    }

    private static final Decoder DECODER = Base64.getDecoder();

    private static GeometryWrapper decodeBase64(String wrapperString) {

        byte[] bytes = DECODER.decode(wrapperString);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            @SuppressWarnings("unchecked")
            GeometryWrapper geometryWrapper = (GeometryWrapper) objectInputStream.readObject();
            return geometryWrapper;
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Geometry Literal Index exception: {}", ex.getMessage());
            return null;
        }
    }

}
