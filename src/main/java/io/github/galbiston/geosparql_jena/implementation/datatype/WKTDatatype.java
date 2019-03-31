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

import io.github.galbiston.geosparql_jena.implementation.DimensionInfo;
import io.github.galbiston.geosparql_jena.implementation.GeometryWrapper;
import io.github.galbiston.geosparql_jena.implementation.parsers.wkt.WKTReader;
import io.github.galbiston.geosparql_jena.implementation.parsers.wkt.WKTWriter;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.Geo;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WKTDatatype class allows the URI "geo:wktLiteral" to be used as a datatype
 * and it will parse that datatype to a JTS Geometry.
 *
 * Req 10 All RDFS Literals of type geo:wktLiteral shall consist of an optional
 * URI identifying the coordinate reference system followed by Simple Features
 * Well Known Text (WKT) describing a geometric value. Valid geo:wktLiterals are
 * formed by concatenating a valid, absolute URI as defined in [RFC 2396], one
 * or more spaces (Unicode U+0020 character) as a separator, and a WKT string as
 * defined in Simple Features [ISO 19125-1].
 *
 * Req 11 The URI {@code <http://www.opengis.net/def/crs/OGC/1.3/CRS84>} shall
 * be assumed as the spatial reference system for geo:wktLiterals that do not *
 * specify an explicit spatial reference system URI.
 */
public class WKTDatatype extends GeometryDatatype {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKTDatatype.class);

    /**
     * The default WKT type URI.
     */
    public static final String URI = Geo.WKT;

    /**
     * A static instance of WKTDatatype.
     */
    public static final WKTDatatype INSTANCE = new WKTDatatype();

    /**
     * private constructor - single global instance.
     */
    private WKTDatatype() {
        super(URI);
    }

    /**
     * This method Un-parses the JTS Geometry to the WKT literal
     *
     * @param geometry - the JTS Geometry to be un-parsed
     * @return WKT - the returned WKT Literal.
     * <br> Notice that the Spatial Reference System is not specified in
     * returned WKT literal.
     *
     */
    @Override
    public String unparse(Object geometry) {

        if (geometry instanceof GeometryWrapper) {
            GeometryWrapper geometryWrapper = (GeometryWrapper) geometry;
            return WKTWriter.write(geometryWrapper);
        } else {
            throw new DatatypeFormatException("Object to unparse WKTDatatype is not a GeometryWrapper: " + geometry);
        }
    }

    @Override
    public GeometryWrapper read(String geometryLiteral) {
        WKTTextSRS wktTextSRS = new WKTTextSRS(geometryLiteral);

        WKTReader wktReader = WKTReader.extract(wktTextSRS.getWktText());

        Geometry geometry = wktReader.getGeometry();
        DimensionInfo dimensionInfo = wktReader.getDimensionInfo();

        return new GeometryWrapper(geometry, wktTextSRS.getSrsURI(), URI, dimensionInfo, geometryLiteral);
    }

    private class WKTTextSRS {

        private final String wktText;
        private final String srsURI;

        public WKTTextSRS(String wktLiteral) {
            int startSRS = wktLiteral.indexOf("<");
            int endSRS = wktLiteral.indexOf(">");

            //Check that both chevrons are located and extract SRS_URI name, otherwise default.
            if (startSRS != -1 && endSRS != -1) {
                srsURI = wktLiteral.substring(startSRS + 1, endSRS);
                wktText = wktLiteral.substring(endSRS + 1);

            } else {
                srsURI = SRS_URI.DEFAULT_WKT_CRS84;
                wktText = wktLiteral;
            }
        }

        public String getWktText() {
            return wktText;
        }

        public String getSrsURI() {
            return srsURI;
        }

    }

    @Override
    public String toString() {
        return "WKTDatatype{" + URI + '}';
    }

}
