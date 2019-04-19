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
package io.github.galbiston.geosparql_jena.implementation.parsers.gml;

import io.github.galbiston.geosparql_jena.implementation.DimensionInfo;
import io.github.galbiston.geosparql_jena.implementation.jts.CoordinateSequenceDimensions;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomCoordinateSequence;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomGeometryFactory;
import io.github.galbiston.geosparql_jena.implementation.parsers.ParserReader;
import io.github.galbiston.geosparql_jena.implementation.registry.SRSRegistry;
import io.github.galbiston.geosparql_jena.implementation.vocabulary.SRS_URI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.locationtech.jts.geom.*;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GMLReader implements ParserReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    //Geometry attributes
    private final Geometry geometry;
    private final String srsURI;
    private final CoordinateSequenceDimensions dims;
    private final DimensionInfo dimensionInfo;

    private static final Namespace GML_NAMESPACE = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");

    /**
     * Aiming to achieve SF-0 of GML Simple Features Profile 2.0 [10-100R3].<br>
     * This is based on GML3.2 and limits the set of geometries.<br>
     * <br>
     * [10-100r3], page 22: All point geometries must use {@code <gml:pos>}
     * child element and all other geometries {@code <gml:posList>}. <br>
     * Converting geometries to those found in WKT and supported by JTS: Point,
     * LineString and Polygon.<br>
     * <br>
     * [10-100r3], page 23: The multi-shapes are listed. Multi-shapes have been
     * renamed between GML2 and GML3. MultiPolygon is now MultiSurface and
     * MutliLineString is now MultiCurve.<br>
     * Only X,Y and X,Y,Z coordinate and spatial dimensions supported.<br>
     * [07-036], page 310 states "srsDimension is the dimension of the
     * coordinate reference system as stated in the coordinate reference system
     * definition."<br>
     * [10-100r3], page 22 states "c) coordinate reference systems may have 1, 2
     * or 3 dimensions".
     *
     * @see
     * <a href="https://en.wikipedia.org/wiki/Geography_Markup_Language#GML_Simple_Features_Profile"></a>
     * @see
     * <a href="https://portal.opengeospatial.org/files/?artifact_id=42729"></a>
     *
     * @param gmlElement
     * @throws DatatypeFormatException
     */
    protected GMLReader(Element gmlElement) throws DatatypeFormatException {
        this.srsURI = getSrsURI(gmlElement);
        CoordinateReferenceSystem crs = SRSRegistry.getCRS(srsURI);

        // [07-036], page 56: "The optional attribute srsDimension is the number of coordinate values in a position. This dimension is derived
        // from the coordinate reference system."
        int srsDimension = crs.getCoordinateSystem().getDimension();
        this.dims = CoordinateSequenceDimensions.find(srsDimension);
        String geometryType = gmlElement.getName();
        this.geometry = buildGeometry(geometryType, gmlElement, dims);
        this.dimensionInfo = new DimensionInfo(dims, geometry.getDimension());
    }

    protected GMLReader(Geometry geometry, int srsDimension, String srsURI) {
        this.srsURI = srsURI;
        this.geometry = geometry;
        this.dims = CoordinateSequenceDimensions.find(srsDimension);
        this.dimensionInfo = new DimensionInfo(dims, geometry.getDimension());
    }

    protected GMLReader(Geometry geometry, int srsDimension) {
        this(geometry, srsDimension, SRS_URI.DEFAULT_WKT_CRS84);
    }

    @Override
    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public String getSrsURI() {
        return srsURI;
    }

    @Override
    public CoordinateSequenceDimensions getDimensions() {
        return dims;
    }

    @Override
    public DimensionInfo getDimensionInfo() {
        return dimensionInfo;
    }

    private static Boolean isSRSNameWarningIssued = false;

    private String getSrsURI(Element gmlElement) {
        String srsNameURI = gmlElement.getAttributeValue("srsName");
        if (srsNameURI == null) {
            srsNameURI = SRS_URI.DEFAULT_WKT_CRS84;
            if (!isSRSNameWarningIssued) {
                LOGGER.warn("GML Literal with no srsName. Defaulting to CRS84 {} used as WKT default SRS. This warning will be issued once.", srsNameURI);
                isSRSNameWarningIssued = true;
            }

        }
        return srsNameURI;
    }

    private Geometry buildGeometry(String shape, Element gmlElement, CoordinateSequenceDimensions dims) throws DatatypeFormatException {

        /**
         *
         * LineStringSegment
         * (http://www.datypic.com/sc/niem21/e-gml32_LineStringSegment.html) has
         * same structure as LineString
         * (http://www.datypic.com/sc/niem21/e-gml32_LineString.html).<br>
         * <br>
         * PolygonPatch
         * (http://www.datypic.com/sc/niem21/e-gml32_PolygonPatch.html) has same
         * structure as Polygon
         * (http://www.datypic.com/sc/niem21/e-gml32_Polygon.html).
         */
        Geometry geo;
        try {
            switch (shape) {
                case "Point":
                    geo = buildPoint(gmlElement, dims);
                    break;
                case "Curve":
                    geo = buildCurve(gmlElement, dims);
                    break;
                case "LineString":
                case "LineStringSegment":
                    geo = buildLineString(gmlElement, dims);
                    break;
                case "Polygon":
                case "PolygonPatch":
                    geo = buildPolygon(gmlElement, dims);
                    break;
                case "MultiPoint":
                    geo = buildMultiPoint(gmlElement, dims);
                    break;
                //case "MultiLineString":
                case "MultiCurve":
                    geo = buildMultiLineString(gmlElement, dims);
                    break;
                //case "MultiPolygon":
                case "MultiSurface":
                    geo = buildMultiPolygon(gmlElement, dims);
                    break;
                case "GeometryCollection":
                    geo = buildGeometryCollection(gmlElement, dims);
                    break;
                default:
                    throw new DatatypeFormatException("Geometry shape not supported: " + shape);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new DatatypeFormatException("Build GML Geometry Exception - Shape: " + shape + ", Element: " + gmlElement + ". " + ex.getMessage());
        }

        return geo;
    }

    private CustomCoordinateSequence extractPos(Element gmlElement, CoordinateSequenceDimensions dims) {
        String coordinates = gmlElement.getChildTextNormalize("pos", GML_NAMESPACE);
        if (coordinates == null) {
            coordinates = "";
        }
        return new CustomCoordinateSequence(dims, coordinates);
    }

    private CustomCoordinateSequence extractPosList(Element gmlElement, CoordinateSequenceDimensions dims) {
        String posList = gmlElement.getChildTextNormalize("posList", GML_NAMESPACE);
        if (posList == null) {
            return new CustomCoordinateSequence();
        }
        String cleanPosList = convertPosList(posList, dims);
        return new CustomCoordinateSequence(dims, cleanPosList);
    }

    private String convertPosList(String originalCoordinates, CoordinateSequenceDimensions dims) {
        StringBuilder sb = new StringBuilder("");
        String[] coordinates = originalCoordinates.trim().split(" ");

        int srsDimension = CoordinateSequenceDimensions.convertToInt(dims);
        int mod = coordinates.length % srsDimension;
        if (mod != 0) {
            throw new DatatypeFormatException("GML Pos List does not divide into srs dimension: " + coordinates.length + " divide " + srsDimension + " remainder " + mod + ".");
        }

        int finalCoordinate = coordinates.length - 1;
        for (int i = 0; i < coordinates.length; i++) {
            if (i != 0 & i % srsDimension == 0) {
                sb.append(",");
            }
            String coordinate = coordinates[i];
            sb.append(coordinate);
            if (i != finalCoordinate) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    private Point buildPoint(Element gmlElement, CoordinateSequenceDimensions dims) {
        CustomCoordinateSequence coordinateSequence = extractPos(gmlElement, dims);
        return GEOMETRY_FACTORY.createPoint(coordinateSequence);
    }

    /**
     * Curve has one or more LineStringSegments that have connecting points.
     * http://www.datypic.com/sc/niem21/e-gml32_Curve.html <br>
     * "The curve segments are connected to one another, with the end point of
     * each segment except the last being the start point of the next segment in
     * the segment list."<br>
     * [07-036], page 22: "gml:Curve with gml:LineStringSegment, gml:Arc,
     * gml:Circle or gml:CircleByCenterPoint segments1."
     *
     * @param gmlElement
     * @param srsDimension
     * @return
     */
    private LineString buildCurve(Element gmlElement, CoordinateSequenceDimensions dims) {
        //TODO Try using: GeometricShapeFactory gsf = new GeometricShapeFactory();
        //TODO Arc: three points that describe - centre and angles?
        //TODO Circle: three points that describe - centre and angles?
        //TODO CicleByCentrePoint: centre <pos></pos> and radius (use GSF or buffer from point as LineString)
        //TODO Add methods to GeometryWrapperFactory.createGMLArc, createGMLCircle, createGMLCircleByCentrePoint.

        //LineStringSegements
        List<Element> lineStringSegments = gmlElement.getChildren("LineStringSegments", GML_NAMESPACE);

        int srsDimension = CoordinateSequenceDimensions.convertToInt(dims);
        String posList = "";
        for (Element lineStringSegment : lineStringSegments) {
            String pList = lineStringSegment.getChildTextNormalize("posList", GML_NAMESPACE);
            if (pList != null) {
                if (posList.isEmpty()) {
                    posList = pList;
                } else {
                    int firstCoords = StringUtils.ordinalIndexOf(" ", pList, srsDimension);
                    posList += pList.substring(firstCoords);
                }
            }
        }

        String cleanPosList = convertPosList(posList, dims);
        CustomCoordinateSequence coordinateSequence = new CustomCoordinateSequence(dims, cleanPosList);
        return GEOMETRY_FACTORY.createLineString(coordinateSequence);
    }

    private LineString buildLineString(Element gmlElement, CoordinateSequenceDimensions dims) {
        CustomCoordinateSequence coordinateSequence = extractPosList(gmlElement, dims);
        return GEOMETRY_FACTORY.createLineString(coordinateSequence);
    }

    private Polygon buildPolygon(Element gmlElement, CoordinateSequenceDimensions dims) {
        //Following Polygon structure from: http://www.gdal.org/drv_gml.html
        Polygon polygon;

        //Exterior shell
        Element exteriorElement = gmlElement.getChild("exterior", GML_NAMESPACE);
        if (exteriorElement == null) {
            exteriorElement = gmlElement.getChild("outerBoundaryIs", GML_NAMESPACE);
        }
        CustomCoordinateSequence exteriorSequence;
        if (exteriorElement != null) {
            Element exteriorLinearRingElement = exteriorElement.getChild("LinearRing", GML_NAMESPACE);
            exteriorSequence = extractPosList(exteriorLinearRingElement, dims);
        } else {
            exteriorSequence = new CustomCoordinateSequence();
        }
        //Interior shell - that may not be present.
        List<Element> interiorElements = gmlElement.getChildren("interior", GML_NAMESPACE);
        if (interiorElements == null) {
            interiorElements = gmlElement.getChildren("innerBoundaryIs", GML_NAMESPACE);
        }
        List<LinearRing> interiorLinearRingList = new ArrayList<>();
        for (Element interiorElement : interiorElements) {
            Element interiorLinearRingElement = interiorElement.getChild("LinearRing", GML_NAMESPACE);
            CustomCoordinateSequence interiorSequence = extractPosList(interiorLinearRingElement, dims);
            LinearRing linearRing = GEOMETRY_FACTORY.createLinearRing(interiorSequence);
            interiorLinearRingList.add(linearRing);
        }

        //Build the polygon depending on whether interior shells were found.
        if (interiorLinearRingList.isEmpty()) {
            polygon = GEOMETRY_FACTORY.createPolygon(exteriorSequence);
        } else {
            LinearRing exteriorLinearRing = GEOMETRY_FACTORY.createLinearRing(exteriorSequence);
            LinearRing[] interiorLinerRings = interiorLinearRingList.toArray(new LinearRing[interiorLinearRingList.size()]);
            polygon = GEOMETRY_FACTORY.createPolygon(exteriorLinearRing, interiorLinerRings);
        }

        return polygon;
    }

    private Geometry buildMultiPoint(Element gmlElement, CoordinateSequenceDimensions dims) {

        List<Element> children = gmlElement.getChildren();
        Point[] points = new Point[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            Element point = child.getChild("Point", GML_NAMESPACE);
            CustomCoordinateSequence sequence = extractPos(point, dims);

            points[i] = GEOMETRY_FACTORY.createPoint(sequence);
        }
        return GEOMETRY_FACTORY.createMultiPoint(points);
    }

    private Geometry buildMultiLineString(Element gmlElement, CoordinateSequenceDimensions dims) {

        List<Element> children = gmlElement.getChildren();
        LineString[] lineStrings = new LineString[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            Element lineString = child.getChild("LineString", GML_NAMESPACE);
            CustomCoordinateSequence sequence = extractPosList(lineString, dims);

            lineStrings[i] = GEOMETRY_FACTORY.createLineString(sequence);
        }
        return GEOMETRY_FACTORY.createMultiLineString(lineStrings);
    }

    private Geometry buildMultiPolygon(Element gmlElement, CoordinateSequenceDimensions dims) {

        List<Element> children = gmlElement.getChildren();
        Polygon[] polygons = new Polygon[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            polygons[i] = buildPolygon(child.getChild("Polygon", GML_NAMESPACE), dims);
        }

        return GEOMETRY_FACTORY.createMultiPolygon(polygons);
    }

    private Geometry buildGeometryCollection(Element gmlElement, CoordinateSequenceDimensions dims) {

        List<Element> children = gmlElement.getChildren();
        Geometry[] geometries = new Geometry[children.size()];

        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);

            //Geometry Members
            for (Element grandChild : child.getChildren()) {
                String shape = grandChild.getName();
                geometries[i] = buildGeometry(shape, grandChild, dims);
            }
        }

        return GEOMETRY_FACTORY.createGeometryCollection(geometries);
    }

    private static final String EMPTY_GML_TEXT = "<gml:Point xmlns:gml='http://www.opengis.net/ont/gml' srsName=\"http://www.opengis.net/def/crs/OGC/1.3/CRS84\" />";

    public static GMLReader extract(String gmlText) throws JDOMException, IOException {

        if (gmlText.isEmpty()) {
            gmlText = EMPTY_GML_TEXT;
        }

        SAXBuilder jdomBuilder = new SAXBuilder();
        InputStream stream = new ByteArrayInputStream(gmlText.getBytes("UTF-8"));
        Document xmlDoc = jdomBuilder.build(stream);

        Element gmlElement = xmlDoc.getRootElement();

        return new GMLReader(gmlElement);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.geometry);
        hash = 41 * hash + Objects.hashCode(this.srsURI);
        hash = 41 * hash + Objects.hashCode(this.dims);
        hash = 41 * hash + Objects.hashCode(this.dimensionInfo);
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
        final GMLReader other = (GMLReader) obj;
        if (!Objects.equals(this.srsURI, other.srsURI)) {
            return false;
        }
        if (!Objects.equals(this.geometry, other.geometry)) {
            return false;
        }
        if (this.dims != other.dims) {
            return false;
        }
        return Objects.equals(this.dimensionInfo, other.dimensionInfo);
    }

    @Override
    public String toString() {
        return "GMLReader{" + "geometry=" + geometry + ", srsURI=" + srsURI + ", dims=" + dims + ", dimensionInfo=" + dimensionInfo + '}';
    }

}
