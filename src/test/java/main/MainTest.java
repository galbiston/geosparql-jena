/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author haozhechen
 */
public class MainTest {
    
    public MainTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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

    /**
     * Test of main method, of class Main.
     */
    @Test
    @Ignore
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Main.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFullName method, of class Main.
     */
    @Test
    public void testGetFullName() {
        System.out.println("getFullName");
        String firstName = "Greg";
        String secondName = "Albiston";
        Main instance = new Main();
        String expResult = "Greg Albiston";
        String result = instance.getFullName(firstName, secondName);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUserName method, of class Main.
     */
    @Ignore
    @Test
    public void testGetUserName() {
        System.out.println("getUserName");
        String firstName = "";
        String secondName = "";
        Main instance = new Main();
        String expResult = "";
        String result = instance.getUserName(firstName, secondName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
