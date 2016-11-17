/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import com.vividsolutions.jts.geom.Geometry;
import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import implementation.support.GeoSerialisationEnum;
import org.apache.jena.datatypes.DatatypeFormatException;

/**
 *
 * @author Gregory Albiston
 */
public class DatatypeSelector {

    public static final String unparse(Geometry geometry, GeoSerialisationEnum serialisation) {

        String result;
        if (serialisation.equals(GeoSerialisationEnum.WKT)) {
            WKTDatatype datatype = WKTDatatype.theWKTDatatype;
            result = datatype.unparse(geometry);
        } else {
            GMLDatatype datatype = GMLDatatype.theGMLDatatype;
            result = datatype.unparse(geometry);
        }
        return result;
    }

    public static final GeometryWrapper parse(String lexicalForm, String datatypeURI) throws DatatypeFormatException {

        if (datatypeURI.equals(WKTDatatype.theTypeURI)) {
            WKTDatatype datatype = WKTDatatype.theWKTDatatype;
            return datatype.parse(lexicalForm);
        } else if (datatypeURI.equals(GMLDatatype.theTypeURI)) {
            GMLDatatype datatype = GMLDatatype.theGMLDatatype;
            return datatype.parse(lexicalForm);
        } else {
            throw new DatatypeFormatException("Literal is not a WKT or GML Literal.");
        }
    }

}
