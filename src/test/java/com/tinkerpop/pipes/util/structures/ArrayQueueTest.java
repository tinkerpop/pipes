package com.tinkerpop.pipes.util.structures;

import junit.framework.TestCase;

import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ArrayQueueTest extends TestCase {

    public void testQueueOperations() {
        Queue<Integer> queue = new ArrayQueue<Integer>();
        assertTrue(queue.offer(1));
        assertTrue(queue.offer(2));
        assertTrue(queue.offer(3));
        assertTrue(queue.offer(4));
        assertTrue(queue.offer(5));
        assertEquals(queue.peek(), new Integer(1));
        assertEquals(queue.remove(), new Integer(1));
        assertEquals(queue.poll(), new Integer(2));
        assertEquals(queue.peek(), new Integer(3));
        assertEquals(queue.peek(), new Integer(3));
        assertEquals(queue.element(), new Integer(3));
        assertEquals(queue.remove(), new Integer(3));
        assertEquals(queue.poll(), new Integer(4));
        assertEquals(queue.poll(), new Integer(5));
        assertNull(queue.peek());
        assertNull(queue.poll());
        try {
            queue.remove();
            assertFalse(true);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }

        try {
            queue.element();
            assertFalse(true);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }

    }

}
