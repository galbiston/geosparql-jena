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
package implementation.parsers.wkt;

import implementation.DimensionInfo;
import implementation.datatype.ParseException;
import implementation.jts.CustomCoordinateSequence;
import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import implementation.jts.CustomGeometryFactory;
import java.util.Arrays;
import java.util.Objects;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class WKTReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKTReader.class);
    private static final GeometryFactory GEOMETRY_FACTORY = CustomGeometryFactory.theInstance();

    private final CoordinateSequenceDimensions coordinateSequenceDimensions;
    private final Geometry geometry;

    public WKTReader(String geometryType, String dimension, String coordinates) {
        this.coordinateSequenceDimensions = convertDimensions(dimension);
        this.geometry = buildGeometry(geometryType, coordinates);
    }

    public WKTReader() {
        this("point", "", "");
    }

    private static CoordinateSequenceDimensions convertDimensions(String dimensionsString) {

        CoordinateSequenceDimensions dims;
        switch (dimensionsString) {
            case "zm":
                dims = CoordinateSequenceDimensions.XYZM;
                break;
            case "z":
                dims = CoordinateSequenceDimensions.XYZ;
                break;
            case "m":
                dims = CoordinateSequenceDimensions.XYM;
                break;
            default:
                dims = CoordinateSequenceDimensions.XY;
                break;
        }
        return dims;
    }

    public DimensionInfo getDimensionInfo() {
        return new DimensionInfo(getCoordinateDimension(), getSpatialDimension(), geometry.getDimension());
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public int getSpatialDimension() {

        switch (coordinateSequenceDimensions) {
            default:
                return 2;
            case XYZ:
            case XYZM:
                return 3;
        }
    }

    public int getCoordinateDimension() {

        switch (coordinateSequenceDimensions) {
            default:
                return 2;
            case XYZ:
            case XYM:
                return 3;
            case XYZM:
                return 4;
        }
    }

    private Geometry buildGeometry(String geometryType, String coordinates) throws ParseException {

        Geometry geo;

        try {
            switch (geometryType) {
                case "point":
                    CustomCoordinateSequence pointSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                    geo = GEOMETRY_FACTORY.createPoint(pointSequence);
                    break;
                case "linestring":
                    CustomCoordinateSequence lineSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                    geo = GEOMETRY_FACTORY.createLineString(lineSequence);
                    break;
                case "linearring":
                    CustomCoordinateSequence linearSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                    geo = GEOMETRY_FACTORY.createLinearRing(linearSequence);
                    break;
                case "polygon":
                    geo = buildPolygon(coordinates);
                    break;
                case "multipoint":
                    CustomCoordinateSequence multiPointSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
                    geo = GEOMETRY_FACTORY.createMultiPoint(multiPointSequence);
                    break;
                case "multilinestring":
                    geo = buildMultiLineString(coordinates);
                    break;
                case "multipolygon":
                    geo = buildMultiPolygon(coordinates);
                    break;
                case "geometrycollection":
                    geo = buildGeometryCollection(coordinates);
                    break;
                default:
                    throw new ParseException("Geometry type not supported: " + geometryType);
            }
        } catch (ArrayIndexOutOfBoundsException | ParseException ex) {
            LOGGER.error("Build WKT Geometry Exception - Type: {}, Coordinates: {}", geometryType, coordinates);
            throw new ParseException(ex.getMessage());
        }
        return geo;
    }

    private String clean(String unclean) {
        return unclean.replace(")", "").replace("(", "").trim();
    }

    private Geometry buildGeometryCollection(String coordinates) throws ParseException {

        if (coordinates.isEmpty()) {
            return GEOMETRY_FACTORY.createGeometryCollection(new Geometry[0]);
        }

        //Split coordinates
        String tidied = coordinates.substring(1, coordinates.length() - 1);
        tidied = tidied.replaceAll("[\\ ]?,[\\ ]?", ","); //Remove spaces around commas
        String[] partCoordinates = tidied.split("\\),(?=[^\\(])"); //Split whenever there is a ), but not ),(

        Geometry[] geometries = new Geometry[partCoordinates.length];

        for (int i = 0; i < partCoordinates.length; i++) {
            WKTReader partWKTInfo = extract(partCoordinates[i]);
            geometries[i] = partWKTInfo.geometry;
        }
        return GEOMETRY_FACTORY.createGeometryCollection(geometries);
    }

    private Geometry buildMultiLineString(String coordinates) {

        if (coordinates.isEmpty()) {
            return GEOMETRY_FACTORY.createMultiLineString(new LineString[0]);
        }

        String[] splitCoordinates = splitCoordinates(coordinates);
        LineString[] lineStrings = splitLineStrings(splitCoordinates);
        return GEOMETRY_FACTORY.createMultiLineString(lineStrings);
    }

    private Geometry buildMultiPolygon(String coordinates) {

        if (coordinates.isEmpty()) {
            return GEOMETRY_FACTORY.createMultiPolygon(new Polygon[0]);
        }

        String trimmed = coordinates.replace(")) ,", ")),");
        String[] multiCoordinates = trimmed.split("\\)\\),");
        Polygon[] polygons = new Polygon[multiCoordinates.length];
        for (int i = 0; i < multiCoordinates.length; i++) {
            polygons[i] = buildPolygon(multiCoordinates[i]);
        }

        return GEOMETRY_FACTORY.createMultiPolygon(polygons);
    }

    private Polygon buildPolygon(String coordinates) {

        Polygon polygon;

        String[] splitCoordinates = splitCoordinates(coordinates);
        if (splitCoordinates.length == 1) { //Polygon without holes.
            CustomCoordinateSequence shellSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(coordinates));
            polygon = GEOMETRY_FACTORY.createPolygon(shellSequence);
        } else {    //Polygon with holes
            String shellCoordinates = splitCoordinates[0];

            CustomCoordinateSequence shellSequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(shellCoordinates));
            LinearRing shellLinearRing = GEOMETRY_FACTORY.createLinearRing(shellSequence);

            String[] splitHoleCoordinates = Arrays.copyOfRange(splitCoordinates, 1, splitCoordinates.length);
            LinearRing[] holesLinearRing = splitLinearRings(splitHoleCoordinates);

            polygon = GEOMETRY_FACTORY.createPolygon(shellLinearRing, holesLinearRing);

        }
        return polygon;
    }

    private String[] splitCoordinates(String coordinates) {

        String trimmed = coordinates.replace(") ,", "),");
        return trimmed.split("\\),");

    }

    private LineString[] splitLineStrings(String[] splitCoordinates) {

        LineString[] lineStrings = new LineString[splitCoordinates.length];

        for (int i = 0; i < splitCoordinates.length; i++) {
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(splitCoordinates[i]));
            LineString lineString = GEOMETRY_FACTORY.createLineString(sequence);
            lineStrings[i] = lineString;
        }

        return lineStrings;

    }

    private LinearRing[] splitLinearRings(String[] splitCoordinates) {

        LinearRing[] linearRings = new LinearRing[splitCoordinates.length];

        for (int i = 0; i < splitCoordinates.length; i++) {
            CustomCoordinateSequence sequence = new CustomCoordinateSequence(coordinateSequenceDimensions, clean(splitCoordinates[i]));
            LinearRing linearRing = GEOMETRY_FACTORY.createLinearRing(sequence);
            linearRings[i] = linearRing;
        }

        return linearRings;

    }

    public static WKTReader extract(String wktText) throws ParseException {

        String goemetryType = "point";
        String dimension = "";
        String coordinates = "";

        if (!wktText.equals("")) {

            wktText = wktText.trim();
            wktText = wktText.toLowerCase();

            String[] parts = wktText.split("\\(", 2);

            String remainder;
            if (parts.length == 1) { //Check for "empty" keyword and remove.
                remainder = parts[0].replace("empty", "").trim();
            } else {
                int coordinatesStart = wktText.indexOf("(");
                coordinates = wktText.substring(coordinatesStart);
                remainder = parts[0].trim();
            }

            int firstSpace = remainder.indexOf(" ");

            if (firstSpace != -1) {
                goemetryType = remainder.substring(0, firstSpace);
                dimension = remainder.substring(firstSpace + 1);
            } else {
                goemetryType = remainder;
                //dimension = ""; //Dimension already set to empty, but kept as a reminder.
            }
        }

        return new WKTReader(goemetryType, dimension, coordinates);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.coordinateSequenceDimensions);
        hash = 37 * hash + Objects.hashCode(this.geometry);
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
        final WKTReader other = (WKTReader) obj;
        if (this.coordinateSequenceDimensions != other.coordinateSequenceDimensions) {
            return false;
        }
        return Objects.equals(this.geometry, other.geometry);
    }

    @Override
    public String toString() {
        return "WKTInfo{" + "dimensions=" + coordinateSequenceDimensions + ", geometry=" + geometry + '}';
    }

}
