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

import static io.github.galbiston.geosparql_jena.implementation.vocabulary.GeoSPARQL_URI.SPATIAL_URI;

/**
 *
 *
 */
public class SpatialProp {

    //Jena Spatial Extension
    public static final String NEARBY = SPATIAL_URI + "nearby";
    public static final String WITHIN_CIRCLE = SPATIAL_URI + "withinCircle";
    public static final String NEARBY_GEOM = SPATIAL_URI + "nearbyGeom";
    public static final String WITHIN_CIRCLE_GEOM = SPATIAL_URI + "withinCircleGeom";
}
