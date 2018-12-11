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

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SpatialExtension;
import io.github.galbiston.geosparql_jena.spatial.filter_functions.ConvertLatLonFF;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.strtree.STRtree;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class SpatialIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static org.locationtech.jts.index.SpatialIndex SPATIAL_INDEX;

    public static void intialiseSpatialIndex(int capacity) {
        SPATIAL_INDEX = new STRtree(capacity);
    }

    public static void intialiseSpatialIndex(Collection<SpatialIndexItem> spatialIndexItems) {
        SPATIAL_INDEX = new STRtree(spatialIndexItems.size());
        insertItems(spatialIndexItems);
    }

    public static void insertItems(Collection<SpatialIndexItem> indexItems) {

        for (SpatialIndexItem indexItem : indexItems) {
            insertItem(indexItem.getEnvelope(), indexItem.getItem());
        }

    }

    public static void insertItem(Envelope envelope, Resource item) {
        SPATIAL_INDEX.insert(envelope, item);
    }

    @SuppressWarnings("unchecked")
    public static List<Resource> query(Envelope searchEnvelope) {
        return SPATIAL_INDEX.query(searchEnvelope);
    }

    public static void buildSpatialIndex(Dataset dataset, File spatialIndexFile) {

        boolean isLoaded = load(spatialIndexFile);

        if (!isLoaded) {
            buildSpatialIndex(dataset);
            save(spatialIndexFile);
        }

    }

    public static void buildSpatialIndex(Dataset dataset) {
        LOGGER.info("Building Spatial Index - Started");

        //Default Model
        dataset.begin(ReadWrite.READ);
        Model defaultModel = dataset.getDefaultModel();
        Collection<SpatialIndexItem> items = buildSpatialIndex(defaultModel);

        //Named Models
        Iterator<String> graphNames = dataset.listNames();
        while (graphNames.hasNext()) {
            String graphName = graphNames.next();
            Model namedModel = dataset.getNamedModel(graphName);
            Collection<SpatialIndexItem> graphItems = buildSpatialIndex(namedModel);
            items.addAll(graphItems);
        }

        intialiseSpatialIndex(items);

        LOGGER.info("Building Spatial Index - Completed");
        dataset.end();
    }

    public static final Collection<SpatialIndexItem> buildSpatialIndex(Model model) {

        List<SpatialIndexItem> items = new ArrayList<>();

        //Only add one set of statements as a converted dataset will duplicate the same info.
        if (model.contains(null, Geo.HAS_GEOMETRY_PROP, (Resource) null)) {
            LOGGER.info("Feature-hasGeometry-Geometry statements found. Any Geo predicates will not be added to index.");
            Collection<SpatialIndexItem> geometryLiteralItems = buildGeometryLiteralIndex(model);
            items.addAll(geometryLiteralItems);
        } else if (model.contains(null, SpatialExtension.GEO_LAT_PROP, (Literal) null)) {
            LOGGER.info("Geo predicate statements found.");
            Collection<SpatialIndexItem> geoPredicateItems = buildGeoPredicateIndex(model);
            items.addAll(geoPredicateItems);
        }

        return items;
    }

    private static Collection<SpatialIndexItem> buildGeometryLiteralIndex(Model model) {
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
                    //Ensure all entries in the index are WGS84 SRS.
                    GeometryWrapper transformedGeometryWrapper = geometryWrapper.convertCRS(SRS_URI.WGS84_CRS);

                    Envelope envelope = transformedGeometryWrapper.getEnvelope();
                    SpatialIndexItem item = new SpatialIndexItem(envelope, feature);
                    items.add(item);
                } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
                    LOGGER.error("Transformation Exception: {}, {}", geometryLiteral, ex.getMessage());
                }

            }
        }
        return items;
    }

    private static Collection<SpatialIndexItem> buildGeoPredicateIndex(Model model) {
        List<SpatialIndexItem> items = new ArrayList<>();
        ResIterator resIt = model.listResourcesWithProperty(SpatialExtension.GEO_LAT_PROP);

        while (resIt.hasNext()) {
            Resource feature = resIt.nextResource();

            Literal lat = feature.getProperty(SpatialExtension.GEO_LAT_PROP).getLiteral();
            Literal lon = feature.getProperty(SpatialExtension.GEO_LONG_PROP).getLiteral();

            Literal latLonPoint = ConvertLatLonFF.toLiteral(lat.getFloat(), lon.getFloat());
            GeometryWrapper geometryWrapper = GeometryWrapper.extract(latLonPoint);

            Envelope envelope = geometryWrapper.getEnvelope();
            SpatialIndexItem item = new SpatialIndexItem(envelope, feature);
            items.add(item);
        }
        return items;
    }

    public static final boolean load(File spatialIndexFile) {

        boolean isLoaded = false;
        if (spatialIndexFile != null && spatialIndexFile.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(spatialIndexFile))) {
                SPATIAL_INDEX = (STRtree) in.readObject();
            } catch (Exception ex) {
                LOGGER.error("Spatial Index Load Exception: {}", ex.getMessage());
            }
            isLoaded = true;
        }

        return isLoaded;
    }

    public static final void save(File spatialIndexFile) {

        if (spatialIndexFile != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(spatialIndexFile))) {
                out.writeObject(SPATIAL_INDEX);
            } catch (Exception ex) {
                LOGGER.error("Spatial Index Save Exception: {}", ex.getMessage());
            }
        }
    }

}
