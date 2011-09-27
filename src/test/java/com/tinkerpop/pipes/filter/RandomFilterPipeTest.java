package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RandomFilterPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe<String, String> pipe = new RandomFilterPipe<String>(1.0d);
        pipe.setStarts(this.generateUUIDs(100).iterator());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        assertEquals(counter, 100);

        pipe = new RandomFilterPipe<String>(0.0d);
        pipe.setStarts(this.generateUUIDs(100).iterator());
        counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        assertEquals(counter, 0);
    }

    public void testRandomFilterPipe5050() {
        Pipe<String, String> pipe = new RandomFilterPipe<String>(0.5d);
        pipe.setStarts(this.generateUUIDs(1000).iterator());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        assertTrue(counter > 400 && counter < 600);
    }

    private List<String> generateUUIDs(int amount) {
        List<String> list = new ArrayList<String>(amount);
        for (int i = 0; i < amount; i++) {
            list.add(UUID.randomUUID().toString());
        }
        return list;
    }
}
