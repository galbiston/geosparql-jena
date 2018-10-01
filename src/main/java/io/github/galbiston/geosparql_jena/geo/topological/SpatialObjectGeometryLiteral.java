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
package io.github.galbiston.geosparql_jena.geo.topological;

import java.util.Objects;
import org.apache.jena.graph.Node;

/**
 *
 *
 */
public class SpatialObjectGeometryLiteral {

    private final Node spatialObject;
    private final Node geometryLiteral;

    public SpatialObjectGeometryLiteral(Node spatialObject, Node geometryLiteral) {
        this.spatialObject = spatialObject;
        this.geometryLiteral = geometryLiteral;
    }

    public Node getSpatialObject() {
        return spatialObject;
    }

    public Node getGeometryLiteral() {
        return geometryLiteral;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.spatialObject);
        hash = 73 * hash + Objects.hashCode(this.geometryLiteral);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpatialObjectGeometryLiteral other = (SpatialObjectGeometryLiteral) obj;
        if (!Objects.equals(this.spatialObject, other.spatialObject)) {
            return false;
        }
        return Objects.equals(this.geometryLiteral, other.geometryLiteral);
    }

    @Override
    public String toString() {
        return "SpatialObjectGeometryLiteral{" + "spatialObject=" + spatialObject + ", geometryLiteral=" + geometryLiteral + '}';
    }

}
