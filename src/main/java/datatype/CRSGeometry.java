/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatype;

import com.vividsolutions.jts.geom.Geometry;
import java.util.Objects;

/**
 *
 * @author Greg
 */
public class CRSGeometry {

    private final Geometry geometry;
    private final String srsName;
    private final GeoSerialisation serialisation;

    public CRSGeometry(Geometry geometry, String srsName, GeoSerialisation serialisation) {
        this.geometry = geometry;
        this.srsName = srsName;
        this.serialisation = serialisation;

        CRSRegistry.addCRS(srsName);
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getSrsName() {
        return srsName;
    }

    public GeoSerialisation getSerialisation() {
        return serialisation;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.geometry);
        hash = 41 * hash + Objects.hashCode(this.srsName);
        hash = 41 * hash + Objects.hashCode(this.serialisation);
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
        final CRSGeometry other = (CRSGeometry) obj;
        if (!Objects.equals(this.srsName, other.srsName)) {
            return false;
        }
        if (!Objects.equals(this.geometry, other.geometry)) {
            return false;
        }
        return this.serialisation == other.serialisation;
    }

    @Override
    public String toString() {
        return "CRSGeometry{" + "geometry=" + geometry + ", srsName=" + srsName + ", serialisation=" + serialisation + '}';
    }

}
