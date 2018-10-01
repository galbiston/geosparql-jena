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
package io.github.galbiston.geosparql_jena.implementation.data_conversion;

import io.github.galbiston.geosparql_jena.implementation.datatype.GMLDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.GeometryDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import rdf_tables.datatypes.DatatypeController;

/**
 *
 *
 */
public class CSVConversion {

    public static final void registerDatatypes() {
        DatatypeController.addPrefixDatatype("wkt", WKTDatatype.INSTANCE);
        DatatypeController.addPrefixDatatype("gml", GMLDatatype.INSTANCE);
        GeometryDatatype.registerDatatypes();
    }

}
