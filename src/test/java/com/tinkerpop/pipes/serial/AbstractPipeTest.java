package com.tinkerpop.pipes.serial;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AbstractPipeTest extends TestCase {

    public void testIterable() {
        Collection<String> names = Arrays.asList("marko", "josh", "peter");
        Pipe<String, String> pipe = new IdentityPipe<String>();
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next();
            assertTrue(name.equals("marko") || name.equals("josh") || name.equals("peter"));
        }
        assertEquals(counter, 3);
        pipe.setStarts(names);
        counter = 0;
        for (String name : pipe) {
            assertTrue(name.equals("marko") || name.equals("josh") || name.equals("peter"));
            counter++;
        }
        assertEquals(counter, 3);
    }

    public void testClear() {
        Collection<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        Pipe<String, String> pipe = new IdentityPipe<String>();
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next();
            assertTrue(name.equals("marko") || name.equals("povel") || name.equals("peter") || name.equals("josh"));
            // todo: pipe.hasNext();
            pipe.clear(); // identity pipe has no state

        }
        assertEquals(counter, 4);
    }
}
