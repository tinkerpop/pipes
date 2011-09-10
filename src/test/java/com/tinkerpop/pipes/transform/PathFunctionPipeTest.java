package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.SingleIterator;
import junit.framework.TestCase;

import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathFunctionPipeTest extends TestCase {

    public void testPipeBasic() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, List> pipeline = new Pipeline<Vertex, List>(new OutPipe(), new PathFunctionPipe<Vertex>(new NamePipeFunction()));
        pipeline.setStarts(new SingleIterator<Vertex>(marko));
        int counter = 0;
        for (List path : pipeline) {
            assertEquals(path.get(0), "marko");
            assertTrue(path.get(1).equals("vadas") || path.get(1).equals("josh") || path.get(1).equals("lop"));
            counter++;
        }
        assertEquals(counter, 3);
    }

    private class NamePipeFunction implements PipeFunction<Vertex, String> {
        public String compute(Vertex argument) {
            return (String) argument.getProperty("name");
        }
    }
}
