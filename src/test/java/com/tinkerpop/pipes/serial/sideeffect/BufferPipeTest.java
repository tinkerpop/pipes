package com.tinkerpop.pipes.serial.sideeffect;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BufferPipeTest extends TestCase {

    public void testBufferPipeNormal() {
        List<String> names = Arrays.asList("marko", "peter", "josh", "marko");
        SideEffectPipe<String, String, List<String>> pipe = new BufferPipe<String>(2);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            counter++;
            assertTrue(name.equals("marko") || name.equals("peter") || name.equals("josh"));
            if (counter > 1) {
                assertEquals(pipe.getSideEffect().size(), 2);
            }
        }
        assertEquals(counter, 4);
        assertEquals(pipe.getSideEffect().get(0), "josh");
        assertEquals(pipe.getSideEffect().get(1), "marko");

        pipe = new BufferPipe<String>(4);
        pipe.setStarts(names);
        counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            counter++;
            assertTrue(name.equals("marko") || name.equals("peter") || name.equals("josh"));
        }
        assertEquals(counter, 4);
        assertEquals(pipe.getSideEffect().get(0), "marko");
        assertEquals(pipe.getSideEffect().get(1), "peter");
        assertEquals(pipe.getSideEffect().get(2), "josh");
        assertEquals(pipe.getSideEffect().get(3), "marko");
    }
}
