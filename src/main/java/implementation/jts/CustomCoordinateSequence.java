/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.jts;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import java.util.Arrays;

/**
 *
 * Based on: OGC 06-103r4 http://www.opengeospatial.org/standards/sfa
 *
 * 
 */
public class CustomCoordinateSequence implements CoordinateSequence {

    private final double[] x;
    private final double[] y;
    private final double[] z;
    private final double[] m;
    private final int size;
    private final int coordinateDimension;
    private final int spatialDimension;
    private final CoordinateSequenceDimensions dimensions;

    public enum CoordinateSequenceDimensions {
        XY, XYZ, XYZM, XYM
    };

    public CustomCoordinateSequence() {
        this.size = 0;
        this.x = new double[size];
        this.y = new double[size];
        this.z = new double[size];
        this.m = new double[size];
        this.coordinateDimension = 4;
        this.spatialDimension = 3;
        this.dimensions = CoordinateSequenceDimensions.XYZM;
    }

    public CustomCoordinateSequence(int size, CoordinateSequenceDimensions dimensions) {
        this.size = size;
        this.x = new double[size];
        this.y = new double[size];
        this.z = new double[size];
        this.m = new double[size];

        for (int i = 0; i < size; i++) {
            this.x[i] = Double.NaN;
            this.y[i] = Double.NaN;
            this.z[i] = Double.NaN;
            this.m[i] = Double.NaN;
        }

        int[] dims = getDimensionValues(dimensions);
        this.coordinateDimension = dims[0];
        this.spatialDimension = dims[1];
        this.dimensions = dimensions;

    }

    public CustomCoordinateSequence(int size, int dimension) {
        this.size = size;
        this.x = new double[size];
        this.y = new double[size];
        this.z = new double[size];
        this.m = new double[size];

        for (int i = 0; i < size; i++) {
            this.x[i] = Double.NaN;
            this.y[i] = Double.NaN;
            this.z[i] = Double.NaN;
            this.m[i] = Double.NaN;
        }

        this.coordinateDimension = dimension;

        //Doesn't handle XYM....
        if (dimension == 4) {
            this.spatialDimension = 3;
            this.dimensions = CoordinateSequenceDimensions.XYZM;
        } else {
            this.spatialDimension = dimension;
            if (dimension == 2) {
                this.dimensions = CoordinateSequenceDimensions.XY;
            } else {
                this.dimensions = CoordinateSequenceDimensions.XYZ;
            }

        }
    }

    public CustomCoordinateSequence(CoordinateSequenceDimensions dimensions, String sequence) {

        this.dimensions = dimensions;
        if (!sequence.isEmpty()) {

            String parts[] = sequence.split(",");

            this.size = parts.length;
            this.x = new double[size];
            this.y = new double[size];
            this.z = new double[size];
            this.m = new double[size];

            for (int i = 0; i < size; i++) {
                String part = parts[i].trim();
                String[] coords = part.split(" ");

                switch (dimensions) {
                    default:
                        this.x[i] = Double.parseDouble(coords[0]);
                        this.y[i] = Double.parseDouble(coords[1]);
                        this.z[i] = Double.NaN;
                        this.m[i] = Double.NaN;
                        break;
                    case XYZ:
                        this.x[i] = Double.parseDouble(coords[0]);
                        this.y[i] = Double.parseDouble(coords[1]);
                        this.z[i] = Double.parseDouble(coords[2]);
                        this.m[i] = Double.NaN;
                        break;
                    case XYM:
                        this.x[i] = Double.parseDouble(coords[0]);
                        this.y[i] = Double.parseDouble(coords[1]);
                        this.z[i] = Double.NaN;
                        this.m[i] = Double.parseDouble(coords[2]);
                        break;
                    case XYZM:
                        this.x[i] = Double.parseDouble(coords[0]);
                        this.y[i] = Double.parseDouble(coords[1]);
                        this.z[i] = Double.parseDouble(coords[2]);
                        this.m[i] = Double.parseDouble(coords[3]);
                        break;
                }

            }

        } else {
            this.size = 0;
            this.x = new double[size];
            this.y = new double[size];
            this.z = new double[size];
            this.m = new double[size];
        }

        int[] dims = getDimensionValues(dimensions);
        this.coordinateDimension = dims[0];
        this.spatialDimension = dims[1];

    }

