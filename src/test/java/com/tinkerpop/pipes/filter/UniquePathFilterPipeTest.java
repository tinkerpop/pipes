package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.BaseTest;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class UniquePathFilterPipeTest extends BaseTest {

    /*  public void testUniquePathFilter() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe1 = new OutEdgesPipe();
        Pipe pipe2 = new InVertexPipe();
        Pipe pipe3 = new InEdgesPipe();
        Pipe pipe4 = new OutVertexPipe();
        Pipe pipe5 = new UniquePathFilterPipe();
        Pipe pipeline = new Pipeline(pipe1, pipe2, pipe3, pipe4, pipe5);
        pipeline.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        int counter = 0;
        for (Object object : pipeline) {
            counter++;
            assertTrue(object.equals(graph.getVertex(6)) || object.equals(graph.getVertex(4)));
        }
        assertEquals(counter, 2);
    }*/

    public void testTrue() {
        assertTrue(true);
    }
}
