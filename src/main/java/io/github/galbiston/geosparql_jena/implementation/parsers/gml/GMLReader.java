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
import io.github.galbiston.geosparql_jena.implementation.SRSInfo;
import io.github.galbiston.geosparql_jena.implementation.SRSInfoException;
import io.github.galbiston.geosparql_jena.implementation.UnitsOfMeasure;
import io.github.galbiston.geosparql_jena.implementation.jts.CoordinateSequenceDimensions;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomCoordinateSequence;
import io.github.galbiston.geosparql_jena.implementation.jts.CustomGeometryFactory;
import io.github.galbiston.geosparql_jena.implementation.parsers.ParserReader;
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
import org.locationtech.jts.util.GeometricShapeFactory;
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
    protected GMLReader(Element gmlElement) throws DatatypeFormatException, SRSInfoException {
        this.srsURI = getSrsURI(gmlElement);
        SRSInfo srsInfo = new SRSInfo(srsURI);
        CoordinateReferenceSystem crs = srsInfo.getCrs();

        // [07-036], page 56: "The optional attribute srsDimension is the number of coordinate values in a position. This dimension is derived
        // from the coordinate reference system."
        int srsDimension = crs.getCoordinateSystem().getDimension();
        this.dims = CoordinateSequenceDimensions.find(srsDimension);
        String geometryType = gmlElement.getName();

        this.geometry = buildGeometry(geometryType, gmlElement, dims, srsInfo);
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

    private Geometry buildGeometry(String shape, Element gmlElement, CoordinateSequenceDimensions dims, SRSInfo srsInfo) throws DatatypeFormatException {

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
                case "LineString":
                    geo = buildLineString(gmlElement, dims);
                    break;
                case "Curve":
                    geo = buildCurve(gmlElement, dims, srsInfo);
                    break;
                case "Polygon":
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
                    geo = buildGeometryCollection(gmlElement, dims, srsInfo);
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

    private String extractPosList(Element gmlElement, int srsDimension) {
        String posList = gmlElement.getChildTextNormalize("posList", GML_NAMESPACE);
        StringBuilder sb = new StringBuilder("");
        String[] coordinates = posList.trim().split(" ");

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

    private List<Coordinate> buildCoordinateList(Element gmlElement, int srsDimension) {

        String posList = gmlElement.getChildTextNormalize("posList", GML_NAMESPACE);
        String[] coordinates = posList.trim().split(" ");

        int mod = coordinates.length % srsDimension;
        if (mod != 0) {
            throw new DatatypeFormatException("GML Pos List does not divide into srs dimension: " + coordinates.length + " divide " + srsDimension + " remainder " + mod + ".");
        }

        List<Coordinate> coordinateList = new ArrayList<>();

        for (int i = 0; i < coordinates.length; i += srsDimension) {
            Coordinate coord;
            //[10-100rs], page 22: "c) coordinate reference systems may have 1, 2 or 3 dimensions". 1 dimension does not fit with spatial relations of GeoSPARQL.
            switch (srsDimension) {
                case 2:
                    coord = new CoordinateXY(Double.parseDouble(coordinates[i]), Double.parseDouble(coordinates[i + 1]));
                    break;
                case 3:
                    coord = new Coordinate(Double.parseDouble(coordinates[i]), Double.parseDouble(coordinates[i + 1]), Double.parseDouble(coordinates[i + 2]));
                    break;
                default:
                    throw new DatatypeFormatException("SRS dimension " + srsDimension + " is not supported.");
            }
            coordinateList.add(coord);
        }

        return coordinateList;
    }

    private Point buildPoint(Element gmlElement, CoordinateSequenceDimensions dims) {
        CustomCoordinateSequence coordinateSequence = extractPos(gmlElement, dims);
        return GEOMETRY_FACTORY.createPoint(coordinateSequence);
    }

    private LineString buildLineString(Element gmlElement, CoordinateSequenceDimensions dims) {
        int srsDimension = CoordinateSequenceDimensions.convertToInt(dims);
        String posList = extractPosList(gmlElement, srsDimension);
        CustomCoordinateSequence coordinateSequence = new CustomCoordinateSequence(dims, posList);
        return GEOMETRY_FACTORY.createLineString(coordinateSequence);
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
    private Geometry buildCurve(Element gmlElement, CoordinateSequenceDimensions dims, SRSInfo srsInfo) {
        //TODO Try using: GeometricShapeFactory gsf = new GeometricShapeFactory();
        //TODO Arc: three points that describe - centre and angles?
        //TODO Circle: three points that describe - centre and angles?
        //TODO Add methods to GeometryWrapperFactory.createGMLArc, createGMLCircle, createGMLCircleByCentrePoint.

        //LineStringSegements
        Element segmentsElement = gmlElement.getChild("segments", GML_NAMESPACE);

        List<Element> segments = segmentsElement.getChildren("LineStringSegment", GML_NAMESPACE);
        if (!segments.isEmpty()) {
            return buildLineStringSegments(segments, dims);
        }

        segments = segmentsElement.getChildren("Arc", GML_NAMESPACE);
        if (!segments.isEmpty()) {
            return buildArc(segments, dims);
        }

        segments = segmentsElement.getChildren("Circle", GML_NAMESPACE);
        if (!segments.isEmpty()) {
            return buildCircle(segments, dims);
        }

        segments = segmentsElement.getChildren("CircleByCenterPoint", GML_NAMESPACE);
        if (!segments.isEmpty()) {
            return buildCircleByCentrePoint(segments, dims, srsInfo);
        } else {
            throw new DatatypeFormatException("GML Curve segments not supported or empty: " + gmlElement);
        }

    }

    private LineString buildLineStringSegments(List<Element> segments, CoordinateSequenceDimensions dims) {

        int srsDimension = CoordinateSequenceDimensions.convertToInt(dims);
        String posList = "";
        for (Element segment : segments) {

            String segmentPosList = extractPosList(segment, srsDimension);

            if (segmentPosList != null) {
                if (posList.isEmpty()) {
                    posList = segmentPosList;
                } else {
                    int firstCoords = StringUtils.ordinalIndexOf(" ", segmentPosList, srsDimension);
                    posList += segmentPosList.substring(firstCoords);
                }
            }
        }
        CustomCoordinateSequence coordinateSequence = new CustomCoordinateSequence(dims, posList);
        return GEOMETRY_FACTORY.createLineString(coordinateSequence);
    }

    protected LineString buildArc(List<Element> segments, CoordinateSequenceDimensions dims) {
        int srsDimension = CoordinateSequenceDimensions.convertToInt(dims);

        Element posListElement = segments.get(0); //Already ensured that non-zero length. gml:Arc only has single posList.
        List<Coordinate> coordinates = buildCoordinateList(posListElement, srsDimension);
        if (coordinates.size() != 3) {
            throw new DatatypeFormatException("GML Arc posList does not contain 3 coordinates: " + posListElement);
        }

        Coordinate centre = findCentre(coordinates);
        Coordinate coord0 = coordinates.get(0);
        double radius = Math.hypot(centre.x - coord0.x, centre.y - coord0.y); //All coordinates on the arc are radius distance from centre point.

        GeometricShapeFactory geometricShapeFactory = new GeometricShapeFactory(GEOMETRY_FACTORY);
        geometricShapeFactory.setCentre(centre);
        geometricShapeFactory.setWidth(radius * 2);

        double startAng = findAngle(centre, coord0);
        double angExtent = findAngle(centre, coordinates.get(2));
        return geometricShapeFactory.createArc(startAng, angExtent);
    }

    protected Coordinate findCentre(List<Coordinate> coordinates) {
        if (coordinates.size() < 3) {
            throw new DatatypeFormatException("GML posList does not contain 3 coordinates: " + coordinates);
        }

        //Construct two chords between available coordinates.
        LineSegment line0 = new LineSegment(coordinates.get(0), coordinates.get(1));
        LineSegment line1 = new LineSegment(coordinates.get(1), coordinates.get(2));

        //Find the perpendicular lines for each chord.
        LineSegment pLine0 = buildPerpendicularLine(line0);
        LineSegment pLine1 = buildPerpendicularLine(line1);

        //Centre point is the intersection of the perpendicular lines.
        Coordinate centre = pLine0.intersection(pLine1);
        return centre;
    }

    private static final double HALF_PI = Math.PI / 2;
    private static final double HALF_LENGTH = Double.MAX_VALUE / 2;

    protected LineSegment buildPerpendicularLine(LineSegment line) {
        Coordinate midPoint = line.midPoint();

        //Perpendicular angle to the chord in x-axis.
        double normalAngle = line.angle() + HALF_PI;

        //Determine the change in axis for maximum length line.
        double dx = HALF_LENGTH * Math.cos(normalAngle);
        double dy = HALF_LENGTH * Math.sin(normalAngle);

        //Start point
        double x0 = midPoint.x - dx;
        double y0 = midPoint.y - dy;
        Coordinate coord0 = new Coordinate(x0, y0);

        //End point
        double x1 = midPoint.x + dx;
        double y1 = midPoint.y + dy;
        Coordinate coord1 = new Coordinate(x1, y1);

        LineSegment pLine = new LineSegment(coord0, coord1);
        return pLine;
    }

    protected double findAngle(Coordinate coord0, Coordinate coord1) {

        LineSegment line = new LineSegment(coord0, coord1);
        double angle = line.angle();
        return angle;
    }

    private Polygon buildCircle(List<Element> segments, CoordinateSequenceDimensions dims) {
        int srsDimension = CoordinateSequenceDimensions.convertToInt(dims);

        Element posListElement = segments.get(0); //Already ensured that non-zero length. gml:Circle only has single posList.
        List<Coordinate> coordinates = buildCoordinateList(posListElement, srsDimension);
        if (coordinates.size() != 3) {
            throw new DatatypeFormatException("GML Circle posList does not contain 3 coordinates: " + posListElement);
        }

        Coordinate centre = findCentre(coordinates);
        Coordinate coord = coordinates.get(0);
        double radius = Math.hypot(centre.x - coord.x, centre.y - coord.y);

        GeometricShapeFactory geometricShapeFactory = new GeometricShapeFactory(GEOMETRY_FACTORY);
        geometricShapeFactory.setCentre(centre);
        geometricShapeFactory.setWidth(radius * 2);
        return geometricShapeFactory.createCircle();
    }

    private Polygon buildCircleByCentrePoint(List<Element> segments, CoordinateSequenceDimensions dims, SRSInfo srsInfo) {

        Element circleElement = segments.get(0);
        Point point = buildPoint(circleElement, dims);
        Coordinate centre = point.getCoordinate();

        //Get radius and convert units of measure if required.
        Element radiusElement = circleElement.getChild("radius", GML_NAMESPACE);
        double radius = Double.parseDouble(radiusElement.getTextNormalize());

        //Extract units of measure uri. Use SRS units of measure if absent.
        String uomURI = radiusElement.getAttributeValue("uom");
        if (uomURI == null) {
            uomURI = srsInfo.getUnitsOfMeasure().getUnitURI();
        }

        //Convert the radius if the specified units of measure don't match.
        if (!uomURI.equals(srsInfo.getUnitsOfMeasure().getUnitURI())) {
            UnitsOfMeasure sourceUnitsOfMeasure = new UnitsOfMeasure(uomURI);
            radius = UnitsOfMeasure.conversion(radius, sourceUnitsOfMeasure, srsInfo.getUnitsOfMeasure());
        }

        GeometricShapeFactory geometricShapeFactory = new GeometricShapeFactory(GEOMETRY_FACTORY);
        geometricShapeFactory.setCentre(centre);
        geometricShapeFactory.setSize(radius * 2);  //Set the width and height, i.e. the diameter.
        return geometricShapeFactory.createCircle();
    }

    private Polygon buildPolygon(Element gmlElement, CoordinateSequenceDimensions dims) {

        Polygon polygon;
        int srsDimension = CoordinateSequenceDimensions.convertToInt(dims);

        //Exterior shell - [0..1]
        Element exteriorElement = gmlElement.getChild("exterior", GML_NAMESPACE);
        CustomCoordinateSequence exteriorSequence;
        if (exteriorElement != null) {
            Element exteriorLinearRingElement = exteriorElement.getChild("LinearRing", GML_NAMESPACE);
            String posList = extractPosList(exteriorLinearRingElement, srsDimension);
            exteriorSequence = new CustomCoordinateSequence(dims, posList);
        } else {
            exteriorSequence = new CustomCoordinateSequence();
        }
        //Interior shell - [0..*]
        List<Element> interiorElements = gmlElement.getChildren("interior", GML_NAMESPACE);
        List<LinearRing> interiorLinearRingList = new ArrayList<>();
        for (Element interiorElement : interiorElements) {
            Element interiorLinearRingElement = interiorElement.getChild("LinearRing", GML_NAMESPACE);
            String posList = extractPosList(interiorLinearRingElement, srsDimension);
            CustomCoordinateSequence interiorSequence = new CustomCoordinateSequence(dims, posList);
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
        int srsDimension = CoordinateSequenceDimensions.convertToInt(dims);

        List<Element> children = gmlElement.getChildren();
        LineString[] lineStrings = new LineString[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            Element lineString = child.getChild("LineString", GML_NAMESPACE);
            String posList = extractPosList(lineString, srsDimension);
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(dims, posList);
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

    private Geometry buildGeometryCollection(Element gmlElement, CoordinateSequenceDimensions dims, SRSInfo srsInfo) {

        List<Element> children = gmlElement.getChildren();
        Geometry[] geometries = new Geometry[children.size()];

        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);

            //Geometry Members
            for (Element grandChild : child.getChildren()) {
                String shape = grandChild.getName();
                geometries[i] = buildGeometry(shape, grandChild, dims, srsInfo);
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
