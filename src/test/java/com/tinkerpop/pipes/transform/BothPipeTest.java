package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BothPipeTest extends TestCase {

    private final Graph graph = TinkerGraphFactory.createTinkerGraph();

    public void testBothWithLabel() {
        Pipe<Vertex, Vertex> pipe = new BothPipe("created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko") || name.equals("lop") || name.equals("ripple"));
        }
        assertEquals(counter, 8);
    }


    public void testBothWithBranchFactor() {
        Pipe<Vertex, Vertex> pipe = new BothPipe(1, "created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko") || name.equals("lop") || name.equals("ripple"));
        }
        assertEquals(counter, 5);
    }

    public void testBothWithBranchFactor2() {
        Pipe<Vertex, Vertex> pipe = new BothPipe(0);
        pipe.setStarts(graph.getVertices());
        assertFalse(pipe.hasNext());
    }

    public void testBothWithBranchFactor3() {
        Pipe<Vertex, Vertex> pipe = new BothPipe(1);
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko") || name.equals("lop") || name.equals("ripple") || name.equals("vadas"));
        }
        assertEquals(counter, 6);
    }
}
