package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BothEdgesPipeTest extends TestCase {

    private final Graph graph = TinkerGraphFactory.createTinkerGraph();

    public void testBothEdgesWithLabel() {
        Pipe<Vertex, Edge> pipe = new BothEdgesPipe("created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f) || weight.equals(0.5f));
        }
        assertEquals(counter, 8);
    }


    public void testBothEdgesWithBranchFactor() {
        Pipe<Vertex, Edge> pipe = new BothEdgesPipe(1, "created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f));
        }
        assertEquals(counter, 5);
    }

    public void testBothEdgesWithBranchFactor2() {
        Pipe<Vertex, Edge> pipe = new BothEdgesPipe(0);
        pipe.setStarts(graph.getVertices());
        assertFalse(pipe.hasNext());
    }

    public void testBothEdgesWithBranchFactor3() {
        Pipe<Vertex, Edge> pipe = new BothEdgesPipe(1);
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f) || weight.equals(0.5f));
        }
        assertEquals(counter, 6);
    }
}
