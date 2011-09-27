package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe<String, List<String>> pipe = new GatherPipe<String>();
        pipe.setStarts(Arrays.asList("marko", "josh", "peter"));
        List list = pipe.next();
        assertEquals(list.size(), 3);
        assertEquals(list.get(0), "marko");
        assertEquals(list.get(1), "josh");
        assertEquals(list.get(2), "peter");
        assertFalse(pipe.hasNext());
    }

}
