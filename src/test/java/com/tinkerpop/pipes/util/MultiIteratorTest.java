package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.BaseTest;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class MultiIteratorTest extends TestCase {

    public void testMultiIteratorSimple() {
        List<Integer> a = Arrays.asList(1, 2);
        List<Integer> b = Arrays.asList(3, 4, 5);
        List<Integer> c = Arrays.asList(6, 7, 8);
        Iterator<Integer> itty = new MultiIterator<Integer>(a.iterator(), b.iterator(), c.iterator());
        int counter = 0;
        while (itty.hasNext()) {
            counter++;
            assertEquals(itty.next(), (Integer) counter);
        }
        assertEquals(counter, 8);

    }

    public void testMultiIteratorPureNext() {
        List<Integer> a = Arrays.asList(1, 2);
        List<Integer> b = Arrays.asList(3, 4, 5);
        List<Integer> c = Arrays.asList(6, 7, 8);
        Iterator<Integer> itty = new MultiIterator<Integer>(a.iterator(), b.iterator(), c.iterator());
        int counter = 0;
        try {
            while (true) {
                counter++;
                assertEquals(itty.next(), (Integer) counter);
            }
        } catch (NoSuchElementException e) {
            assertEquals(counter, 9);
        }

    }

    public void testMultiIteratorNoParameters() {
        Iterator<Integer> itty = new MultiIterator<Integer>();
        int counter = 0;
        while (itty.hasNext()) {
            counter++;
        }
        assertEquals(counter, 0);
    }
}
