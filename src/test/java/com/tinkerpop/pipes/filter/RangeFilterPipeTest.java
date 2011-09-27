package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RangeFilterPipeTest extends TestCase {

    public void testRangeFilterNormal() {
        List<String> names = Arrays.asList("abe", "bob", "carl", "derick", "evan", "fran");
        Pipe<String, String> pipe = new RangeFilterPipe<String>(0, 2);
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

    public void testRangeFilterReset() {
        List<String> names = Arrays.asList("abe", "bob", "carl", "derick", "evan", "fran");
        Pipe<String, String> pipe = new RangeFilterPipe<String>(1, 1);
        pipe.setStarts(names);
        assertEquals("bob", pipe.next());
        assertFalse(pipe.hasNext());
        pipe.reset();
        assertEquals("evan", pipe.next());
        assertFalse(pipe.hasNext());
        pipe.reset();
        pipe.setStarts(names);
        assertEquals("bob", pipe.next());
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
        Pipe<String, String> pipe = new RangeFilterPipe<String>(-1, 2);
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

    public void testRangeFilterEdgeCases() {
        List<String> names = Arrays.asList("abe", "bob", "carl", "derick", "evan", "fran");
        Pipe<String, String> pipe = new RangeFilterPipe<String>(0, 0);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        assertEquals("abe", pipe.next());
        assertFalse(pipe.hasNext());
        pipe = new RangeFilterPipe<String>(-1, 0);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        assertEquals("abe", pipe.next());
        assertFalse(pipe.hasNext());
        pipe = new RangeFilterPipe<String>(0, 1);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        assertEquals("abe", pipe.next());
        assertTrue(pipe.hasNext());
        assertEquals("bob", pipe.next());
        assertFalse(pipe.hasNext());
        pipe = new RangeFilterPipe<String>(-1, 1);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        assertEquals("abe", pipe.next());
        assertTrue(pipe.hasNext());
        assertEquals("bob", pipe.next());
        assertFalse(pipe.hasNext());
        pipe = new RangeFilterPipe<String>(1, 1);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        assertEquals("bob", pipe.next());
        assertFalse(pipe.hasNext());
        pipe = new RangeFilterPipe<String>(4, 5);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        assertEquals("evan", pipe.next());
        assertTrue(pipe.hasNext());
        assertEquals("fran", pipe.next());
        assertFalse(pipe.hasNext());
        pipe = new RangeFilterPipe<String>(5, 5);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        assertEquals("fran", pipe.next());
        assertFalse(pipe.hasNext());
        pipe = new RangeFilterPipe<String>(5, 6);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        assertEquals("fran", pipe.next());
        assertFalse(pipe.hasNext());

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

    /* public void testRangeFilterGraphOneObject() {
       Graph graph = TinkerGraphFactory.createTinkerGraph();
       Vertex marko = graph.getVertex("1");
       Pipe<Vertex, Edge> pipe1 = new OutEdgesPipe();
       Pipe<Edge, Edge> pipe2 = new RangeFilterPipe<Edge>(1, 1);
       Pipe<Edge, Vertex> pipe3 = new InVertexPipe();
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
       Graph graph = TinkerGraphFactory.createTinkerGraph();
       Vertex marko = graph.getVertex("1");
       Pipe<Vertex, Edge> pipe1 = new OutEdgesPipe();
       Pipe<Edge, Edge> pipe2 = new RangeFilterPipe<Edge>(0, 1);
       Pipe<Edge, Vertex> pipe3 = new InVertexPipe();
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
   } */

    public void testRangeFilterAbsurd() {
        try {
            Pipe<String, String> pipe = new RangeFilterPipe<String>(2, 0);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}
