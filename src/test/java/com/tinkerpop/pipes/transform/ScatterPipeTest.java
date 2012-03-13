package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ScatterPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe pipe = new ScatterPipe();
        pipe.enablePath(true);
        pipe.setStarts(Arrays.asList(Arrays.asList(1, 2, 3)));
        int counter = 0;
        while (pipe.hasNext()) {
            Object object = pipe.next();
            assertTrue(object.equals(1) || object.equals(2) || object.equals(3));
            List path = pipe.getCurrentPath();
            assertEquals(path.get(path.size() - 1), object);
            assertEquals(((List) path.get(0)).get(0), 1);
            assertEquals(((List) path.get(0)).get(1), 2);
            assertEquals(((List) path.get(0)).get(2), 3);
            counter++;

        }
        assertEquals(counter, 3);

        pipe.setStarts(Arrays.asList(Arrays.asList(1, 2, 3)));
        pipe.reset();
        assertEquals(1, pipe.next());
        pipe.setStarts(Arrays.asList(Arrays.asList(1, 2, 3)));
        pipe.reset();
        assertEquals(1, pipe.next());
        assertEquals(2, pipe.next());
        assertEquals(3, pipe.next());
    }

    public void testScatterPaths() {
        ScatterPipe pipe = new ScatterPipe();
        pipe.enablePath(true);
        pipe.setStarts(Arrays.asList(Arrays.asList("a", "b", "c"), Arrays.asList(1, 2, 3)));
        int counter = 0;
        while (pipe.hasNext()) {
            Object object = pipe.next();
            List path = pipe.getCurrentPath();
            assertEquals(path.get(path.size() - 1), object);
            if (counter > 2) {
                assertEquals(((List) path.get(0)).get(0), 1);
                assertEquals(((List) path.get(0)).get(1), 2);
                assertEquals(((List) path.get(0)).get(2), 3);
            } else {
                assertEquals(((List) path.get(0)).get(0), "a");
                assertEquals(((List) path.get(0)).get(1), "b");
                assertEquals(((List) path.get(0)).get(2), "c");
            }
            counter++;
        }
        assertEquals(counter, 6);
    }

    public void testMapEntrySetScatter() {
        Pipe<Map, Map.Entry> scatter = new ScatterPipe<Map, Map.Entry>();
        Map map = new HashMap();
        map.put("marko", 1);
        map.put("peter", 2);
        int counter = 0;
        scatter.setStarts(Arrays.asList(map, map));
        while (scatter.hasNext()) {
            counter++;
            Map.Entry entry = scatter.next();
            assertTrue(entry.getKey().equals("marko") || entry.getKey().equals("peter"));
            assertTrue(entry.getValue().equals(1) || entry.getValue().equals(2));

        }
        assertEquals(counter, 4);
    }
}

