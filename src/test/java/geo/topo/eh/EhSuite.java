/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.topo.eh;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author haozhechen
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({geo.topo.eh.EHCoveredByFilterFuncTest.class, geo.topo.eh.EHEqualFilterFuncTest.class, geo.topo.eh.EHCoversFilterFuncTest.class, geo.topo.eh.EHDisjointFilterFuncTest.class, geo.topo.eh.EHContainsFilterFuncTest.class, geo.topo.eh.EHInsideFilterFuncTest.class, geo.topo.eh.EHOverlapFilterFuncTest.class, geo.topo.eh.EHMeetFilterFuncTest.class})
public class EhSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
