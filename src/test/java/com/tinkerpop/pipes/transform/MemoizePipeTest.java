package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.util.FluentPipeline;
import junit.framework.TestCase;

import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class MemoizePipeTest extends TestCase {

    public void testBasicPipeEquality() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        assertEquals(new FluentPipeline().start(graph.getVertices()).out().out().memoize(1).property("name"), new FluentPipeline().start(graph.getVertices()).out().out().property("name"));
    }

    public void testBasicMemoization() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();

        FluentPipeline pipe1 = new FluentPipeline().start(graph.getVertices()).out().memoize(1).property("name");
        pipe1.iterate();
        Map<Vertex, List<Vertex>> map = ((MemoizePipe<Vertex, Vertex>) pipe1.getPipes().get(1)).getMemoization();
        assertEquals(map.get(graph.getVertex(1)).size(), 3);
        assertTrue(map.get(graph.getVertex(1)).contains(graph.getVertex(2)));
        assertTrue(map.get(graph.getVertex(1)).contains(graph.getVertex(3)));
        assertTrue(map.get(graph.getVertex(1)).contains(graph.getVertex(4)));

        assertEquals(map.get(graph.getVertex(2)).size(), 0);

        assertEquals(map.get(graph.getVertex(3)).size(), 0);

        assertEquals(map.get(graph.getVertex(4)).size(), 2);
        assertTrue(map.get(graph.getVertex(4)).contains(graph.getVertex(5)));
        assertTrue(map.get(graph.getVertex(4)).contains(graph.getVertex(3)));

        assertEquals(map.get(graph.getVertex(5)).size(), 0);

        assertEquals(map.get(graph.getVertex(6)).size(), 1);
        assertTrue(map.get(graph.getVertex(6)).contains(graph.getVertex(3)));
    }
}
