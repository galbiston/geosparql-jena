/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geor.queryrewrite;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Greg
 */
public class GenericQueryRewriteTest {

    public GenericQueryRewriteTest() {
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
     * Test of execEvaluated method, of class GenericQueryRewrite.
     */
    @Test
    public void testExecEvaluated() {
        System.out.println("execEvaluated");
        Binding binding = null;
        Node subject = NodeFactory.createURI("myExample:subject");
        Node predicate = NodeFactory.createURI("myExample:propertyFunction");
        Node object = NodeFactory.createURI("myExample:object");
        ExecutionContext execCxt = null;
        GenericQueryRewrite instance = new GenericQueryRewrite();
        QueryIterator expResult = null;
        QueryIterator result = instance.execEvaluated(binding, subject, predicate, object, execCxt);

        assertEquals(expResult, result);
    }

}
