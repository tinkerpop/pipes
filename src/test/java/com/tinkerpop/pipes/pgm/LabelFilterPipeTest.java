package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;
import junit.framework.TestCase;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelFilterPipeTest extends TestCase {

    public void testFilterLabels() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        LabelFilterPipe lfp = new LabelFilterPipe("knows", ComparisonFilterPipe.Filter.NOT_EQUAL);
        lfp.setStarts(marko.getOutEdges().iterator());
        assertTrue(lfp.hasNext());
        int counter = 0;
        while (lfp.hasNext()) {
            Edge e = lfp.next();
            assertEquals(e.getOutVertex(), marko);
            assertTrue(e.getInVertex().getId().equals("2") || e.getInVertex().getId().equals("4"));
            counter++;
        }
        assertEquals(counter, 2);

        lfp = new LabelFilterPipe("knows", ComparisonFilterPipe.Filter.EQUAL);
        lfp.setStarts(marko.getOutEdges().iterator());
        assertTrue(lfp.hasNext());
        counter = 0;
        while (lfp.hasNext()) {
            Edge e = lfp.next();
            assertEquals(e.getOutVertex(), marko);
            assertTrue(e.getInVertex().getId().equals("3"));
            counter++;
        }
        assertEquals(counter, 1);

        // todo: get AND and OR working!
        /*lfp = new LabelFilterPipe(Arrays.asList("knows", "created"), ComparisonFilterPipe.Filter.DISALLOW);
        lfp.setStarts(marko.getOutEdges().iterator());
        assertFalse(lfp.hasNext());

        lfp = new LabelFilterPipe(Arrays.asList("knows", "created"), ComparisonFilterPipe.Filter.ALLOW);
        lfp.setStarts(marko.getOutEdges().iterator());
        assertTrue(lfp.hasNext());
        counter = 0;
        while (lfp.hasNext()) {
            Edge e = lfp.next();
            assertEquals(e.getOutVertex(), marko);
            counter++;
        }
        assertEquals(counter, 3);
        try {
            lfp.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }*/

    }
}
