/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.data_conversion;

import implementation.datatype.GMLDatatype;
import implementation.datatype.WKTDatatype;
import rdfconverter.datatypes.DatatypeController;

/**
 *
 *
 */
public class CSVConversion {

    public static final void registerDatatypes() {
        DatatypeController.addPrefixDatatype("wkt", WKTDatatype.URI);
        DatatypeController.addPrefixDatatype("gml", GMLDatatype.URI);
    }

}