    private static int[] getDimensionValues(CoordinateSequenceDimensions dimensions) {

        int coordinateDimension;
        int spatialDimension;
        switch (dimensions) {
            default:
                coordinateDimension = 2;
                spatialDimension = 2;
                break;
            case XYZ:
                coordinateDimension = 3;
                spatialDimension = 3;
                break;
            case XYM:
                coordinateDimension = 3;
                spatialDimension = 2;
                break;
            case XYZM:
                coordinateDimension = 4;
                spatialDimension = 3;
                break;
        }

        return new int[]{coordinateDimension, spatialDimension};
    }

    public CustomCoordinateSequence(Coordinate[] coordinates) {

        this.size = coordinates.length;
        this.x = new double[size];
        this.y = new double[size];
        this.z = new double[size];
        this.m = new double[size];

        for (int i = 0; i < size; i++) {
            this.x[i] = coordinates[i].x;
            this.y[i] = coordinates[i].y;
            this.z[i] = coordinates[i].z;
            this.m[i] = Double.NaN;
        }

        //Check whether Z coordinateDimension is in use - m cannot be in use with "jts.geom.Coordinate".
        boolean isZPresent = checkDimensionality(this.z);

        if (isZPresent) {
            this.coordinateDimension = 3;
            this.spatialDimension = 3;
            this.dimensions = CoordinateSequenceDimensions.XYZ;
        } else {
            this.coordinateDimension = 2;
            this.spatialDimension = 2;
            this.dimensions = CoordinateSequenceDimensions.XY;
        }

    }

    private CustomCoordinateSequence(double[] x, double[] y, double[] z, double[] m) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.m = m;
        this.size = x.length;

        //Check the dimensionality
        boolean isZPresent = checkDimensionality(this.z);

        boolean isMPresent = checkDimensionality(this.m);

