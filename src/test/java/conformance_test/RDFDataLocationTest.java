/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test;

/**
 *
 * @author haozhechen
 */
public class RDFDataLocationTest {

    /**
     * Sample Data in WKT - small and quick
     */
    public static final String SAMPLE_WKT = RDFDataLocationTest.class.getClassLoader().getResource("dataset/sampleWKT.rdf").toString();

    /**
     * Empty WKT Data - test for empty geometry and default SRID return
     */
    public static final String SAMPLE_WKT_EMPTY = RDFDataLocationTest.class.getClassLoader().getResource("dataset/sampleWKTEmpty.rdf").toString();

    /**
     * Sample Data in GML - small and quick
     */
    public static final String SAMPLE_GML = RDFDataLocationTest.class.getClassLoader().getResource("dataset/sampleGML.rdf").toString();

    /**
     * Empty GML Data - test for empty geometry and default SRID return
     */
    public static final String SAMPLE_GML_EMPTY = RDFDataLocationTest.class.getClassLoader().getResource("dataset/sampleGMLEmpty.rdf").toString();

}
