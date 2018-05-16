/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.datatype;

import implementation.GeometryWrapper;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;

/**
 *
 *
 */
public abstract class GeometryDatatype extends BaseDatatype implements DatatypeReader {

    public GeometryDatatype(String uri) {
        super(uri);
    }

    @Override
    public abstract GeometryWrapper parse(String lexicalForm) throws DatatypeFormatException;

}
