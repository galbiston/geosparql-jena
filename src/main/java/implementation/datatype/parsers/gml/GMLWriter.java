/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.gml;

import com.vividsolutions.jts.geom.*;
import implementation.GeometryWrapper;
import implementation.datatype.parsers.ParseException;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 *
 */
public class GMLWriter {

    public static String write(GeometryWrapper geometryWrapper) {

        Geometry geometry = geometryWrapper.getParsingGeometry();
        CustomCoordinateSequence.CoordinateSequenceDimensions dimensions = geometryWrapper.getCoordinateSequenceDimensions();
        String srsName = geometryWrapper.getSrsURI();
        //String gmlText = expand(geometry, dimensions, srsName);
        Document doc = new Document(expand(geometry, dimensions, srsName));
        XMLOutputter xmlOutput = new XMLOutputter();
        //set GML output format
        xmlOutput.setFormat(Format.getCompactFormat().setOmitDeclaration(true));
        String output = xmlOutput.outputString(doc);
        return output;
    }

    private static Element expand(final Geometry geometry, final CoordinateSequenceDimensions dimensions, final String srsName) {

        Element gmlElement = new Element("EMPTY");
        String srsDimension = convertDimensions(dimensions);
        switch (geometry.getGeometryType()) {
            case "Point":
                Point point = (Point) geometry;
                gmlElement = buildGML("Point", point.getCoordinateSequence(), srsDimension, srsName);
                break;
            case "LineString":
                LineString lineString = (LineString) geometry;
                gmlElement = buildGML("LineString", lineString.getCoordinateSequence(), srsDimension, srsName);
                break;
            case "Polygon":
                Polygon polygon = (Polygon) geometry;
                gmlElement = buildPolygon(polygon, srsDimension, srsName);
                break;
            case "MultiPoint":
                MultiPoint multiPoint = (MultiPoint) geometry;
                gmlElement = buildMultiPoint(multiPoint, srsDimension, srsName);
                break;
            case "MultiLineString":
                MultiLineString multiLineString = (MultiLineString) geometry;
                gmlElement = buildMultiLineString(multiLineString, srsDimension, srsName);
                break;
            case "MultiPolygon":
                MultiPolygon multiPolygon = (MultiPolygon) geometry;
                gmlElement = buildMultiPolygon(multiPolygon, srsDimension, srsName);
                break;
            case "GeometryCollection":
                GeometryCollection geometryCollection = (GeometryCollection) geometry;
                gmlElement = buildGeometryCollection(geometryCollection, srsDimension, dimensions, srsName);
                break;
            default:
                throw new ParseException("Geometry type not supported: " + geometry.getGeometryType());

        }
        return gmlElement;
    }

    private static Element buildGML(final String geometryType, final CoordinateSequence coordSeq, final String dimensionString, final String srsName) {
        CustomCoordinateSequence coordSequence = (CustomCoordinateSequence) coordSeq;

        Namespace gmlNamespace = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");
        Element gmlRoot = new Element(geometryType, gmlNamespace);
        gmlRoot.setAttribute("srsName", srsName);
        gmlRoot.setAttribute("srsDimension", dimensionString);
        Element coordinates = new Element("coordinates", gmlNamespace);
        coordinates.addContent(coordSequence.toGMLText());
        gmlRoot.addContent(coordinates);

        return gmlRoot;
    }

    private static Element buildPolygon(final Polygon polygon, final String dimensionString, final String srsName) {

        Namespace gmlNamespace = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");
        Element gmlRoot = new Element(polygon.getGeometryType(), gmlNamespace);
        gmlRoot.setAttribute("srsName", srsName);
        gmlRoot.setAttribute("srsDimension", dimensionString);

        if (!polygon.isEmpty()) {
            LineString lineString = polygon.getExteriorRing();
            CustomCoordinateSequence coordSequence = (CustomCoordinateSequence) lineString.getCoordinateSequence();

            //Find exterior shell
            Element exterior = new Element("Exterior", gmlNamespace);
            Element posList = new Element("PosList", gmlNamespace);
            posList.addContent(coordSequence.toGMLText());
            exterior.addContent(posList);
            gmlRoot.addContent(exterior);

            //Find inner holes
            int interiorRings = polygon.getNumInteriorRing();

            for (int i = 0; i < interiorRings; i++) {
                //flush all content
                Element interior = new Element("Interior", gmlNamespace);
                Element inerPosList = new Element("PosList", gmlNamespace);

                LineString innerLineString = polygon.getInteriorRingN(i);
                CustomCoordinateSequence innerCoordSequence = (CustomCoordinateSequence) innerLineString.getCoordinateSequence();
                inerPosList.addContent(innerCoordSequence.toGMLText());
                interior.addContent(inerPosList);
                gmlRoot.addContent(interior);
            }

        } else {
            //Do nothing to the GML
        }
        return gmlRoot;
    }

