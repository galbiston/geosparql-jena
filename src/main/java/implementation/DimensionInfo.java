/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

/**
 *
 * @author Gregory Albiston
 */
public class DimensionInfo {

    private final int coordinate;
    private final int spatial;
    private final int topological;

    public DimensionInfo(int coordinate, int spatial, int topological) {
        this.coordinate = coordinate;
        this.spatial = spatial;
        this.topological = topological;
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

}
