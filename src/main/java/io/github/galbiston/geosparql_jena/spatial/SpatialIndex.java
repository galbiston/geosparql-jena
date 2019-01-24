/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_jena.spatial;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLOperations;
import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.SRSInfo;
import io.github.galbiston.geosparql_jena.implementation.index.QueryRewriteIndex;
import io.github.galbiston.geosparql_jena.implementation.registry.SRSRegistry;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialExtension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.util.Context;
import org.apache.jena.sparql.util.Symbol;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.strtree.STRtree;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SpatialIndex for testing bounding box collisions between geometries within a
 * Dataset.<br>
 * Queries must be performed using the same SRS URI as the SpatialIndex.<br>
 * The SpatialIndex is added to the Dataset Context when it is built.<br>
 * QueryRewriteIndex is also stored in the SpatialIndex as its content is
 * Dataset specific.
 *
 */
public class SpatialIndex implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final Symbol SPATIAL_INDEX_SYMBOL = Symbol.create("http://jena.apache.org/spatial#index");

    private final SRSInfo srsInfo;
    private boolean isBuilt;
    private final STRtree strTree;
    private final QueryRewriteIndex queryRewriteIndex;

    /**
     * Unbuilt Spatial Index with provided capacity and default Query Rewrite
     * Index.
     *
     * @param capacity
     * @param srsURI
     */
    public SpatialIndex(int capacity, String srsURI) {
        int indexCapacity = capacity > 0 ? capacity : 1;
        this.strTree = new STRtree(indexCapacity);
        this.isBuilt = false;
        this.srsInfo = SRSRegistry.getSRSInfo(srsURI);
        this.queryRewriteIndex = QueryRewriteIndex.createDefault();
    }

    /**
     * Built Spatial Index with provided capacity and Query Rewrite Index.
     *
     * @param spatialIndexItems
     * @param srsURI
     * @param queryRewriteIndex
     */
    public SpatialIndex(Collection<SpatialIndexItem> spatialIndexItems, String srsURI, QueryRewriteIndex queryRewriteIndex) {
        int indexCapacity = spatialIndexItems.isEmpty() ? 1 : spatialIndexItems.size();
        this.strTree = new STRtree(indexCapacity);
        insertItems(spatialIndexItems);
        this.strTree.build();
        this.isBuilt = true;
        this.srsInfo = SRSRegistry.getSRSInfo(srsURI);
        this.queryRewriteIndex = queryRewriteIndex;
    }

    /**
     * Built Spatial Index with provided items and default Query Rewrite Index.
     *
     * @param spatialIndexItems
     * @param srsURI
     */
    public SpatialIndex(Collection<SpatialIndexItem> spatialIndexItems, String srsURI) {
        this(spatialIndexItems, srsURI, QueryRewriteIndex.createDefault());
    }

    /**
     * Built Spatial Index with provided capacity and provided Query Rewrite
     * Index parameters.
     *
     * @param spatialIndexItems
     * @param srsURI
     * @param queryRewriteLabel
     * @param maxSize
     * @param expiryInterval
     */
    public SpatialIndex(Collection<SpatialIndexItem> spatialIndexItems, String srsURI, String queryRewriteLabel, int maxSize, long expiryInterval) {
        this(spatialIndexItems, srsURI, new QueryRewriteIndex(queryRewriteLabel, maxSize, expiryInterval));
    }

    /**
     *
     * @return Information about the SRS used by the SpatialIndex.
     */
    public SRSInfo getSrsInfo() {
        return srsInfo;
    }

    /**
     *
     * @return True if the SpatialIndex is empty.
     */
    public boolean isEmpty() {
        return strTree.isEmpty();
    }

    /**
     *
     * @return True if the SpatialIndex has been built.
     */
    public boolean isBuilt() {
        return isBuilt;
    }

    /**
     *
     * @return QueryRewriteIndex stored with the SpatialIndex.
     */
    public QueryRewriteIndex getQueryRewriteIndex() {
        return queryRewriteIndex;
    }

    /**
     * Build the Spatial Index. No more items can be added.
     */
    public void build() {
        if (!isBuilt) {
            strTree.build();
            isBuilt = true;
        }
    }

    /**
     * Items to add to an unbuilt Spatial Index.
     *
     * @param indexItems
     */
    public final void insertItems(Collection<SpatialIndexItem> indexItems) {

        for (SpatialIndexItem indexItem : indexItems) {
            insertItem(indexItem.getEnvelope(), indexItem.getItem());
        }
    }

    /**
     * Item to add to an unbuilt Spatial Index.
     *
     * @param envelope
     * @param item
     */
    public final void insertItem(Envelope envelope, Resource item) {
        if (!isBuilt) {
            strTree.insert(envelope, item);
        } else {
            throw new SpatialIndexException("SpatialIndex has been built and cannot have additional items.");
        }
    }

    @SuppressWarnings("unchecked")
    public HashSet<Resource> query(Envelope searchEnvelope) {
        if (!strTree.isEmpty()) {
            return new HashSet<>(strTree.query(searchEnvelope));
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public String toString() {
        return "SpatialIndex{" + "srsInfo=" + srsInfo + ", isBuilt=" + isBuilt + ", strTree=" + strTree + ", queryRewriteIndex=" + queryRewriteIndex + '}';
    }

    public static final SpatialIndex retrieve(ExecutionContext execCxt) throws SpatialIndexException {

        Context context = execCxt.getContext();
        SpatialIndex spatialIndex = (SpatialIndex) context.get(SPATIAL_INDEX_SYMBOL, null);

        if (spatialIndex == null) {
            throw new SpatialIndexException("Dataset Context does not contain SpatialIndex.");
        }

        return spatialIndex;
    }

    /**
     *
     * @param execCxt
     * @return True if a SpatialIndex is defined in the ExecutionContext.
     */
    public static final boolean isDefined(ExecutionContext execCxt) {
        Context context = execCxt.getContext();
        return context.isDefined(SPATIAL_INDEX_SYMBOL);
    }

    /**
     * Set the SpatialIndex into the Context of the Dataset for later retrieval
     * and use in spatial functions.
     *
     * @param dataset
     * @param spatialIndex
     */
    public static final void setSpatialIndex(Dataset dataset, SpatialIndex spatialIndex) {
        Context context = dataset.getContext();
        context.set(SPATIAL_INDEX_SYMBOL, spatialIndex);
    }

    /**
     * Build Spatial Index from all graphs in Dataset.<br>
     * Dataset contains SpatialIndex in Context.<br>
     * Spatial Index written to file.
     *
     * @param dataset
     * @param srsURI
     * @param spatialIndexFile
     * @return SpatialIndex constructed.
     */
    public static SpatialIndex buildSpatialIndex(Dataset dataset, String srsURI, File spatialIndexFile) {

        SpatialIndex spatialIndex = load(spatialIndexFile);

        if (spatialIndex.isEmpty()) {
            spatialIndex = buildSpatialIndex(dataset, srsURI);
            save(spatialIndexFile, spatialIndex);
        }

        setSpatialIndex(dataset, spatialIndex);
        return spatialIndex;
    }

    /**
     * Build Spatial Index from all graphs in Dataset.<br>
     * Dataset contains SpatialIndex in Context.<br>
     * SRS URI based on most frequent found in Dataset.<br>
     * Spatial Index written to file.
     *
     * @param dataset
     * @param spatialIndexFile
     * @return SpatialIndex constructed.
     */
    public static SpatialIndex buildSpatialIndex(Dataset dataset, File spatialIndexFile) {
        String srsURI = GeoSPARQLOperations.findModeSRS(dataset);
        SpatialIndex spatialIndex = buildSpatialIndex(dataset, srsURI, spatialIndexFile);
        return spatialIndex;
    }

    /**
     * Build Spatial Index from all graphs in Dataset.<br>
     * Dataset contains SpatialIndex in Context.
     *
     * @param dataset
     * @param srsURI
     * @return SpatialIndex constructed.
     */
    public static SpatialIndex buildSpatialIndex(Dataset dataset, String srsURI) {
        LOGGER.info("Building Spatial Index - Started");

        //Default Model
        dataset.begin(ReadWrite.READ);
        Model defaultModel = dataset.getDefaultModel();
        Collection<SpatialIndexItem> items = getSpatialIndexItems(defaultModel, srsURI);

        //Named Models
        Iterator<String> graphNames = dataset.listNames();
        while (graphNames.hasNext()) {
            String graphName = graphNames.next();
            Model namedModel = dataset.getNamedModel(graphName);
            Collection<SpatialIndexItem> graphItems = getSpatialIndexItems(namedModel, srsURI);
            items.addAll(graphItems);
        }

        LOGGER.info("Building Spatial Index - Completed");
        dataset.end();
        SpatialIndex spatialIndex = new SpatialIndex(items, srsURI);
        spatialIndex.build();
        setSpatialIndex(dataset, spatialIndex);

        return spatialIndex;
    }

    /**
     * Build Spatial Index from all graphs in Dataset.<br>
     * Dataset contains SpatialIndex in Context.<br>
     * SRS URI based on most frequent found in Dataset.
     *
     * @param dataset
     * @return SpatialIndex constructed.
     */
    public static SpatialIndex buildSpatialIndex(Dataset dataset) {
        String srsURI = GeoSPARQLOperations.findModeSRS(dataset);
        SpatialIndex spatialIndex = buildSpatialIndex(dataset, srsURI);
        return spatialIndex;
    }

    /**
     * Wrap Model in a Dataset and build SpatialIndex.
     *
     * @param model
     * @param srsURI
     * @return Dataset with default Model and SpatialIndex in Context.
     */
    public static final Dataset wrapModel(Model model, String srsURI) {

        Dataset dataset = DatasetFactory.createTxnMem();
        dataset.setDefaultModel(model);
        buildSpatialIndex(dataset, srsURI);

        return dataset;
    }

    /**
     * Find items from the Model transformed to the SRS URI.
     *
     * @param model
     * @param srsURI
     * @return Items found in the Model in the SRS URI.
     */
    public static final Collection<SpatialIndexItem> getSpatialIndexItems(Model model, String srsURI) {

        List<SpatialIndexItem> items = new ArrayList<>();

        //Only add one set of statements as a converted dataset will duplicate the same info.
        if (model.contains(null, Geo.HAS_GEOMETRY_PROP, (Resource) null)) {
            LOGGER.info("Feature-hasGeometry-Geometry statements found. Any Geo predicates will not be added to index.");
            Collection<SpatialIndexItem> geometryLiteralItems = getGeometryLiteralIndexItems(model, srsURI);
            items.addAll(geometryLiteralItems);
        } else if (model.contains(null, SpatialExtension.GEO_LAT_PROP, (Literal) null)) {
            LOGGER.info("Geo predicate statements found.");
            Collection<SpatialIndexItem> geoPredicateItems = buildGeoPredicateIndex(model, srsURI);
            items.addAll(geoPredicateItems);
        }

        return items;
    }

    private static Collection<SpatialIndexItem> getGeometryLiteralIndexItems(Model model, String srsURI) {
        List<SpatialIndexItem> items = new ArrayList<>();
        StmtIterator stmtIt = model.listStatements(null, Geo.HAS_GEOMETRY_PROP, (Resource) null);
        while (stmtIt.hasNext()) {
            Statement stmt = stmtIt.nextStatement();

            Resource feature = stmt.getSubject();
            Resource geometry = stmt.getResource();

            NodeIterator nodeIt = model.listObjectsOfProperty(geometry, Geo.HAS_SERIALIZATION_PROP);
            while (nodeIt.hasNext()) {
                Literal geometryLiteral = nodeIt.nextNode().asLiteral();
                GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometryLiteral);

                try {
                    //Ensure all entries in the target SRS URI.
                    GeometryWrapper transformedGeometryWrapper = geometryWrapper.convertSRS(srsURI);

                    Envelope envelope = transformedGeometryWrapper.getEnvelope();
                    SpatialIndexItem item = new SpatialIndexItem(envelope, feature);
                    items.add(item);
                } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                    throw new SpatialIndexException("Transformation Exception: " + geometryLiteral + ". " + ex.getMessage());
                }

            }
        }
        return items;
    }

