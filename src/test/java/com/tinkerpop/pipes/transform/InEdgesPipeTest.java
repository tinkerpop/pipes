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
public class InEdgesPipeTest extends TestCase {

    private final Graph graph = TinkerGraphFactory.createTinkerGraph();

    public void testInEdgesWithLabel() {
        Pipe<Vertex, Edge> pipe = new InEdgesPipe("created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f));
        }
        assertEquals(counter, 4);
    }


    public void testInEdgesWithBranchFactor() {
        Pipe<Vertex, Edge> pipe = new InEdgesPipe(1, "created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f));
        }
        assertEquals(counter, 2);
    }

    public void testInWithBranchFactor2() {
        Pipe<Vertex, Edge> pipe = new InEdgesPipe(0);
        pipe.setStarts(graph.getVertices());
        assertFalse(pipe.hasNext());
    }

    public void testOutWithBranchFactor3() {
        Pipe<Vertex, Edge> pipe = new InEdgesPipe(1);
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Float weight = pipe.next().getProperty("weight");
            assertTrue(weight.equals(0.4f) || weight.equals(0.2f) || weight.equals(1.0f) || weight.equals(0.5f));
        }
        assertEquals(counter, 4);
    }
}
