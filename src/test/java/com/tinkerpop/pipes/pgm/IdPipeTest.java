package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.BaseTest;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdPipeTest extends BaseTest {

    public void testIds() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        IdPipe pipe = new IdPipe();
        pipe.setStarts((Iterable) graph.getVertex("1").getOutEdges());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            Object id = pipe.next();
            assertTrue(id.equals("7") || id.equals("8") || id.equals("9"));
            counter++;
        }
        assertEquals(counter, 3);
    }
}
