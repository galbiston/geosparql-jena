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
     * Sample Data - small and quick
     */
    public static final String SAMPLE = RDFDataLocation.class.getClassLoader().getResource("dataset/testSample.rdf").toString();

    /**
     * Sample Data in WKT - small and quick
     */
    public static final String SAMPLE_WKT = RDFDataLocation.class.getClassLoader().getResource("dataset/sampleWKT.rdf").toString();

    /**
     * ITN data - middle
     */
    public static final String ITNDATA = RDFDataLocation.class.getClassLoader().getResource("dataset/itn.rdf").toString();

    /**
     * Topological data - large
     */
    public static final String TOPODATA = RDFDataLocation.class.getClassLoader().getResource("dataset/topo.rdf").toString();

    /**
     * Linked geodata - extremely large
     */
    public static final String GEODATA = RDFDataLocation.class.getClassLoader().getResource("dataset/linkedgeodata.nt").toString();

    /**
     * GeoSPARQL schema
     */
    public static final String SCHEMA = RDFDataLocation.class.getClassLoader().getResource("schema/geosparql_vocab_all.rdf").toString();
}
