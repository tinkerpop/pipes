package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.SingleIterator;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GraphElementPipeTest extends TestCase {

    public void testVertexIterator() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe<Graph, Vertex> pipe = new GraphElementPipe<Vertex>(GraphElementPipe.ElementType.VERTEX);
        pipe.setStarts(new SingleIterator<Graph>(graph));
        int counter = 0;
        Set<Vertex> vertices = new HashSet<Vertex>();
        while (pipe.hasNext()) {
            counter++;
            Vertex vertex = pipe.next();
            vertices.add(vertex);
            //System.out.println(vertex);
        }
        assertEquals(counter, 6);
        assertEquals(vertices.size(), 6);
    }

    public void testEdgeIterator() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe<Graph, Edge> pipe = new GraphElementPipe<Edge>(GraphElementPipe.ElementType.EDGE);
        pipe.setStarts(new SingleIterator<Graph>(graph));
        int counter = 0;
        Set<Edge> edges = new HashSet<Edge>();
        while (pipe.hasNext()) {
            counter++;
            Edge edge = pipe.next();
            edges.add(edge);
            //System.out.println(edge);
        }
        assertEquals(counter, 6);
        assertEquals(edges.size(), 6);
    }

    public void testEdgeIteratorThreeGraphs() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe<Graph, Edge> pipe = new GraphElementPipe<Edge>(GraphElementPipe.ElementType.EDGE);
        pipe.setStarts(Arrays.asList(graph, graph, graph));
        int counter = 0;
        Set<Edge> edges = new HashSet<Edge>();
        while (pipe.hasNext()) {
            counter++;
            Edge edge = pipe.next();
            edges.add(edge);
            //System.out.println(edge);
        }
        assertEquals(counter, 18);
        assertEquals(edges.size(), 6);
    }
}
