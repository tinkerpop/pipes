package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.IdentityPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class HasNextPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        HasNextPipe<String> pipe1 = new HasNextPipe<String>(new IdentityPipe<String>());
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            assertTrue(pipe1.next());
        }
        assertEquals(counter, 4);
    }

    public void testPipeInline() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        IdentityPipe<String> source_pipe = new IdentityPipe<String>();
        source_pipe.setStarts(names);
        HasNextPipe<String> pipe1 = new HasNextPipe<String>();
        pipe1.setStarts(source_pipe);
        assertTrue(pipe1.next());
        assertFalse(pipe1.hasNext());

        pipe1.reset();
        source_pipe.setStarts(new ArrayList());
        assertFalse(pipe1.next());
        assertFalse(pipe1.hasNext());

        pipe1.reset();
        source_pipe.setStarts(names);
        assertTrue(pipe1.next());
        assertFalse(pipe1.hasNext());
    }

}
