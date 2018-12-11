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
package io.github.galbiston.geosparql_jena.implementation.vocabulary;

import static io.github.galbiston.geosparql_jena.implementation.vocabulary.GeoSPARQL_URI.GEO_POS_URI;
import static io.github.galbiston.geosparql_jena.implementation.vocabulary.GeoSPARQL_URI.SPATIAL_FUNCTION_URI;
import static io.github.galbiston.geosparql_jena.implementation.vocabulary.GeoSPARQL_URI.SPATIAL_URI;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 *
 */
public class SpatialExtension {

    //Filter Functions
    public static final String CONVERT_LAT_LON = SPATIAL_FUNCTION_URI + "convertLatLon";
    public static final String CONVERT_LAT_LON_BOX = SPATIAL_FUNCTION_URI + "convertLatLonBox";
    public static final String NEARBY = SPATIAL_FUNCTION_URI + "nearby";
    public static final String WITHIN_CIRCLE = SPATIAL_FUNCTION_URI + "withinCircle";

    //Property Functions
    public static final String NEARBY_PROP = SPATIAL_URI + "nearby";
    public static final String WITHIN_CIRCLE_PROP = SPATIAL_URI + "withinCircle";
    public static final String WITHIN_BOX_PROP = SPATIAL_URI + "withinBox";
    public static final String INTERSECT_BOX_PROP = SPATIAL_URI + "intersectBox";
    public static final String NEARBY_GEOM_PROP = SPATIAL_URI + "nearbyGeom";
    public static final String WITHIN_CIRCLE_GEOM_PROP = SPATIAL_URI + "withinCircleGeom";
    public static final String WITHIN_BOX_GEOM_PROP = SPATIAL_URI + "withinBoxGeom";
    public static final String INTERSECT_BOX_GEOM_PROP = SPATIAL_URI + "intersectBoxGeom";

    //Lat/Lon
    public static final String GEO_POS_LAT = GEO_POS_URI + "lat";
    public static final String GEO_POS_LONG = GEO_POS_URI + "long";
    public static final Property GEO_LAT_PROP = ResourceFactory.createProperty(GEO_POS_LAT);
    public static final Property GEO_LONG_PROP = ResourceFactory.createProperty(GEO_POS_LONG);
}
