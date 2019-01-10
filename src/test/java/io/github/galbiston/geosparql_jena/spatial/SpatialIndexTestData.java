/*
 * Copyright 2019 .
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
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 *
 */
public class SpatialIndexTestData {

    public static final Resource LONDON_FEATURE = ResourceFactory.createResource("http://example.org/Feature#London");
    public static final Resource LONDON_GEOMETRY = ResourceFactory.createResource("http://example.org/Geometry#London");
    public static final GeometryWrapper LONDON_GEOMETRY_LITERAL = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(51.489767 -0.088180)", WKTDatatype.URI);

    public static final Resource NEW_YORK_FEATURE = ResourceFactory.createResource("http://example.org/Feature#NewYork");
    public static final Resource NEW_YORK_GEOMETRY = ResourceFactory.createResource("http://example.org/Geometry#NewYork");
    public static final GeometryWrapper NEW_YORK_GEOMETRY_LITERAL = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(40.697150 -73.979635)", WKTDatatype.URI);

    public static final Resource HONOLULU_FEATURE = ResourceFactory.createResource("http://example.org/Feature#Honolulu");
    public static final Resource HONOLULU_GEOMETRY = ResourceFactory.createResource("http://example.org/Geometry#Honolulu");
    public static final GeometryWrapper HONOLULU_GEOMETRY_LITERAL = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(21.325532 -157.801363)", WKTDatatype.URI);

    public static final Resource PERTH_FEATURE = ResourceFactory.createResource("http://example.org/Feature#Perth");
    public static final Resource PERTH_GEOMETRY = ResourceFactory.createResource("http://example.org/Geometry#Perth");
    public static final GeometryWrapper PERTH_GEOMETRY_LITERAL = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(-31.952712 115.860480)", WKTDatatype.URI);

    public static final Resource AUCKLAND_FEATURE = ResourceFactory.createResource("http://example.org/Feature#Auckland");
    public static final Resource AUCKLAND_GEOMETRY = ResourceFactory.createResource("http://example.org/Geometry#Auckland");
    public static final GeometryWrapper AUCKLAND_GEOMETRY_LITERAL = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(-36.854760 174.763470)", WKTDatatype.URI);

    public static final GeometryWrapper PARIS_GEOMETRY_LITERAL = GeometryWrapper.extract("<http://www.opengis.net/def/crs/EPSG/0/4326> POINT(48.857487 2.373047)", WKTDatatype.URI);

    private static SpatialIndex TEST_SPATIAL_INDEX = null;
    private static Dataset TEST_DATASET = null;

    public static final SpatialIndex createTestIndex() {

        if (TEST_SPATIAL_INDEX == null) {
            SpatialIndex spatialIndex = new SpatialIndex(100);
            spatialIndex.insertItem(LONDON_GEOMETRY_LITERAL.getEnvelope(), LONDON_FEATURE);
            spatialIndex.insertItem(NEW_YORK_GEOMETRY_LITERAL.getEnvelope(), NEW_YORK_FEATURE);
            spatialIndex.insertItem(HONOLULU_GEOMETRY_LITERAL.getEnvelope(), HONOLULU_FEATURE);
            spatialIndex.insertItem(PERTH_GEOMETRY_LITERAL.getEnvelope(), PERTH_FEATURE);
            spatialIndex.insertItem(AUCKLAND_GEOMETRY_LITERAL.getEnvelope(), AUCKLAND_FEATURE);

            spatialIndex.build();
            TEST_SPATIAL_INDEX = spatialIndex;
        }

        return TEST_SPATIAL_INDEX;
    }

    public static final Dataset createTestDataset() {

        if (TEST_DATASET == null) {
            Dataset dataset = DatasetFactory.createTxnMem();
            Model model = ModelFactory.createDefaultModel();
            model.add(LONDON_FEATURE, Geo.HAS_GEOMETRY_PROP, LONDON_GEOMETRY);
            model.add(NEW_YORK_FEATURE, Geo.HAS_GEOMETRY_PROP, NEW_YORK_GEOMETRY);
            model.add(HONOLULU_FEATURE, Geo.HAS_GEOMETRY_PROP, HONOLULU_GEOMETRY);
            model.add(PERTH_FEATURE, Geo.HAS_GEOMETRY_PROP, PERTH_GEOMETRY);
            model.add(AUCKLAND_FEATURE, Geo.HAS_GEOMETRY_PROP, AUCKLAND_GEOMETRY);
            model.add(LONDON_GEOMETRY, Geo.HAS_SERIALIZATION_PROP, LONDON_GEOMETRY_LITERAL.asLiteral());
            model.add(NEW_YORK_GEOMETRY, Geo.HAS_SERIALIZATION_PROP, NEW_YORK_GEOMETRY_LITERAL.asLiteral());
            model.add(HONOLULU_GEOMETRY, Geo.HAS_SERIALIZATION_PROP, HONOLULU_GEOMETRY_LITERAL.asLiteral());
            model.add(PERTH_GEOMETRY, Geo.HAS_SERIALIZATION_PROP, PERTH_GEOMETRY_LITERAL.asLiteral());
            model.add(AUCKLAND_GEOMETRY, Geo.HAS_SERIALIZATION_PROP, AUCKLAND_GEOMETRY_LITERAL.asLiteral());

            dataset.setDefaultModel(model);
            SpatialIndex spatialIndex = createTestIndex();
            SpatialIndex.setSpatialIndex(dataset, spatialIndex);
            TEST_DATASET = dataset;
        }

        return TEST_DATASET;
    }

}
