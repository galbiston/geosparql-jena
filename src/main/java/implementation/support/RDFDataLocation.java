/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.support;

/**
 *
 * @author haozhechen
 * @author Gregory Albiston
 */
public class RDFDataLocation {

    /**
     * Sample Data in WKT - small and quick
     *
     * @deprecated
     */
    public static final String SAMPLE_WKT = RDFDataLocation.class.getClassLoader().getResource("dataset/sampleWKT.rdf").toString();

    /**
     * Linked geodata - extremely large
     *
     * @deprecated
     */
    public static final String GEODATA = RDFDataLocation.class.getClassLoader().getResource("dataset/linkedgeodata.nt").toString();

    /**
     * GeoSPARQL schema
     */
    public static final String SCHEMA = RDFDataLocation.class.getClassLoader().getResource("schema/geosparql_vocab_all.rdf").toString();

}
