/*
 * Copyright 2019 the original author or authors.
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
package io.github.galbiston.geosparql_jena.geof.topological.filter_functions.simple_features;

import io.github.galbiston.geosparql_jena.geof.topological.GenericFilterFunction;
import io.github.galbiston.geosparql_jena.implementation.DimensionInfo;
import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

/**
 *
 *
 */
public class SfTouchesFF extends GenericFilterFunction {

    @Override
    protected boolean relate(GeometryWrapper sourceGeometry, GeometryWrapper targetGeometry) throws FactoryException, MismatchedDimensionException, TransformException {
        return sourceGeometry.touches(targetGeometry);
    }

    @Override
    public boolean isDisjoint() {
        return false;
    }

    @Override
    protected boolean permittedTopology(DimensionInfo sourceDimensionInfo, DimensionInfo targetDimensionInfo) {
        return !(sourceDimensionInfo.isPoint() && targetDimensionInfo.isPoint());
    }

    @Override
    public boolean isDisconnected() {
        return false;
    }
}
