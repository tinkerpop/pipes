package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.SingleIterator;
import junit.framework.TestCase;

import java.util.NoSuchElementException;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class EdgeVertexPipeTest extends TestCase {

    public void testInCommingVertex() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        EdgeVertexPipe evp = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        evp.setStarts(marko.getOutEdges());
        assertTrue(evp.hasNext());
        int counter = 0;
        while (evp.hasNext()) {
            Vertex v = evp.next();
            assertTrue(v.getId().equals("2") || v.getId().equals("3") || v.getId().equals("4"));
            counter++;
        }
        assertEquals(counter, 3);

        Vertex josh = graph.getVertex("4");
        evp = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        evp.setStarts(josh.getOutEdges().iterator());
        assertTrue(evp.hasNext());
        counter = 0;
        while (evp.hasNext()) {
            Vertex v = evp.next();
            assertTrue(v.getId().equals("5") || v.getId().equals("3"));
            counter++;
        }
        assertEquals(counter, 2);
        try {
            evp.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }

    public void testBothVertices() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex josh = graph.getVertex("4");
        Edge tempEdge = null;
        for (Edge edge : josh.getOutEdges()) {
            if (edge.getId().equals("11"))
                tempEdge = edge;
        }
        EdgeVertexPipe pipe = new EdgeVertexPipe(EdgeVertexPipe.Step.BOTH_VERTICES);
        pipe.setStarts(new SingleIterator<Edge>(tempEdge));
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Vertex vertex = pipe.next();
            assertTrue(vertex.getId().equals("4") || vertex.getId().equals("3"));
        }
        assertEquals(counter, 2);

    }
}
