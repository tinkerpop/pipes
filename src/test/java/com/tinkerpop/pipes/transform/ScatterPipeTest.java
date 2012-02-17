package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ScatterPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe scatter = new ScatterPipe();
        scatter.setStarts(Arrays.asList(Arrays.asList(1, 2, 3)));
        int counter = 0;
        while (scatter.hasNext()) {
            Object object = scatter.next();
            assertTrue(object.equals(1) || object.equals(2) || object.equals(3));
            counter++;
        }
        assertEquals(counter, 3);

        scatter.setStarts(Arrays.asList(Arrays.asList(1, 2, 3)));
        scatter.reset();
        assertEquals(1, scatter.next());
        scatter.setStarts(Arrays.asList(Arrays.asList(1, 2, 3)));
        scatter.reset();
        assertEquals(1, scatter.next());
        assertEquals(2, scatter.next());
        assertEquals(3, scatter.next());
    }

    public void testMapEntrySetScatter() {
        Pipe<Map,Map.Entry> scatter = new ScatterPipe<Map,Map.Entry>();
        Map map = new HashMap();
        map.put("marko", 1);
        map.put("peter", 2);
        int counter = 0;
        scatter.setStarts(Arrays.asList(map,map));
        while(scatter.hasNext()) {
            counter++;
            Map.Entry entry = scatter.next();
            assertTrue(entry.getKey().equals("marko") || entry.getKey().equals("peter"));
            assertTrue(entry.getValue().equals(1) || entry.getValue().equals(2));
            
        }
        assertEquals(counter,4);
    }
}

