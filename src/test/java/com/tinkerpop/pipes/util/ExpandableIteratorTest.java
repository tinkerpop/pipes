package com.tinkerpop.pipes.util;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExpandableIteratorTest extends TestCase {

    public void testExpansion() {
        ExpandableIterator<Integer> itty = new ExpandableIterator<Integer>(Arrays.asList(1, 2, 3).iterator());
        assertEquals(itty.next(), new Integer(1));
        itty.add(4);
        assertEquals(itty.next(), new Integer(4));
        itty.add(5);
        itty.add(6);
        assertEquals(itty.next(), new Integer(5));
        assertEquals(itty.next(), new Integer(6));
        assertEquals(itty.next(), new Integer(2));
        assertEquals(itty.next(), new Integer(3));
        assertFalse(itty.hasNext());
        try {
            itty.next();
            assertFalse(true);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }
}
