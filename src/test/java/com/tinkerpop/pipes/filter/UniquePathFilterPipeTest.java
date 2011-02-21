package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.SingleIterator;
import com.tinkerpop.pipes.pgm.EdgeVertexPipe;
import com.tinkerpop.pipes.pgm.VertexEdgePipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class UniquePathFilterPipeTest extends BaseTest {

    public void testUniquePathFilter() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe pipe3 = new VertexEdgePipe(VertexEdgePipe.Step.IN_EDGES);
        Pipe pipe4 = new EdgeVertexPipe(EdgeVertexPipe.Step.OUT_VERTEX);
        Pipe pipe5 = new UniquePathFilterPipe();
        Pipe pipeline = new Pipeline(pipe1, pipe2, pipe3, pipe4, pipe5);
        pipeline.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        int counter = 0;
        for (Object object : pipeline) {
            counter++;
            assertTrue(object.equals(graph.getVertex(6)) || object.equals(graph.getVertex(4)));
        }
        assertEquals(counter, 2);
    }

}
