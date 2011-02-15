package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class VertexEdgeLabelFilterPipeTest extends TestCase {

    public void testLabelFilterEdges() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex(1);
        VertexEdgeLabelFilterPipe vsf = new VertexEdgeLabelFilterPipe(VertexEdgePipe.Step.OUT_EDGES, "knows");
        vsf.setStarts(Arrays.asList(marko).iterator());
        assertTrue(vsf.hasNext());
        int counter = 0;
        while (vsf.hasNext()) {
            Edge e = vsf.next();
            assertEquals(e.getOutVertex(), marko);
            assertTrue(e.getInVertex().getId().equals("2") || e.getInVertex().getId().equals("4"));
            counter++;
        }
        assertEquals(counter, 2);
        try {
            vsf.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }
}
