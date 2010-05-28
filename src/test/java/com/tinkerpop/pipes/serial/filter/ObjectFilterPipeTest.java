package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipeTest extends TestCase {

    public void testObjectFilter() {
        List<String> starts = Arrays.asList("marko", "josh", "peter");
        Pipe<String, String> ofp = new ObjectFilterPipe<String>("marko", ComparisonFilterPipe.Filter.NOT_EQUALS);
        ofp.setStarts(starts.iterator());
        assertTrue(ofp.hasNext());
        int counter = 0;
        while (ofp.hasNext()) {
            String next = ofp.next();
            assertTrue(next.equals("josh") || next.equals("peter"));
            counter++;
        }
        assertEquals(counter, 2);

        ofp = new ObjectFilterPipe<String>("marko", ComparisonFilterPipe.Filter.EQUALS);
        ofp.setStarts(starts.iterator());
        assertTrue(ofp.hasNext());
        counter = 0;
        while (ofp.hasNext()) {
            String next = ofp.next();
            assertTrue(next.equals("marko"));
            counter++;
        }
        assertEquals(counter, 1);
        try {
            ofp.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }

    public void testNumericComparisons() {
        List<Integer> starts = Arrays.asList(32, 1, 7);
        Pipe<Integer, Integer> ofp = new ObjectFilterPipe<Integer>(7, ComparisonFilterPipe.Filter.LESS_THAN);
        ofp.setStarts(starts.iterator());
        assertTrue(ofp.hasNext());
        int counter = 0;
        while (ofp.hasNext()) {
            Integer next = ofp.next();
            assertTrue(next.equals(1));
            counter++;
        }
        assertEquals(counter, 1);
        //////
        ofp = new ObjectFilterPipe<Integer>(7, ComparisonFilterPipe.Filter.LESS_THAN_EQUAL);
        ofp.setStarts(starts.iterator());
        assertTrue(ofp.hasNext());
        counter = 0;
        while (ofp.hasNext()) {
            Integer next = ofp.next();
            assertTrue(next.equals(1) || next.equals(7));
            counter++;
        }
        assertEquals(counter, 2);
        //////
        ofp = new ObjectFilterPipe<Integer>(7, ComparisonFilterPipe.Filter.GREATER_THAN);
        ofp.setStarts(starts.iterator());
        assertTrue(ofp.hasNext());
        counter = 0;
        while (ofp.hasNext()) {
            Integer next = ofp.next();
            assertTrue(next.equals(32));
            counter++;
        }
        assertEquals(counter, 1);
        //////
        ofp = new ObjectFilterPipe<Integer>(7, ComparisonFilterPipe.Filter.GREATER_THAN_EQUAL);
        ofp.setStarts(starts.iterator());
        assertTrue(ofp.hasNext());
        counter = 0;
        while (ofp.hasNext()) {
            Integer next = ofp.next();
            assertTrue(next.equals(7) || next.equals(32));
            counter++;
        }
        assertEquals(counter, 2);
    }

}
