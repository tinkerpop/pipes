package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DuplicateFilterPipeTest extends TestCase {

    public void testDuplicateFilter() {
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
}
