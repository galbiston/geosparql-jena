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
package io.github.galbiston.geosparql_jena.implementation;

import io.github.galbiston.geosparql_jena.implementation.jts.CoordinateSequenceDimensions;
import static io.github.galbiston.geosparql_jena.implementation.jts.CustomCoordinateSequence.findCoordinateSequenceDimensions;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.CoordinateXYZM;

/**
 *
 *
 */
public class DimensionInfo implements Serializable {

    private final int coordinate;
    private final int spatial;
    private final int topological;
    private final CoordinateSequenceDimensions coordinateSequenceDimensions;

    private final boolean isPoint;
    private final boolean isLine;
    private final boolean isArea;

    public DimensionInfo(CoordinateSequenceDimensions coordinateSequenceDimensions, int topological) {
        this.coordinateSequenceDimensions = coordinateSequenceDimensions;
        this.topological = topological;
        this.coordinate = findCoordinateDimension(coordinateSequenceDimensions);
        this.spatial = findSpatialDimension(coordinateSequenceDimensions);
        this.isPoint = topological == 0;
        this.isLine = topological == 1;
        this.isArea = topological == 2;
    }

    public DimensionInfo(int coordinate, int spatial, int topological) {
        this.coordinate = coordinate;
        this.spatial = spatial;
        this.topological = topological;
        this.coordinateSequenceDimensions = findCoordinateSequenceDimensions(coordinate, spatial);
        this.isPoint = topological == 0;
        this.isLine = topological == 1;
        this.isArea = topological == 2;
    }

    public static int findSpatialDimension(CoordinateSequenceDimensions dims) {

        switch (dims) {
            case XYZ:
            case XYZM:
                return 3;
            default:
                return 2;
        }
    }

    public static int findCoordinateDimension(CoordinateSequenceDimensions dims) {
        switch (dims) {
            case XYZ:
            case XYM:
                return 3;
            case XYZM:
                return 4;
            default:
                return 2;
        }
    }

    public static DimensionInfo findForPoint(Coordinate coordinate) {

        if (coordinate instanceof CoordinateXY) {
            return XY_POINT;
        } else if (coordinate instanceof CoordinateXYM) {
            return XYM_POINT;
        } else if (coordinate instanceof CoordinateXYZM) {
            return XYZM_POINT;
        }

        //Coordinate can be either XY or XYZ.
        if (Double.isNaN(coordinate.getZ())) {
            return XY_POINT;
        } else {
            return XYZ_POINT;
        }
    }

    public static DimensionInfo findForLineString(List<Coordinate> coordinates) {

        if (coordinates.isEmpty()) {
            return XY_LINESTRING;
        }

        Coordinate coordinate = coordinates.get(0);

        if (coordinate instanceof CoordinateXY) {
            return XY_LINESTRING;
        } else if (coordinate instanceof CoordinateXYM) {
            return XYM_LINESTRING;
        } else if (coordinate instanceof CoordinateXYZM) {
            return XYZM_LINESTRING;
        }

        //Coordinate can be either XY or XYZ.
        if (Double.isNaN(coordinate.getZ())) {
            return XY_LINESTRING;
        } else {
            return XYZ_LINESTRING;
        }
    }

    public static DimensionInfo findForPolygon(List<Coordinate> coordinates) {
        if (coordinates.isEmpty()) {
            return XY_POLYGON;
        }

        Coordinate coordinate = coordinates.get(0);

        if (coordinate instanceof CoordinateXY) {
            return XY_POLYGON;
        } else if (coordinate instanceof CoordinateXYM) {
            return XYM_POLYGON;
        } else if (coordinate instanceof CoordinateXYZM) {
            return XYZM_POLYGON;
        }

        //Coordinate can be either XY or XYZ.
        if (Double.isNaN(coordinate.getZ())) {
            return XY_POLYGON;
        } else {
            return XYZ_POLYGON;
        }
    }

    public int getCoordinate() {
        return coordinate;
    }

    public int getSpatial() {
        return spatial;
    }

    public int getTopological() {
        return topological;
    }

    public boolean isPoint() {
        return isPoint;
    }

    public boolean isLine() {
        return isLine;
    }

    public boolean isArea() {
        return isArea;
    }

    public CoordinateSequenceDimensions getDimensions() {
        return coordinateSequenceDimensions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.coordinate;
        hash = 53 * hash + this.spatial;
        hash = 53 * hash + this.topological;
        hash = 53 * hash + Objects.hashCode(this.coordinateSequenceDimensions);
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
        final DimensionInfo other = (DimensionInfo) obj;
        if (this.coordinate != other.coordinate) {
            return false;
        }
        if (this.spatial != other.spatial) {
            return false;
        }
        if (this.topological != other.topological) {
            return false;
        }
        return this.coordinateSequenceDimensions == other.coordinateSequenceDimensions;
    }

    @Override
    public String toString() {
        return "DimensionInfo{" + "coordinate=" + coordinate + ", spatial=" + spatial + ", topological=" + topological + ", coordinateSequenceDimensions=" + coordinateSequenceDimensions + ", isPoint=" + isPoint + ", isLine=" + isLine + ", isArea=" + isArea + '}';
    }

    public static DimensionInfo XY_POINT = new DimensionInfo(2, 2, 0);

    public static DimensionInfo XYZ_POINT = new DimensionInfo(3, 3, 0);

    public static DimensionInfo XYM_POINT = new DimensionInfo(3, 2, 0);

    public static DimensionInfo XYZM_POINT = new DimensionInfo(4, 3, 0);

    public static DimensionInfo XY_LINESTRING = new DimensionInfo(2, 2, 1);

    public static DimensionInfo XYZ_LINESTRING = new DimensionInfo(3, 3, 1);

    public static DimensionInfo XYM_LINESTRING = new DimensionInfo(3, 2, 1);

    public static DimensionInfo XYZM_LINESTRING = new DimensionInfo(4, 3, 1);

    public static DimensionInfo XY_POLYGON = new DimensionInfo(2, 2, 2);

    public static DimensionInfo XYZ_POLYGON = new DimensionInfo(3, 3, 2);

    public static DimensionInfo XYM_POLYGON = new DimensionInfo(3, 2, 2);

    public static DimensionInfo XYZM_POLYGON = new DimensionInfo(4, 3, 2);

}
