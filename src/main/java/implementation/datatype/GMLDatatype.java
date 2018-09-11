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
package implementation.datatype;

import implementation.DimensionInfo;
import implementation.GeometryWrapper;
import implementation.index.GeometryLiteralIndex;
import implementation.index.GeometryLiteralIndex.GeometryIndex;
import implementation.parsers.gml.GMLReader;
import implementation.parsers.gml.GMLWriter;
import implementation.vocabulary.Geo;
import java.io.IOException;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.jdom2.JDOMException;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 *
 */
public class GMLDatatype extends GeometryDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(GMLDatatype.class);

    /**
     * The default GML type URI.
     */
    public static final String URI = Geo.GML;

    /**
     * A static instance of GMLDatatype.
     */
    public static final GMLDatatype INSTANCE = new GMLDatatype();

    /**
     * XML element tag "gml" is defined for the convenience of GML generation.
     */
    public static final String GML_PREFIX = "gml";

    /**
     * private constructor - single global instance.
     */
    public GMLDatatype() {
        super(URI);
    }

    /**
     * This method Un-parses the JTS Geometry to the GML literal
     *
     * @param geometry - the JTS Geometry to be un-parsed
     * @return GML - the returned GML Literal.
     * <br> Notice that the Spatial Reference System
     * "urn:ogc:def:crs:OGC::CRS84" is predefined in the returned GML literal.
     */
    @Override
    public String unparse(Object geometry) {
        if (geometry instanceof GeometryWrapper) {
            GeometryWrapper geometryWrapper = (GeometryWrapper) geometry;
            return GMLWriter.write(geometryWrapper);
        } else {
            throw new AssertionError("Object passed to GMLDatatype is not a GeometryWrapper: " + geometry);
        }
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
    public GeometryWrapper parse(String lexicalForm) throws DatatypeFormatException {
        return parse(lexicalForm, GeometryIndex.PRIMARY);
    }

    @Override
    public GeometryWrapper parse(String lexicalForm, GeometryIndex targetIndex) throws DatatypeFormatException {
        //Check the Geometry Literal Index to see if been previously read and cached.
        //DatatypeReader interface used to instruct index on how to obtain the GeometryWrapper.
        try {
            return GeometryLiteralIndex.retrieve(lexicalForm, this, targetIndex);
        } catch (ParseException | IllegalArgumentException ex) {
            LOGGER.error("{} - Illegal WKT literal: {} ", ex.getMessage(), lexicalForm);
            throw new DatatypeFormatException(ex.getMessage() + " - Illegal WKT literal: " + lexicalForm);
        }
    }

    @Override
    public GeometryWrapper read(String geometryLiteral) {
        try {
            GMLReader gmlReader = GMLReader.extract(geometryLiteral);
            Geometry geometry = gmlReader.getGeometry();
            DimensionInfo dimensionInfo = gmlReader.getDimensionInfo();

            return new GeometryWrapper(geometry, gmlReader.getSrsName(), URI, dimensionInfo, geometryLiteral);
        } catch (JDOMException | IOException ex) {
            LOGGER.error("{} - Illegal GML literal: {} ", ex.getMessage(), geometryLiteral);
            throw new DatatypeFormatException("Illegal GML literal:" + geometryLiteral);
        }
    }

    @Override
    public String toString() {
        return "GMLDatatype{" + URI + '}';
    }

}
