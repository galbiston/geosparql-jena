/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import static implementation.datatype.WKTDatatype.DEFAULT_SRS_URI;
import java.util.HashMap;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gregory Albiston
 */
public class CRSRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRSRegistry.class);
    private static final HashMap<String, CoordinateReferenceSystem> CRS_REGISTRY = new HashMap<>();

    static {
        addCRS(DEFAULT_SRS_URI);
    }

    public static final CoordinateReferenceSystem addCRS(String srsURI) {

        CoordinateReferenceSystem crs = null;
        if (!CRS_REGISTRY.containsKey(srsURI)) {

            try {
                crs = CRS.decode(srsURI);
                CRS_REGISTRY.put(srsURI, crs);
                UnitRegistry.addUnits(srsURI, crs);

            } catch (FactoryException ex) {
                LOGGER.error("CRS Parse Error: {} {}", srsURI, ex.getMessage());
            }
        } else {
            crs = CRS_REGISTRY.get(srsURI);
        }
        return crs;
    }

    public static final CoordinateReferenceSystem getCRS(String srsURI) {

        CoordinateReferenceSystem crs = addCRS(srsURI);
        UnitRegistry.addUnits(srsURI, crs);
        return crs;
    }

}
