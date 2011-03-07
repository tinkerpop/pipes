package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OutEdgesPipeTest extends TestCase {

    public void testOutGoingEdges() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        OutEdgesPipe pipe = new OutEdgesPipe();
        pipe.setStarts(Arrays.asList(marko).iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            Edge e = pipe.next();
            assertEquals(e.getOutVertex(), marko);
            assertTrue(e.getInVertex().getId().equals("2") || e.getInVertex().getId().equals("3") || e.getInVertex().getId().equals("4"));
            counter++;
        }
        assertEquals(counter, 3);
        try {
            pipe.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }

        Vertex josh = graph.getVertex("4");
        pipe = new OutEdgesPipe();
        pipe.setStarts(Arrays.asList(josh).iterator());
        assertTrue(pipe.hasNext());
        counter = 0;
        while (pipe.hasNext()) {
            Edge e = pipe.next();
            assertEquals(e.getOutVertex(), josh);
            assertTrue(e.getInVertex().getId().equals("5") || e.getInVertex().getId().equals("3"));
            counter++;
        }
        assertEquals(counter, 2);
        try {
            pipe.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }

        Vertex lop = graph.getVertex("3");
        pipe = new OutEdgesPipe();
        pipe.setStarts(Arrays.asList(lop).iterator());
        assertFalse(pipe.hasNext());
        counter = 0;
        while (pipe.hasNext()) {
            counter++;
        }
        assertEquals(counter, 0);
        try {
            pipe.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }

    public void testBigGraphWithNoEdges() {
        // This used to cause a stack overflow. Not crashing makes this a success.
        TinkerGraph graph = new TinkerGraph();
        for (int i = 0; i < 100000; i++) {
            graph.addVertex(null);
        }
        OutEdgesPipe outEdges = new OutEdgesPipe();
        outEdges.setStarts(graph.getVertices());
        int counter = 0;
        while (outEdges.hasNext()) {
            outEdges.next();
            counter++;
        }
        assertEquals(counter, 0);
    }

    public void testLabelFilterEdges() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex(1);
        Pipe<Vertex, Edge> pipe = new OutEdgesPipe("knows");
        pipe.setStarts(Arrays.asList(marko).iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            Edge e = pipe.next();
            assertEquals(e.getOutVertex(), marko);
            assertTrue(e.getInVertex().getId().equals("2") || e.getInVertex().getId().equals("4"));
            counter++;
        }
        assertEquals(counter, 2);
        try {
            pipe.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);

        }
    }
}