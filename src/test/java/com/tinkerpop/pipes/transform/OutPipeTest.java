package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OutPipeTest extends TestCase {

    private final Graph graph = TinkerGraphFactory.createTinkerGraph();

    public void testOutWithLabel() {
        Pipe<Vertex, Vertex> pipe = new OutPipe("created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("lop") || name.equals("ripple"));
        }
        assertEquals(counter, 4);
    }

    public void testOutWithBranchFactor() {
        Pipe<Vertex, Vertex> pipe = new OutPipe(1, "created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("lop") || name.equals("ripple"));
        }
        assertEquals(counter, 3);
    }

    public void testOutWithBranchFactor2() {
        Pipe<Vertex, Vertex> pipe = new OutPipe(0);
        pipe.setStarts(graph.getVertices());
        assertFalse(pipe.hasNext());
    }

    public void testOutWithBranchFactor3() {
        Pipe<Vertex, Vertex> pipe = new OutPipe(1);
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("lop") || name.equals("ripple") || name.equals("vadas") || name.equals("josh"));
        }
        assertEquals(counter, 3);
    }
}
