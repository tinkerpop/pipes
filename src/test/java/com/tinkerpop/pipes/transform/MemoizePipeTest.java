package com.tinkerpop.pipes.transform;

import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class MemoizePipeTest extends TestCase {

    /* public void testBasicPipeEquality() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        assertEquals(new FluentPipeline().start(graph.getVertices()).out().out().memoize(1).property("name"), new FluentPipeline().start(graph.getVertices()).out().out().property("name"));
    }

    public void testBasicMemoization() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();

        Map<Vertex, List<Vertex>> map = new HashMap<Vertex, List<Vertex>>();
        FluentPipeline pipe1 = new FluentPipeline().start(graph.getVertices()).out().memoize(1, map).property("name");
        pipe1.iterate();

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
    }*/

    public void testTrue() {
        assertTrue(true);
    }
}