    private static Element buildMultiPoint(final MultiPoint multiPoint, final String dimensionString, final String srsName) {

        Namespace gmlNamespace = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");
        Element gmlRoot = new Element(multiPoint.getGeometryType(), gmlNamespace);
        gmlRoot.setAttribute("srsName", srsName);
        gmlRoot.setAttribute("srsDimension", dimensionString);

        if (!multiPoint.isEmpty()) {

            int geomCount = multiPoint.getNumGeometries();
            for (int i = 0; i < geomCount; i++) {
                Element pointelement = new Element("Point", gmlNamespace);
                Element pointMember = new Element("PointMember", gmlNamespace);

                Point point = (Point) multiPoint.getGeometryN(i);
                CustomCoordinateSequence coordSequence = (CustomCoordinateSequence) point.getCoordinateSequence();
                pointelement.addContent(coordSequence.toGMLText());
                pointMember.addContent(pointelement);
                gmlRoot.addContent(pointMember);
            }

        } else {
            //Do nothing
        }
        return gmlRoot;
    }

    private static Element buildMultiLineString(final MultiLineString multiLineString, final String dimensionString, final String srsName) {

        Namespace gmlNamespace = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");
        Element gmlRoot = new Element(multiLineString.getGeometryType(), gmlNamespace);
        gmlRoot.setAttribute("srsName", srsName);
        gmlRoot.setAttribute("srsDimension", dimensionString);

        if (!multiLineString.isEmpty()) {

            int geomCount = multiLineString.getNumGeometries();
            for (int i = 0; i < geomCount; i++) {
                Element lineStringelement = new Element("LineString", gmlNamespace);
                Element lineStringMember = new Element("LineStringMember", gmlNamespace);

                LineString lineString = (LineString) multiLineString.getGeometryN(i);
                CustomCoordinateSequence coordSequence = (CustomCoordinateSequence) lineString.getCoordinateSequence();
                lineStringelement.addContent(coordSequence.toGMLText());
                lineStringMember.addContent(lineStringelement);
                gmlRoot.addContent(lineStringMember);
            }

        } else {
            //Do nothing
        }
        return gmlRoot;
    }

    private static Element buildMultiPolygon(final MultiPolygon multiPolygon, final String dimensionString, final String srsName) {

        Namespace gmlNamespace = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");
        Element gmlRoot = new Element(multiPolygon.getGeometryType(), gmlNamespace);
        gmlRoot.setAttribute("srsName", srsName);
        gmlRoot.setAttribute("srsDimension", dimensionString);

        if (!multiPolygon.isEmpty()) {

            int geomCount = multiPolygon.getNumGeometries();
            for (int i = 0; i < geomCount; i++) {
                Element polygonMember = new Element("PolygonMember", gmlNamespace);

                Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);

                polygonMember.addContent(buildPolygon(polygon, dimensionString, srsName));
                gmlRoot.addContent(polygonMember);
            }

        } else {
            //Do nothing
        }
        return gmlRoot;
    }

    private static Element buildGeometryCollection(final GeometryCollection geometryCollection, final String dimensionString, final CoordinateSequenceDimensions dimensions, final String srsName) {

        Namespace gmlNamespace = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");
        Element gmlRoot = new Element(geometryCollection.getGeometryType(), gmlNamespace);
        gmlRoot.setAttribute("srsName", srsName);
        gmlRoot.setAttribute("srsDimension", dimensionString);

        if (!geometryCollection.isEmpty()) {

            int geomCount = geometryCollection.getNumGeometries();
            for (int i = 0; i < geomCount; i++) {
                Element geometryMember = new Element("GeometryMember", gmlNamespace);

                Geometry geometry = geometryCollection.getGeometryN(i);
                geometryMember.addContent(expand(geometry, dimensions, srsName));
                gmlRoot.addContent(geometryMember);
            }

        } else {
            //Do nothing
        }
        return gmlRoot;
    }

    private static String convertDimensions(final CoordinateSequenceDimensions dimensions) {

        switch (dimensions) {
            case XYZ:
                return 3 + "";
            case XYZM:
                return 3 + "";
            case XYM:
                return 2 + "";
            default:
                return 2 + "";
        }
    }
}
