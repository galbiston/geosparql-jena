/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import static datatype.WKTDatatype.DEFAULT_SRS_URI;
import java.util.HashMap;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Greg
 */
public class CRSRegistry {

    private static final HashMap<String, CoordinateReferenceSystem> REGISTRY = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(CRSRegistry.class);

    static {
        addCRS(DEFAULT_SRS_URI);
    }

    public static final void addCRS(String srsURI) {

        if (!REGISTRY.containsKey(srsURI)) {

            try {
                CoordinateReferenceSystem crs = CRS.decode(srsURI);
                REGISTRY.put(srsURI, crs);
            } catch (FactoryException ex) {
                LOGGER.error("CRS Parse Error: {} {}", DEFAULT_SRS_URI, ex.getMessage());
            }
        }
    }

    public static final CoordinateReferenceSystem getCRS(String srsURI) {

        addCRS(srsURI);
        return REGISTRY.get(srsURI);
    }

    public static final DistanceUnitsEnum extractCRSDistanceUnits(CoordinateReferenceSystem crs) {

        //TODO Extract units from WKT string.
        String wktMetadata = crs.toWKT();

        String units;

        DistanceUnitsEnum distanceUnits = UomConverter.extract(units);

        return distanceUnits;
    }

}
