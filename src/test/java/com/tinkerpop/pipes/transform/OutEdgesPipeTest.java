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
public class OutEdgesPipeTest extends TestCase {

    private final Graph graph = TinkerGraphFactory.createTinkerGraph();

    public void testOutEdgesWithLabel() {
        Pipe<Vertex, Edge> pipe = new OutEdgesPipe("created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f));
        }
        assertEquals(counter, 4);
    }

    public void testOutWithBranchFactor() {
        Pipe<Vertex, Edge> pipe = new OutEdgesPipe(1, "created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f));
        }
        assertEquals(counter, 3);
    }

    public void testOutWithBranchFactor2() {
        Pipe<Vertex, Edge> pipe = new OutEdgesPipe(0);
        pipe.setStarts(graph.getVertices());
        assertFalse(pipe.hasNext());
    }

    public void testOutEdgesWithBranchFactor3() {
        Pipe<Vertex, Edge> pipe = new OutEdgesPipe(1);
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f) || weight.equals(0.5f));
        }
        assertEquals(counter, 3);
    }
}
