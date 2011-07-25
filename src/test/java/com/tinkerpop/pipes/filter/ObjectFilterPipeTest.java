package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipeTest extends TestCase {

    public void testNullObjects() {
        List<String> starts = Arrays.asList("marko", "pavel", null);
        Pipe<String, String> pipe = new ObjectFilterPipe<String>(null, ComparisonFilterPipe.Filter.EQUAL);
        pipe.setStarts(starts);
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String string = pipe.next();
            assertNull(string);
        }
        assertEquals(counter, 1);

        pipe = new ObjectFilterPipe<String>(null, ComparisonFilterPipe.Filter.NOT_EQUAL);
        pipe.setStarts(starts);
        counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String string = pipe.next();
            assertTrue(string.equals("marko") || string.equals("pavel"));
        }
        assertEquals(counter, 2);

        pipe = new ObjectFilterPipe<String>(null, ComparisonFilterPipe.Filter.LESS_THAN_EQUAL);
        pipe.setStarts(starts);
        counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        assertEquals(counter, 0);
    }

    public void testObjectFilter() {
        List<String> starts = Arrays.asList("marko", "josh", "peter");
        Pipe<String, String> pipe = new ObjectFilterPipe<String>("marko", ComparisonFilterPipe.Filter.NOT_EQUAL);
        pipe.setStarts(starts.iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            String next = pipe.next();
            assertTrue(next.equals("josh") || next.equals("peter"));
            counter++;
        }
        assertEquals(counter, 2);

        pipe = new ObjectFilterPipe<String>("marko", ComparisonFilterPipe.Filter.EQUAL);
        pipe.setStarts(starts.iterator());
        assertTrue(pipe.hasNext());
        counter = 0;
        while (pipe.hasNext()) {
            String next = pipe.next();
            assertTrue(next.equals("marko"));
            counter++;
        }
        assertEquals(counter, 1);
        try {
            pipe.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }

    public void testNumericComparisons() {
        List<Integer> starts = Arrays.asList(32, 1, 7);
        Pipe<Integer, Integer> pipe = new ObjectFilterPipe<Integer>(6, ComparisonFilterPipe.Filter.LESS_THAN_EQUAL);
        pipe.setStarts(starts.iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            Integer next = pipe.next();
            assertTrue(next.equals(1));
            counter++;
        }
        assertEquals(counter, 1);
        //////
        pipe = new ObjectFilterPipe<Integer>(8, ComparisonFilterPipe.Filter.LESS_THAN);
        pipe.setStarts(starts.iterator());
        assertTrue(pipe.hasNext());
        counter = 0;
        while (pipe.hasNext()) {
            Integer next = pipe.next();
            assertTrue(next.equals(1) || next.equals(7));
            counter++;
        }
        assertEquals(counter, 2);
        //////
        pipe = new ObjectFilterPipe<Integer>(8, ComparisonFilterPipe.Filter.GREATER_THAN_EQUAL);
        pipe.setStarts(starts.iterator());
        assertTrue(pipe.hasNext());
        counter = 0;
        while (pipe.hasNext()) {
            Integer next = pipe.next();
            assertTrue(next.equals(32));
            counter++;
        }
        assertEquals(counter, 1);
        //////
        pipe = new ObjectFilterPipe<Integer>(6, ComparisonFilterPipe.Filter.GREATER_THAN);
        pipe.setStarts(starts.iterator());
        assertTrue(pipe.hasNext());
        counter = 0;
        while (pipe.hasNext()) {
            Integer next = pipe.next();
            assertTrue(next.equals(7) || next.equals(32));
            counter++;
        }
        assertEquals(counter, 2);
    }

}
