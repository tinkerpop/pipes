package com.tinkerpop.pipes.sideeffect;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CountPipeTest extends TestCase {

    public void testCountPipeNormal() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        CountPipe<String> pipe1 = new CountPipe<String>();
        pipe1.setStarts(list.iterator());
        assertTrue(pipe1.hasNext());
        assertTrue(pipe1.hasNext());
        Long counter = 0l;
        while (pipe1.hasNext()) {
            String s = pipe1.next();
            assertTrue(s.equals("marko") || s.equals("antonio") || s.equals("rodriguez") || s.equals("was") || s.equals("here") || s.equals("."));
            counter++;
            assertEquals(pipe1.getSideEffect(), counter);
        }
        assertEquals(pipe1.getSideEffect(), new Long(6));
        try {
            pipe1.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }

        pipe1.reset();
        assertEquals(0l, (long) pipe1.getSideEffect());
        pipe1.setStarts(list.iterator());
        pipe1.next();
        assertEquals(1l, (long) pipe1.getSideEffect());
        pipe1.hasNext();
        assertEquals(2l, (long) pipe1.getSideEffect());
    }


    public void testCountPipeZero() {
        List<String> list = Arrays.asList();
        CountPipe<String> pipe1 = new CountPipe<String>();
        pipe1.setStarts(list.iterator());
        assertFalse(pipe1.hasNext());
        assertFalse(pipe1.hasNext());
        while (pipe1.hasNext()) {
            pipe1.next();
        }
        assertEquals(pipe1.getSideEffect(), new Long(0));
        try {
            pipe1.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }
}
