package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.SingleIterator;
import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipeTest extends TestCase {

    public void testBasicGather() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe0 = new OutEdgesPipe();
        Pipe pipe1 = new InVertexPipe();
        Pipe pipe2 = new GatherPipe();
        Pipe pipeline = new Pipeline(pipe0, pipe1, pipe2);
        pipeline.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        while (pipeline.hasNext()) {
            System.out.println(pipeline.next() + "--->" + pipeline.getPath());
        }
    }

}
