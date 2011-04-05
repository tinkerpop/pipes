package com.tinkerpop.pipes.util;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.SingleIterator;
import com.tinkerpop.pipes.pgm.EdgeVertexPipe;
import com.tinkerpop.pipes.pgm.VertexEdgePipe;
import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipeTest extends TestCase {

    public void testBasicGather() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe0 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe pipe1 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe pipe2 = new GatherPipe();
        Pipe pipeline = new Pipeline(pipe0, pipe1, pipe2);
        pipeline.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        while (pipeline.hasNext()) {
            System.out.println(pipeline.next() + "--->" + pipeline.getPath());
        }
    }

}
