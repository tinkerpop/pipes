package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RangeFilterPipeTest extends TestCase {

    public void testRangeFilterNormal() {
        List<String> names = Arrays.asList("abe", "bob", "carl", "derick", "evan", "fran");
        Pipe<String, String> pipe = new RangeFilterPipe<String>(0, 4);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            counter++;
            //System.out.println(name);
            assertTrue(name.equals("abe") || name.equals("bob") || name.equals("carl") || name.equals("derick"));
            assertFalse(name.equals("evan") || name.equals("fran"));
        }
        assertEquals(counter, 4);
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
        Pipe<String, String> pipe = new RangeFilterPipe<String>(-1, 4);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            counter++;
            //System.out.println(name);
            assertTrue(name.equals("abe") || name.equals("bob") || name.equals("carl") || name.equals("derick"));
            assertFalse(name.equals("evan") || name.equals("fran"));
        }
        assertEquals(counter, 4);
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
            Pipe<String, String> pipe = new RangeFilterPipe<String>(2, 1);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            //System.out.println(e);
            assertTrue(true);
        }

    }
}
