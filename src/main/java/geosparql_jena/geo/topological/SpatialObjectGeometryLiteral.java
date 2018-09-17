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
package geosparql_jena.geo.topological;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;

/**
 *
 *
 */
public class SpatialObjectGeometryLiteral {

    private final Resource spatialObject;
    private final Literal geometryLiteral;

    public SpatialObjectGeometryLiteral(Resource spatialObject, Literal geometryLiteral) {
        this.spatialObject = spatialObject;
        this.geometryLiteral = geometryLiteral;
    }

    public Resource getSpatialObject() {
        return spatialObject;
    }

    public Literal getGeometryLiteral() {
        return geometryLiteral;
    }

    @Override
    public String toString() {
        return "SpatialObjectGeometryLiteral{" + "spatialObject=" + spatialObject + ", geometryLiteral=" + geometryLiteral + '}';
    }

}
