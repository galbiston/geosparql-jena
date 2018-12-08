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
package io.github.galbiston.geosparql_jena.implementation.datatype;

import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.index.GeometryLiteralIndex;
import io.github.galbiston.geosparql_jena.implementation.index.GeometryLiteralIndex.GeometryIndex;
import java.lang.invoke.MethodHandles;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public abstract class GeometryDatatype extends BaseDatatype implements DatatypeReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public GeometryDatatype(String uri) {
        super(uri);
    }

    /**
     * This method Parses the GML literal to the JTS Geometry
     *
     * @param lexicalForm - the GML literal to be parsed
     * @return geometry - if the GML literal is valid.
     * <br> empty geometry - if the GML literal is empty.
     * <br> null - if the GML literal is invalid.
     */
    @Override
    public final GeometryWrapper parse(String lexicalForm) throws DatatypeFormatException {
        return parse(lexicalForm, GeometryIndex.PRIMARY);
    }

    public final GeometryWrapper parse(String lexicalForm, GeometryIndex targetIndex) throws DatatypeFormatException {
        //Check the Geometry Literal Index to see if been previously read and cached.
        //DatatypeReader interface used to instruct index on how to obtain the GeometryWrapper.
        try {
            return GeometryLiteralIndex.retrieve(lexicalForm, this, targetIndex);
        } catch (ParseException | IllegalArgumentException ex) {
            LOGGER.error("{} - Illegal Geometry Literal: {} ", ex.getMessage(), lexicalForm);
            throw new DatatypeFormatException(ex.getMessage() + " - Illegal Geometry Literal: " + lexicalForm);
        }
    }

    private static final TypeMapper TYPE_MAPPER = TypeMapper.getInstance();
    private static boolean isDatatypesRegistered = false;

    public static final void registerDatatypes() {
        if (!isDatatypesRegistered) {
            TYPE_MAPPER.registerDatatype(WKTDatatype.INSTANCE);
            TYPE_MAPPER.registerDatatype(GMLDatatype.INSTANCE);
            isDatatypesRegistered = true;
        }
    }

    public static final GeometryDatatype get(RDFDatatype rdfDatatype) {
        if (rdfDatatype instanceof GeometryDatatype) {
            return (GeometryDatatype) rdfDatatype;
        } else {
            LOGGER.error("Unrecognised Geometry Datatype: {}. Ensure that Datatype is extending GeometryDatatype.", rdfDatatype.getURI());
            throw new DatatypeFormatException("Unrecognised Geometry Datatype: " + rdfDatatype.getURI());
        }
    }

    public static final GeometryDatatype get(String datatypeURI) {
        registerDatatypes();
        RDFDatatype rdfDatatype = TYPE_MAPPER.getTypeByName(datatypeURI);
        return GeometryDatatype.get(rdfDatatype);
    }

    public static final boolean checkURI(String datatypeURI) {
        registerDatatypes();
        RDFDatatype rdfDatatype = TYPE_MAPPER.getTypeByName(datatypeURI);
        if (rdfDatatype != null) {
            return check(rdfDatatype);
        } else {
            LOGGER.error("Unrecognised Datatype: {} - Ensure that GeoSPARQLSupport is enabled and Datatype has been registered.", datatypeURI);
            throw new NullPointerException("Datatype not found: " + datatypeURI);
        }
    }

    public static final boolean check(RDFDatatype rdfDatatype) {
        return rdfDatatype instanceof GeometryDatatype;
    }

}
