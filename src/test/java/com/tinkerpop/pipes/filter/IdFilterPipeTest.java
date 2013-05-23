package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Query;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.transform.EdgesVerticesPipe;
import com.tinkerpop.pipes.transform.VerticesEdgesPipe;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdFilterPipeTest extends TestCase {

    public void testFilterIds1() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        VerticesEdgesPipe pipe1 = new VerticesEdgesPipe(Direction.OUT);
        EdgesVerticesPipe pipe2 = new EdgesVerticesPipe(Direction.IN);
        IdFilterPipe pipe3 = new IdFilterPipe(Query.Compare.EQUAL, "3");
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
        VerticesEdgesPipe pipe1 = new VerticesEdgesPipe(Direction.OUT);
        EdgesVerticesPipe pipe2 = new EdgesVerticesPipe(Direction.IN);
        IdFilterPipe pipe3 = new IdFilterPipe(Query.Compare.NOT_EQUAL, "3");
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
