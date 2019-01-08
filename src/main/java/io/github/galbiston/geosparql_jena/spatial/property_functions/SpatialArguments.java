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
package io.github.galbiston.geosparql_jena.spatial.property_functions;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import java.util.Objects;
import org.locationtech.jts.geom.Envelope;

/**
 *
 *
 */
public class SpatialArguments {

    protected final int limit;
    protected final GeometryWrapper geometryWrapper;
    protected final Envelope envelope;

    public SpatialArguments(int limit, GeometryWrapper geometryWrapper, Envelope envelope) {
        this.limit = limit;
        this.geometryWrapper = geometryWrapper;
        this.envelope = envelope;
    }

    public int getLimit() {
        return limit;
    }

    public GeometryWrapper getGeometryWrapper() {
        return geometryWrapper;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    @Override
    public String toString() {
        return "SpatialArguments{" + "limit=" + limit + ", geometryWrapper=" + geometryWrapper + ", envelope=" + envelope + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.limit;
        hash = 43 * hash + Objects.hashCode(this.geometryWrapper);
        hash = 43 * hash + Objects.hashCode(this.envelope);
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
        final SpatialArguments other = (SpatialArguments) obj;
        if (this.limit != other.limit) {
            return false;
        }
        if (!Objects.equals(this.geometryWrapper, other.geometryWrapper)) {
            return false;
        }
        return Objects.equals(this.envelope, other.envelope);
    }

}
