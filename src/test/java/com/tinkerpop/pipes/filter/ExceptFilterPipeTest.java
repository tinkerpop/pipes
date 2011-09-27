package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExceptFilterPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Pipe<String, String> pipe1 = new ExceptFilterPipe<String>(new HashSet<String>(Arrays.asList("marko", "pavel")));
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh"));
        }
        assertEquals(counter, 2);
    }

    public void testPipeNoExceptions() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Pipe<String, String> pipe1 = new ExceptFilterPipe<String>(new HashSet<String>(Arrays.asList("bill")));
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko") || name.equals("pavel"));
        }
        assertEquals(counter, 6);
    }

    public void testPipeNoExceptions2() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Pipe<String, String> pipe1 = new ExceptFilterPipe<String>(new HashSet<String>());
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko") || name.equals("pavel"));
        }
        assertEquals(counter, 6);
    }
}