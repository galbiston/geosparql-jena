/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.support.GeoSerialisationEnum;
import org.apache.jena.datatypes.DatatypeFormatException;

/**
 *
 * @author Gregory Albiston
 */
public class DatatypeSelector {

    public static final String unparse(GeometryWrapper geometryWrapper, GeoSerialisationEnum serialisation) {

        String result;
        if (serialisation.equals(GeoSerialisationEnum.WKT)) {
            WKTDatatype datatype = WKTDatatype.theWKTDatatype;
            result = datatype.unparse(geometryWrapper);
        } else {
            GMLDatatype datatype = GMLDatatype.theGMLDatatype;
            result = datatype.unparse(geometryWrapper);
        }
        return result;
    }

    public static final GeometryWrapper parse(String lexicalForm, String datatypeURI) throws DatatypeFormatException {

        switch (datatypeURI) {
            case WKTDatatype.theTypeURI: {
                WKTDatatype datatype = WKTDatatype.theWKTDatatype;
                return datatype.parse(lexicalForm);
            }
            case GMLDatatype.theTypeURI: {
                GMLDatatype datatype = GMLDatatype.theGMLDatatype;
                return datatype.parse(lexicalForm);
            }
            default:
                throw new DatatypeFormatException("Literal is not a WKT or GML Literal.");
        }
    }

}
