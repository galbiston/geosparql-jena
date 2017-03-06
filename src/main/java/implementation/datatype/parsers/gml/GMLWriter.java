/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype.parsers.gml;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import implementation.GeometryWrapper;
import implementation.jts.CustomCoordinateSequence;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author haozhechen
 */
public class GMLWriter {

    public static String write(GeometryWrapper geometryWrapper) {

        Geometry geometry = geometryWrapper.getParsingGeometry();
        CustomCoordinateSequence.CoordinateSequenceDimensions dimensions = geometryWrapper.getCoordinateSequenceDimensions();
        String srsName = geometryWrapper.getSrsURI();
        String gmlText = expand(geometry, dimensions, srsName);
        return gmlText;
    }

    private static String expand(final Geometry geometry, final CustomCoordinateSequence.CoordinateSequenceDimensions dimensions, final String srsName) {

        String gmlString = "";
        String srsDimension = convertDimensions(dimensions);
        switch (geometry.getGeometryType()) {
            case "Point":
                Point point = (Point) geometry;
                gmlString = buildGML("Point", point.getCoordinateSequence(), srsDimension, srsName);
                break;
            case "LineString":
                LineString lineString = (LineString) geometry;
                gmlString = buildGML("LineString", lineString.getCoordinateSequence(), srsDimension, srsName);
        }
        return gmlString;
    }

    private static String buildGML(final String geometryType, final CoordinateSequence coordSeq, final String dimensionString, final String srsName) {
        CustomCoordinateSequence coordSequence = (CustomCoordinateSequence) coordSeq;
        String gmlText = coordSequence.toGMLText();

        Namespace gmlNamespace = Namespace.getNamespace("gml", "http://www.opengis.net/ont/gml");
        Element gmlRoot = new Element(geometryType, gmlNamespace);
        Document doc = new Document(gmlRoot);
        gmlRoot.setAttribute("srsName", srsName);
        gmlRoot.setAttribute("srsDimension", dimensionString);
        Element coordinates = new Element("coordinates", gmlNamespace);
        coordinates.addContent(gmlText);
        gmlRoot.addContent(coordinates);

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat().setOmitDeclaration(true));
        String output = xmlOutput.outputString(doc);
        return output;
    }

    private static String buildPolygon(final String geometryType, final boolean isIncludeGeometryType, final CoordinateSequence coordSeq, final String dimensionString, final String srsName) {
        return null;
    }

    private static String convertDimensions(final CustomCoordinateSequence.CoordinateSequenceDimensions dimensions) {

        switch (dimensions) {
            case XYZ:
                return 3 + "";
            default:
                return 2 + "";
        }
    }
}
