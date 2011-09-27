package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RangeFilterPipeTest extends TestCase {

    public void testPipeBasic() {
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

    public void testRangeFilterAbsurd() {
        try {
            new RangeFilterPipe<String>(2, 0);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}
