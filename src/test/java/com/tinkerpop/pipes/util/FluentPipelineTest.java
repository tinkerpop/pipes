package com.tinkerpop.pipes.util;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.AbstractPipeClosure;
import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FluentPipelineTest extends TestCase {

    public void testBasicFluentPipeline() {
        Graph g = TinkerGraphFactory.createTinkerGraph();
        FluentPipeline<Vertex, String> pipeline = new FluentPipeline<Vertex, String>();
        pipeline.start(g.getVertex(1)).out("knows").property("name");
        int counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            String name = pipeline.next();
            assertTrue(name.equals("josh") || name.equals("vadas"));
        }
        assertEquals(counter, 2);
    }

    public void testFilterFluentPipeline() {
        Graph g = TinkerGraphFactory.createTinkerGraph();
        FluentPipeline<Vertex, String> pipeline = new FluentPipeline<Vertex, String>();
        pipeline.start(g.getVertex(1)).out("knows").property("name").filter(new AbstractPipeClosure<Boolean, Pipe>() {
            public Boolean compute(Object... objects) {
                return ((String) objects[0]).startsWith("j");
            }
        });
        int counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            String name = pipeline.next();
            assertTrue(name.equals("josh"));
        }
        assertEquals(counter, 1);
    }
}