//TODO - force conversion to GeometryLiteral. These won't get picked up in the query search.
    private static Collection<SpatialIndexItem> buildGeoPredicateIndex(Model model, String srsURI) {
        List<SpatialIndexItem> items = new ArrayList<>();
        ResIterator resIt = model.listResourcesWithProperty(SpatialExtension.GEO_LAT_PROP);

        while (resIt.hasNext()) {
            Resource feature = resIt.nextResource();

            Literal lat = feature.getProperty(SpatialExtension.GEO_LAT_PROP).getLiteral();
            Literal lon = feature.getProperty(SpatialExtension.GEO_LON_PROP).getLiteral();

            Literal latLonPoint = ConvertLatLon.toLiteral(lat.getFloat(), lon.getFloat());
            GeometryWrapper geometryWrapper = GeometryWrapper.extract(latLonPoint);

            try {
                //Ensure all entries in the target SRS URI.
                GeometryWrapper transformedGeometryWrapper = geometryWrapper.convertSRS(srsURI);

                Envelope envelope = transformedGeometryWrapper.getEnvelope();
                SpatialIndexItem item = new SpatialIndexItem(envelope, feature);
                items.add(item);
            } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                throw new SpatialIndexException("Transformation Exception: " + geometryWrapper.getLexicalForm() + ". " + ex.getMessage());
            }
        }
        return items;
    }

    /**
     * Load a SpatialIndex from file.
     *
     * @param spatialIndexFile
     * @return
     * @throws SpatialIndexException
     */
    public static final SpatialIndex load(File spatialIndexFile) throws SpatialIndexException {

        if (spatialIndexFile != null && spatialIndexFile.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(spatialIndexFile))) {
                SpatialIndex spatialIndex = (SpatialIndex) in.readObject();
                spatialIndex.build();
                return spatialIndex;
            } catch (ClassNotFoundException | IOException ex) {
                throw new SpatialIndexException("Loading Exception: " + ex.getMessage(), ex);
            }
        } else {
            throw new SpatialIndexException("File is null or does not exist.");
        }
    }

    /**
     * Save SpatialIndex to file.
     *
     * @param spatialIndexFile
     * @param spatialIndex
     */
    public static final void save(File spatialIndexFile, SpatialIndex spatialIndex) {

        if (spatialIndexFile != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(spatialIndexFile))) {
                out.writeObject(spatialIndex);
            } catch (Exception ex) {
                throw new SpatialIndexException("Save Exception: " + ex.getMessage());
            }
        }
    }

}
