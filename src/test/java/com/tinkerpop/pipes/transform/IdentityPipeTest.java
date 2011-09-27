package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.Pipe;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdentityPipeTest extends BaseTest {

    public void testPipeBasic() {
        List<String> list = Arrays.asList("marko", "peter", "josh", "pavel", "stephen", "alex", "darrick", "pierre");
        Pipe<String, String> pipe = new IdentityPipe<String>();
        pipe.setStarts(list);
        int counter = 0;
        assertTrue(pipe.hasNext());
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), list.get(counter));
            counter++;
        }
        assertEquals(counter, list.size());
        assertFalse(pipe.hasNext());
    }

    public void testPipeWithNoElements() {
        List<String> list = Arrays.asList();
        Pipe<String, String> pipe = new IdentityPipe<String>();
        pipe.setStarts(list);
        int counter = 0;
        assertFalse(pipe.hasNext());
        assertEquals(counter, 0);
        assertFalse(pipe.hasNext());
    }
}