        if (!isZPresent && !isMPresent) {
            this.coordinateDimension = 2;
            this.spatialDimension = 2;
            this.dimensions = CoordinateSequenceDimensions.XY;
        } else if (isZPresent && !isMPresent) {
            this.coordinateDimension = 3;
            this.spatialDimension = 3;
            this.dimensions = CoordinateSequenceDimensions.XYZ;
        } else if (!isZPresent && isMPresent) {
            this.coordinateDimension = 3;
            this.spatialDimension = 2;
            this.dimensions = CoordinateSequenceDimensions.XYM;
        } else {
            this.coordinateDimension = 4;
            this.spatialDimension = 3;
            this.dimensions = CoordinateSequenceDimensions.XYZM;
        }

    }

    private boolean checkDimensionality(double[] dim) {

        if (dim.length > 0) {
            return !Double.isNaN(dim[0]);
        } else {
            return false;
        }

    }

    @Override
    public int getDimension() {
        return coordinateDimension;
    }

    public CoordinateSequenceDimensions getDimensions() {
        return dimensions;
    }

    public int getSpatialDimension() {
        return spatialDimension;
    }

    @Override
    public Coordinate getCoordinate(int i) {
        return new Coordinate(x[i], y[i], z[i]);
    }

    @Override
    public Coordinate getCoordinateCopy(int i) {
        return new Coordinate(x[i], y[i], z[i]);
    }

    @Override
    public void getCoordinate(int index, Coordinate coord) {
        coord.x = x[index];
        coord.y = y[index];
        coord.z = z[index];
    }

    @Override
    public double getX(int index) {
        return x[index];
    }

    @Override
    public double getY(int index) {
        return y[index];
    }

    public double getZ(int index) {
        return z[index];
    }

    public double getM(int index) {
        return m[index];
    }

    @Override
    public double getOrdinate(int index, int ordinateIndex) {
        switch (ordinateIndex) {
            case X:
                return x[index];
            case Y:
                return y[index];
            case Z:
                return z[index];
            case M:
                return m[index];
        }
        return Double.NaN;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void setOrdinate(int index, int ordinateIndex, double value) {

        switch (ordinateIndex) {
            case X:
                x[index] = value;
            case Y:
                y[index] = value;
            case Z:
                z[index] = value;
            case M:
                m[index] = value;
        }
    }

    @Override
    public Coordinate[] toCoordinateArray() {

        Coordinate[] coordinates = new Coordinate[size];

        for (int i = 0; i < size; i++) {
            coordinates[i] = new Coordinate(x[i], y[i], z[i]);
        }

        return coordinates;
    }

    @Override
    public Envelope expandEnvelope(Envelope env) {
        Envelope newEnv = new Envelope(env);

        for (int i = 0; i < size; i++) {
            newEnv.expandToInclude(x[i], y[i]);
        }

        return newEnv;
    }

    @Override
    public CustomCoordinateSequence clone() {
        return new CustomCoordinateSequence(x, y, z, m);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Arrays.hashCode(this.x);
        hash = 67 * hash + Arrays.hashCode(this.y);
        hash = 67 * hash + Arrays.hashCode(this.z);
        hash = 67 * hash + Arrays.hashCode(this.m);
        hash = 67 * hash + this.size;
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
        final CustomCoordinateSequence other = (CustomCoordinateSequence) obj;
        if (this.size != other.size) {
            return false;
        }
        if (!Arrays.equals(this.x, other.x)) {
            return false;
        }
        if (!Arrays.equals(this.y, other.y)) {
            return false;
        }
        if (!Arrays.equals(this.z, other.z)) {
            return false;
        }
        return Arrays.equals(this.m, other.m);
    }

    @Override
    public String toString() {
        return "CustomCoordinateSequence{" + "x=" + Arrays.toString(x) + ", y=" + Arrays.toString(y) + ", z=" + Arrays.toString(z) + ", m=" + Arrays.toString(m) + ", size=" + size + '}';
    }

    public String toWKTText() {

        StringBuilder sb = new StringBuilder();

        if (size != 0) {
            sb.append("(");
            String coordText = getCoordinateText(0);
            sb.append(coordText);

            for (int i = 1; i < size; i++) {

                sb.append(", ");
                coordText = getCoordinateText(i);
                sb.append(coordText);
            }
            sb.append(")");
        } else {
            sb.append("EMPTY");
        }

        return sb.toString();
    }

    public String toGMLText() {
        StringBuilder sb = new StringBuilder();

        if (size != 0) {
            String coordText = getCoordinateText(0);
            sb.append(coordText);

            for (int i = 1; i < size; i++) {

                sb.append(" ");
                coordText = getCoordinateText(i);
                sb.append(coordText);
            }
        } else {
            sb.append("");
        }

        return sb.toString();

    }

    private String getCoordinateText(int index) {

        StringBuilder sb = new StringBuilder();

        String xValue = minimise(x[index]);
        String yValue = minimise(y[index]);
        String zValue;
        String mValue;
        switch (dimensions) {
            case XY:
                sb.append(xValue).append(" ").append(yValue);
                break;
            case XYZ:
                zValue = minimise(z[index]);
                sb.append(xValue).append(" ").append(yValue).append(" ").append(zValue);
                break;
            case XYM:
                mValue = minimise(m[index]);
                sb.append(xValue).append(" ").append(yValue).append(" ").append(mValue);
                break;
            default:
                zValue = minimise(z[index]);
                mValue = minimise(m[index]);
                sb.append(xValue).append(" ").append(yValue).append(" ").append(zValue).append(" ").append(mValue);
                break;

        }
        return sb.toString();
    }

    /**
     * Reduce precision if decimal places are zero.
     *
     * @param value
     * @return
     */
    private String minimise(Double value) {

        long longValue = value.longValue();

        if (value == longValue) {
            return Long.toString(longValue);
        } else {
            return value.toString();
        }
    }

    public static final CoordinateSequenceDimensions findCoordinateSequenceDimensions(int coordinateDimension, int spatialDimension) {
        if (coordinateDimension == 2 && spatialDimension == 2) {
            return CoordinateSequenceDimensions.XY;
        } else if (coordinateDimension == 3 && spatialDimension == 3) {
            return CoordinateSequenceDimensions.XYZ;
        } else if (coordinateDimension == 3 && spatialDimension == 2) {
            return CoordinateSequenceDimensions.XYM;
        } else {
            return CoordinateSequenceDimensions.XYZM;
        }
    }

}
