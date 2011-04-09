package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SideEffectCapPipeTest extends TestCase {

    public void testSideEffectCapPipeNormalCount() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        Pipe<String, Long> pipe = new SideEffectCapPipe<String, Long>(new CountPipe<String>());
        pipe.setStarts(list.iterator());
        assertTrue(pipe.hasNext());
        assertTrue(pipe.hasNext());
        assertEquals(pipe.next(), new Long(6));
        assertFalse(pipe.hasNext());
        try {
            pipe.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
        pipe.reset();
        pipe.setStarts(list);
        assertTrue(pipe.hasNext());
        assertEquals(6, (long)pipe.next());
    }

    public void testSideEffectCapPipeZeroCount() {
        List<String> list = Arrays.asList();
        Pipe<String, Long> pipe = new SideEffectCapPipe<String, Long>(new CountPipe<String>());
        pipe.setStarts(list.iterator());
        assertTrue(pipe.hasNext());
        assertTrue(pipe.hasNext());
        assertEquals(pipe.next(), new Long(0));
        assertFalse(pipe.hasNext());
        try {
            pipe.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }
}
