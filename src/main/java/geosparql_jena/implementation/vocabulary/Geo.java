/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geosparql_jena.implementation.vocabulary;

import static geosparql_jena.implementation.vocabulary.GeoSPARQL_URI.GEO_URI;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 *
 *
 */
public interface Geo {

    public static final Property HAS_SERIALIZATION_PROP = ResourceFactory.createProperty(GEO_URI + "hasSerialization");
    public static final Node HAS_SERIALIZATION_NODE = HAS_SERIALIZATION_PROP.asNode();

    public static final Property AS_WKT_PROP = ResourceFactory.createProperty(GEO_URI + "asWKT");
    public static final Property AS_GML_PROP = ResourceFactory.createProperty(GEO_URI + "asGML");

    public static final Property HAS_DEFAULT_GEOMETRY_PROP = ResourceFactory.createProperty(GEO_URI + "hasDefaultGeometry");
    public static final Node HAS_DEFAULT_GEOMETRY_NODE = HAS_DEFAULT_GEOMETRY_PROP.asNode();

    public static final Property HAS_GEOMETRY_PROP = ResourceFactory.createProperty(GEO_URI + "hasGeometry");
    public static final Node HAS_GEOMETRY_NODE = HAS_GEOMETRY_PROP.asNode();

    public static final Resource GEOMETRY_RES = ResourceFactory.createResource(GEO_URI + "Geometry");
    public static final Node GEOMETRY_NODE = GEOMETRY_RES.asNode();

    public static final Resource FEATURE_RES = ResourceFactory.createResource(GEO_URI + "Feature");
    public static final Node FEATURE_NODE = FEATURE_RES.asNode();

    public static final Resource SPATIAL_OBJECT_RES = ResourceFactory.createResource(GEO_URI + "SpatialObject");
    public static final Node SPATIAL_OBJECT_NODE = SPATIAL_OBJECT_RES.asNode();

    //Simple Feature Topological function names:
    public static final String SF_CONTAINS_NAME = GEO_URI + "sfContains";
    public static final String SF_INTERSECTS_NAME = GEO_URI + "sfIntersects";
    public static final String SF_EQUALS_NAME = GEO_URI + "sfEquals";

    public static final String SF_TOUCHES_NAME = GEO_URI + "sfTouches";
    public static final String SF_DISJOINT_NAME = GEO_URI + "sfDisjoint";
    public static final String SF_OVERLAPS_NAME = GEO_URI + "sfOverlaps";
    public static final String SF_CROSSES_NAME = GEO_URI + "sfCrosses";
    public static final String SF_WITHIN_NAME = GEO_URI + "sfWithin";

    //Egenhofer Topological function names:
    public static final String EH_DISJOINT_NAME = GEO_URI + "ehDisjoint";
    public static final String EH_CONTAINS_NAME = GEO_URI + "ehContains";
    public static final String EH_COVERED_BY_NAME = GEO_URI + "ehCoveredBy";
    public static final String EH_EQUALS_NAME = GEO_URI + "ehEquals";
    public static final String EH_OVERLAP_NAME = GEO_URI + "ehOverlap";
    public static final String EH_COVERS_NAME = GEO_URI + "ehCovers";
    public static final String EH_INSIDE_NAME = GEO_URI + "ehInside";
    public static final String EH_MEET_NAME = GEO_URI + "ehMeet";

    //RCC8 Topological function names:
    public static final String RCC_DISCONNECTED_NAME = GEO_URI + "rcc8dc";
    public static final String RCC_NTANPROPERPART_NAME = GEO_URI + "rcc8ntpp";
    public static final String RCC_NTANPROPERPARTINVERSE_NAME = GEO_URI + "rcc8ntppi";
    public static final String RCC_TANPROPERPART_NAME = GEO_URI + "rcc8tpp";
    public static final String RCC_TANPROPERPARTINVERSE_NAME = GEO_URI + "rcc8tppi";
    public static final String RCC_EQUALS_NAME = GEO_URI + "rcc8eq";
    public static final String RCC_PARTIALLY_OVERLAPPING_NAME = GEO_URI + "rcc8po";
    public static final String RCC_EXTERNALLY_CONNECTED_NAME = GEO_URI + "rcc8ec";

    //Topological DE-9IM function: relate
    public static final String RELATE_NAME = GEO_URI + "relate";

    //Geometry Properties
    public static final String DIMENSION = GEO_URI + "dimension";
    public static final String COORDINATE_DIMENSION = GEO_URI + "coordinateDimension";
    public static final String SPATIAL_DIMENSION = GEO_URI + "spatialDimension";
    public static final String IS_EMPTY = GEO_URI + "isEmpty";
    public static final String IS_SIMPLE = GEO_URI + "isSimple";

    public static final Property DIMENSION_RES = ResourceFactory.createProperty(DIMENSION);
    public static final Node DIMENSION_NODE = DIMENSION_RES.asNode();
    public static final Property COORDINATE_DIMENSION_RES = ResourceFactory.createProperty(COORDINATE_DIMENSION);
    public static final Node COORDINATE_DIMENSION_NODE = COORDINATE_DIMENSION_RES.asNode();
    public static final Property SPATIAL_DIMENSION_RES = ResourceFactory.createProperty(SPATIAL_DIMENSION);
    public static final Node SPATIAL_DIMENSION_NODE = SPATIAL_DIMENSION_RES.asNode();
    public static final Property IS_EMPTY_RES = ResourceFactory.createProperty(IS_EMPTY);
    public static final Node IS_EMPTY_NODE = IS_EMPTY_RES.asNode();
    public static final Property IS_SIMPLE_RES = ResourceFactory.createProperty(IS_SIMPLE);
    public static final Node IS_SIMPLE_NODE = IS_SIMPLE_RES.asNode();

    //Geometry Literal Datatypes
    public static final String WKT = GEO_URI + "wktLiteral";
    public static final String GML = GEO_URI + "gmlLiteral";
}
