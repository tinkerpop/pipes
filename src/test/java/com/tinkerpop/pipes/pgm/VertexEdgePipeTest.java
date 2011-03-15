package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class VertexEdgePipeTest extends TestCase {

    public void testOutGoingEdges() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        VertexEdgePipe vsf = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        vsf.setStarts(Arrays.asList(marko).iterator());
        assertTrue(vsf.hasNext());
        int counter = 0;
        while (vsf.hasNext()) {
            Edge e = vsf.next();
            assertEquals(e.getOutVertex(), marko);
            assertTrue(e.getInVertex().getId().equals("2") || e.getInVertex().getId().equals("3") || e.getInVertex().getId().equals("4"));
            counter++;
        }
        assertEquals(counter, 3);
        try {
            vsf.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }

        Vertex josh = graph.getVertex("4");
        vsf = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        vsf.setStarts(Arrays.asList(josh).iterator());
        assertTrue(vsf.hasNext());
        counter = 0;
        while (vsf.hasNext()) {
            Edge e = vsf.next();
            assertEquals(e.getOutVertex(), josh);
            assertTrue(e.getInVertex().getId().equals("5") || e.getInVertex().getId().equals("3"));
            counter++;
        }
        assertEquals(counter, 2);
        try {
            vsf.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }

        Vertex lop = graph.getVertex("3");
        vsf = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        vsf.setStarts(Arrays.asList(lop).iterator());
        assertFalse(vsf.hasNext());
        counter = 0;
        while (vsf.hasNext()) {
            counter++;
        }
        assertEquals(counter, 0);
        try {
            vsf.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }

    public void testInEdges() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex josh = graph.getVertex("4");
        VertexEdgePipe pipe = new VertexEdgePipe(VertexEdgePipe.Step.IN_EDGES);
        pipe.setStarts(new SingleIterator<Vertex>(josh));
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Edge edge = pipe.next();
            assertEquals(edge.getId(), "8");
        }
        assertEquals(counter, 1);
    }

    public void testBothEdges() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex josh = graph.getVertex("4");
        VertexEdgePipe pipe = new VertexEdgePipe(VertexEdgePipe.Step.BOTH_EDGES);
        pipe.setStarts(new SingleIterator<Vertex>(josh));
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Edge edge = pipe.next();
            assertTrue(edge.getId().equals("8") || edge.getId().equals("10") || edge.getId().equals("11"));
        }
        assertEquals(counter, 3);
    }

    public void testBigGraphWithNoEdges() {
        // This used to cause a stack overflow. Not crashing makes this a success.
        TinkerGraph graph = new TinkerGraph();
        for (int i = 0; i < 10000; i++) {
            graph.addVertex(null);
        }
        Pipe<Graph, Vertex> vertices = new GraphElementPipe<Vertex>(GraphElementPipe.ElementType.VERTEX);
        vertices.setStarts(new SingleIterator<Graph>(graph));
        VertexEdgePipe outEdges = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        outEdges.setStarts(vertices);
        int counter = 0;
        while (outEdges.hasNext()) {
            outEdges.next();
            counter++;
        }
        assertEquals(counter, 0);
    }
}
