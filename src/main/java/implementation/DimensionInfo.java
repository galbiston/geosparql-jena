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
package implementation;

import implementation.jts.CustomCoordinateSequence.CoordinateSequenceDimensions;
import static implementation.jts.CustomCoordinateSequence.findCoordinateSequenceDimensions;
import java.io.Serializable;

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

    public DimensionInfo(int coordinate, int spatial, int topological) {
        this.coordinate = coordinate;
        this.spatial = spatial;
        this.topological = topological;
        this.coordinateSequenceDimensions = findCoordinateSequenceDimensions(coordinate, spatial);
        this.isPoint = topological == 0;
        this.isLine = topological == 1;
        this.isArea = topological == 2;
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
        int hash = 5;
        hash = 97 * hash + this.coordinate;
        hash = 97 * hash + this.spatial;
        hash = 97 * hash + this.topological;
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
        return this.topological == other.topological;
    }

    @Override
    public String toString() {
        return "DimensionInfo{" + "coordinate=" + coordinate + ", spatial=" + spatial + ", topological=" + topological + '}';
    }

    public static DimensionInfo xyPoint() {
        return new DimensionInfo(2, 2, 0);
    }

    public static DimensionInfo xyzPoint() {
        return new DimensionInfo(3, 3, 0);
    }

}
