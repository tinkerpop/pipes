package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.SingleIterator;
import com.tinkerpop.pipes.pgm.EdgeVertexPipe;
import com.tinkerpop.pipes.pgm.PropertyPipe;
import com.tinkerpop.pipes.pgm.VertexEdgePipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RangeFilterPipeTest extends TestCase {

    public void testRangeFilterNormal() {
        List<String> names = Arrays.asList("abe", "bob", "carl", "derick", "evan", "fran");
        Pipe<String, String> pipe = new RangeFilterPipe<String>(0, 3);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            //System.out.println(name + counter);
            counter++;
            assertTrue(name.equals("abe") || name.equals("bob") || name.equals("carl"));
            assertFalse(name.equals("derick") || name.equals("evan") || name.equals("fran"));
        }
        assertEquals(counter, 3);
    }

    public void testRangeFilterHighInfinity() {
        List<String> names = Arrays.asList("abe", "bob", "carl", "derick", "evan", "fran");
        Pipe<String, String> pipe = new RangeFilterPipe<String>(2, -1);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            counter++;
            //System.out.println(name);
            assertTrue(name.equals("carl") || name.equals("derick") || name.equals("evan") || name.equals("fran"));
            assertFalse(name.equals("abe") || name.equals("bob"));
        }
        assertEquals(counter, 4);
    }

    public void testRangeFilterLowInfinity() {
        List<String> names = Arrays.asList("abe", "bob", "carl", "derick", "evan", "fran");
        Pipe<String, String> pipe = new RangeFilterPipe<String>(-1, 3);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            counter++;
            //System.out.println(name);
            assertTrue(name.equals("abe") || name.equals("bob") || name.equals("carl"));
            assertFalse(name.equals("derick") || name.equals("evan") || name.equals("fran"));
        }
        assertEquals(counter, 3);
    }

    public void testRangeFilterLowHighInfinity() {
        List<String> names = Arrays.asList("abe", "bob", "carl", "derick", "evan", "fran");
        Pipe<String, String> pipe = new RangeFilterPipe<String>(-1, -1);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            counter++;
            //System.out.println(name);
            assertTrue(name.equals("abe") || name.equals("bob") || name.equals("carl") || name.equals("derick") || name.equals("evan") || name.equals("fran"));
        }
        assertEquals(counter, 6);
    }

    public void testRangeFilterGraphOneObject() {
        // ./outE[2]/inV/@name

        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Edge> pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Edge, Edge> pipe2 = new RangeFilterPipe<Edge>(1, 2);
        Pipe<Edge, Vertex> pipe3 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, String> pipe4 = new PropertyPipe<Vertex, String>("name");
        Pipeline<Vertex, String> pipeline = new Pipeline<Vertex, String>(pipe1, pipe2, pipe3, pipe4);
        pipeline.setStarts(new SingleIterator<Vertex>(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            assertEquals(pipeline.next(), "lop");
        }
        assertEquals(counter, 1);
    }

    public void testRangeFilterGraphTwoObjects() {
        // ./outE[position() > 0 and position() < 3]/inV/@name
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Edge> pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Edge, Edge> pipe2 = new RangeFilterPipe<Edge>(0, 2);
        Pipe<Edge, Vertex> pipe3 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, String> pipe4 = new PropertyPipe<Vertex, String>("name");
        Pipeline<Vertex, String> pipeline = new Pipeline<Vertex, String>(pipe1, pipe2, pipe3, pipe4);
        pipeline.setStarts(new SingleIterator<Vertex>(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            String name = pipeline.next();
            assertTrue(name.equals("lop") || name.equals("vadas"));
        }
        assertEquals(counter, 2);
    }

    public void testRangeFilterAbsurd() {
        try {
            Pipe<String, String> pipe = new RangeFilterPipe<String>(2, 1);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            //System.out.println(e);
            assertTrue(true);
        }
    }

    public void testRangeFilterHalt() {
        List names = mock(List.class);
        Iterator iterator = mock(Iterator.class);
        when(names.iterator()).thenReturn(iterator);
        when(iterator.next()).thenReturn("duck", "duck", "duck", "goose", "goose").thenThrow(new NoSuchElementException());
        Pipe<String, String> pipe = new RangeFilterPipe<String>(0, 3);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            //System.out.println(name + counter);
            counter++;
            assertEquals("duck", name);
        }
        assertEquals(counter, 3);
        verify(iterator, times(4)).next();
    }
}
