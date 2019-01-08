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
package io.github.galbiston.geosparql_jena.spatial.property_functions.cardinal;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.spatial.CardinalDirection;
import io.github.galbiston.geosparql_jena.spatial.SearchEnvelope;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndex;
import java.util.List;
import org.apache.jena.rdf.model.Resource;
import org.locationtech.jts.geom.Envelope;

/**
 *
 *
 */
public class WestPF extends GenericCardinalPropertyFunction {

    private Envelope wrapEnvelope;

    @Override
    protected Envelope buildSearchEnvelope(GeometryWrapper geometryWrapper) {
        Envelope searchEnvelope = SearchEnvelope.build(geometryWrapper, CardinalDirection.WEST);
        wrapEnvelope = SearchEnvelope.buildWrap(geometryWrapper, CardinalDirection.WEST);
        return searchEnvelope;
    }

    @Override
    protected List<Resource> testSearchEnvelope() {
        SpatialIndex spatialIndex = getSpatialIndex();
        List<Resource> features = spatialIndex.query(envelope);
        List<Resource> wrapFeatures = spatialIndex.query(wrapEnvelope);
        features.addAll(wrapFeatures);
        return features;
    }

}
