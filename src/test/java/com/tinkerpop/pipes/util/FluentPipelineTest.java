package com.tinkerpop.pipes.util;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.AbstractPipeClosure;
import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FluentPipelineTest extends TestCase {

    public void testBasic() {
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

    public void testFilterClosureUsingInnerClass() {
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

    public void testFilterClosureUsingPipeHelper() throws Exception {
        Graph g = TinkerGraphFactory.createTinkerGraph();
        FluentPipeline<Vertex, String> pipeline =
                new FluentPipeline<Vertex, String>().start(g.getVertex(1)).out("knows").property("name").filter(PipeHelper.createPipeClosure(FluentPipelineTest.class.getMethod("startsWithJ", String.class)));
        int counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            String name = pipeline.next();
            assertTrue(name.equals("josh"));
        }
        assertEquals(counter, 1);
    }

    public static boolean startsWithJ(final String string) {
        return string.startsWith("j");
    }

    public void testCoDevelopers() {
        Graph g = TinkerGraphFactory.createTinkerGraph();
        List<String> coDevelopers =
                new FluentPipeline<Vertex, String>().start(g.getVertex(1)).out("created").in("created").except(Arrays.asList(g.getVertex(1))).property("name").toList();

        assertEquals(coDevelopers.size(), 2);
        assertTrue(coDevelopers.contains("josh"));
        assertTrue(coDevelopers.contains("peter"));
    }


}
