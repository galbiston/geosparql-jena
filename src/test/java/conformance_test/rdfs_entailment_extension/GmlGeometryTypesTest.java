/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test.rdfs_entailment_extension;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 *
 *
 * A.5.3.1 /conf/rdfs-entailment-extension/gml-geometry-types
 *
 * Requirement: /req/rdfs-entailment-extension/gml-geometry-types
 * Implementations shall support graph patterns involving terms from an RDFS/OWL
 * class hierarchy of geometry types consistent with the GML schema that
 * implements GM_Object using the specified version of GML [OGC 07-036].
 *
 * a.) Test purpose: check conformance with this requirement
 *
 * b.) Test method: Verify that a set of SPARQL queries involving GML Geometry
 * types returns the correct result for a test dataset using the specified
 * version of GML.
 *
 * c.) Reference: Clause 10.4.1 Req 27
 *
 * d.) Test Type: Capabilities
 */
public class GmlGeometryTypesTest {

    @BeforeClass
    public static void setUpClass() {
        /**
         * Initialize all the topology functions.
         */

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

}
