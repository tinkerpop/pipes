package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.util.PipeHelper;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ToStringPipeTest extends TestCase {

    public void testNullPointerHandling() {
        ToStringPipe pipe = new ToStringPipe();
        pipe.setStarts(Arrays.asList("marko", null, 1));
        List list = PipeHelper.makeList(pipe);
        assertEquals(list.get(0), "marko");
        assertEquals(list.get(1), "null");
        assertEquals(list.get(2), "1");
        assertEquals(list.size(), 3);
    }
}