/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import implementation.GeometryWrapper;

/**
 *
 * @author Gerg
 */
public interface DatatypeReader {

    public GeometryWrapper read(String geometryLiteral);

}
