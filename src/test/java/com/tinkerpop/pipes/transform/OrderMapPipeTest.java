package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.PipeHelper;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrderMapPipeTest extends TestCase {

    public void testPipeBasicIncrement() {
        Pipe pipe = new OrderMapPipe<String>(TransformPipe.Order.INCR);
        Map map = new HashMap();
        map.put("c", 3);
        map.put("a", 10);
        map.put("b", 5);

        pipe.setStarts(Arrays.asList(map));
        List<String> list = PipeHelper.makeList(pipe);
        assertEquals(list.get(0), "c");
        assertEquals(list.get(1), "b");
        assertEquals(list.get(2), "a");
        assertEquals(list.size(), 3);
    }

    public void testPipeBasicDescrement() {
        Pipe pipe = new OrderMapPipe<String>(TransformPipe.Order.DECR);
        Map map = new HashMap();
        map.put("c", 3);
        map.put("a", 10);
        map.put("b", 5);

        pipe.setStarts(Arrays.asList(map));
        List<String> list = PipeHelper.makeList(pipe);
        assertEquals(list.get(2), "c");
        assertEquals(list.get(1), "b");
        assertEquals(list.get(0), "a");
        assertEquals(list.size(), 3);
    }
}