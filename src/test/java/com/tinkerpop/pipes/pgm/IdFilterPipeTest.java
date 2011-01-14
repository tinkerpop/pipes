package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdFilterPipeTest extends TestCase {

    public void testFilterIds1() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        VertexEdgePipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        EdgeVertexPipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        IdFilterPipe pipe3 = new IdFilterPipe("3", ComparisonFilterPipe.Filter.NOT_EQUAL);
        Pipeline<Vertex, Vertex> pipeline = new Pipeline<Vertex, Vertex>(pipe1, pipe2, pipe3);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            Vertex vertex = pipeline.next();
            assertEquals(vertex.getProperty("name"), "lop");
            counter++;
        }
        assertEquals(counter, 1);
    }

    public void testFilterIds2() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        VertexEdgePipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        EdgeVertexPipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        IdFilterPipe pipe3 = new IdFilterPipe("3", ComparisonFilterPipe.Filter.EQUAL);
        Pipeline<Vertex, Vertex> pipeline = new Pipeline<Vertex, Vertex>(pipe1, pipe2, pipe3);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            Vertex vertex = pipeline.next();
            assertTrue(vertex.getProperty("name").equals("vadas") || vertex.getProperty("name").equals("josh"));
            counter++;
        }
        assertEquals(counter, 2);
    }
}
