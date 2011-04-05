package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.IdentityPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Darrick Wiebe
 */
public class HasCountPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        HasCountPipe<String> pipe1 = new HasCountPipe<String>(1, 2);
        pipe1.setStarts(names);
        assertFalse(pipe1.next());
        assertFalse(pipe1.hasNext());

        HasCountPipe<String> pipe2 = new HasCountPipe<String>(1, 4);
        pipe2.setStarts(names);
        assertTrue(pipe2.next());
        assertFalse(pipe2.hasNext());

        HasCountPipe<String> pipe3 = new HasCountPipe<String>(4, 10);
        pipe3.setStarts(names);
        assertTrue(pipe3.next());
        assertFalse(pipe3.hasNext());

        pipe3.reset();
        pipe3.setStarts(names);
        assertTrue(pipe3.next());
        assertFalse(pipe3.hasNext());

        HasCountPipe<String> pipe4 = new HasCountPipe<String>(5, 10);
        pipe4.setStarts(names);
        assertFalse(pipe4.next());
        assertFalse(pipe4.hasNext());
    }
}
