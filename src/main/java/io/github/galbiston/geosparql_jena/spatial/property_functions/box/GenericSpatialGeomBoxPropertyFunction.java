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
package io.github.galbiston.geosparql_jena.spatial.property_functions.box;

import io.github.galbiston.geosparql_jena.implementation.CRSInfo;
import io.github.galbiston.geosparql_jena.spatial.property_functions.GenericSpatialGeomPropertyFunction;
import io.github.galbiston.geosparql_jena.spatial.property_functions.SpatialArguments;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.pfunction.PropFuncArg;

/**
 *
 *
 */
public abstract class GenericSpatialGeomBoxPropertyFunction extends GenericSpatialGeomPropertyFunction {

    //This is to simplify testing process by providing access to protected method for unit tests.
    @Override
    protected SpatialArguments extractObjectArguments(Node predicate, PropFuncArg object, CRSInfo indexCRSInfo) {
        return super.extractObjectArguments(predicate, object, indexCRSInfo);
    }
}
