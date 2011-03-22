package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class StartPipeTest extends TestCase {

    public void testStartPipe() {
        Pipe<String, String> pipe = new StartPipe<String>("hello");
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            assertEquals(pipe.next(), "hello");
        }
        assertEquals(counter, 1);

        pipe = new StartPipe<String>(Arrays.asList("hello", "hell", "he"));
        counter = 0;
        while (pipe.hasNext()) {
            counter++;
            assertTrue(pipe.next().startsWith("he"));
        }
        assertEquals(counter, 3);
    }
}
