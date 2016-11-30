/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.jts;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;

/**
 *
 * @author Greg
 */
public class CustomCoordinateSequenceFactory implements CoordinateSequenceFactory {

    @Override
    public CoordinateSequence create(Coordinate[] coordinates) {
        return new CustomCoordinateSequence(coordinates);
    }

    @Override
    public CoordinateSequence create(CoordinateSequence coordSeq) {
        CustomCoordinateSequence copyCoordSeq;

        if (coordSeq == null) {
            copyCoordSeq = new CustomCoordinateSequence();
        } else if (coordSeq instanceof CustomCoordinateSequence) {
            CustomCoordinateSequence customCoordSeq = (CustomCoordinateSequence) coordSeq;
            copyCoordSeq = customCoordSeq.clone();
        } else {
            copyCoordSeq = new CustomCoordinateSequence(coordSeq.toCoordinateArray());
        }

        return copyCoordSeq;
    }

    @Override
    public CoordinateSequence create(int size, int dimension) {
        return new CustomCoordinateSequence(size, dimension);
    }

}
