package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class InPipeTest extends TestCase {

    private final Graph graph = TinkerGraphFactory.createTinkerGraph();

    public void testInWithLabel() {
        Pipe<Vertex, Vertex> pipe = new InPipe("created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko"));
        }
        assertEquals(counter, 4);
    }


    public void testInWithBranchFactor() {
        Pipe<Vertex, Vertex> pipe = new InPipe(1, "created");
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko"));
        }
        assertEquals(counter, 2);
    }

    public void testInWithBranchFactor2() {
        Pipe<Vertex, Vertex> pipe = new InPipe(0);
        pipe.setStarts(graph.getVertices());
        assertFalse(pipe.hasNext());
    }

    public void testOutWithBranchFactor3() {
        Pipe<Vertex, Vertex> pipe = new InPipe(1);
        pipe.setStarts(graph.getVertices());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next().getProperty("name");
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko"));
        }
        assertEquals(counter, 4);
    }
}
