package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.BaseTest;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelPipeTest extends BaseTest {

    public void testLabels() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        LabelPipe pipe = new LabelPipe();
        pipe.setStarts(graph.getVertex("1").getOutEdges().iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            String label = pipe.next();
            assertTrue(label.equals("knows") || label.equals("created"));
            counter++;
        }
        assertEquals(counter, 3);
    }
}
