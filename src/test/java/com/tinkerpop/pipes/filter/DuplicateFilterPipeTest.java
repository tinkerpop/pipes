package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DuplicateFilterPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> starts = Arrays.asList("marko", "josh", "peter", "marko", "marko");
        Pipe<String, String> dfp = new DuplicateFilterPipe<String>();
        dfp.setStarts(starts.iterator());
        assertTrue(dfp.hasNext());
        int counter = 0;
        int counter2 = 0;
        while (dfp.hasNext()) {
            String next = dfp.next();
            assertTrue(next.equals("josh") || next.equals("peter") || next.equals("marko"));
            if (next.equals("marko"))
                counter2++;
            counter++;
        }
        assertEquals(counter, 3);
        assertEquals(counter2, 1);
    }

    public void testDedupFunction() {
        List<String> starts = Arrays.asList("marko", "josh", "peter", "marko", "marko");
        Pipe<String, String> pipe = new DuplicateFilterPipe<String>(new PipeFunction<String, Object>() {
            @Override
            public Object compute(String argument) {
                return argument.length();
            }
        });
        pipe.setStarts(starts.iterator());
        int counter = 0;
        int counter2 = 0;
        while (pipe.hasNext()) {
            String next = pipe.next();
            assertTrue(next.equals("josh") || next.equals("marko"));
            if (next.equals("marko") || next.equals("peter"))
                counter2++;
            counter++;
        }
        assertEquals(counter, 2);
        assertEquals(counter2, 1);


    }

    public void testReset() {
        List<String> starts = Arrays.asList("marko", "marko", "peter", "marko", "josh");
        Pipe<String, String> pipe = new DuplicateFilterPipe<String>();
        pipe.setStarts(starts.iterator());
        assertTrue(pipe.hasNext());
        assertEquals(pipe.next(), "marko");
        assertEquals(pipe.next(), "peter");
        pipe.reset();
        assertEquals(pipe.next(), "marko");
        assertEquals(pipe.next(), "josh");
        assertFalse(pipe.hasNext());
    }

}
