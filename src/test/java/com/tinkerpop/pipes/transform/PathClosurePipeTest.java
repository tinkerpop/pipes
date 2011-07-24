package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.SingleIterator;
import junit.framework.TestCase;

import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathClosurePipeTest extends TestCase {

    public void testPipeBasic() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, List> pipeline = new Pipeline<Vertex, List>(new OutPipe(), new PathClosurePipe<Vertex>(new NamePipeClosure()));
        pipeline.setStarts(new SingleIterator<Vertex>(marko));
        int counter = 0;
        for (List path : pipeline) {
            assertEquals(path.get(0), "marko");
            assertTrue(path.get(1).equals("vadas") || path.get(1).equals("josh") || path.get(1).equals("lop"));
            counter++;
        }
        assertEquals(counter, 3);
    }

    private class NamePipeClosure implements PipeClosure<String, PathClosurePipe> {
        public String compute(Object... parameters) {
            Vertex vertex = (Vertex) parameters[0];
            return (String) vertex.getProperty("name");
        }

        public void setPipe(PathClosurePipe pipe) {

        }
    }
}
