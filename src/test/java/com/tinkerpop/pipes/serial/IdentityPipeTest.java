package com.tinkerpop.pipes.serial;

import com.tinkerpop.pipes.BaseTest;

import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdentityPipeTest extends BaseTest {

    public void testIdentityPipeNormal() {
        List<String> uuids = generateUUIDs(100);
        Pipe<String, String> pipe = new IdentityPipe<String>();
        pipe.setStarts(uuids);
        int counter = 0;
        assertTrue(pipe.hasNext());
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), uuids.get(counter));
            counter++;
        }
        assertEquals(counter, 100);
        assertFalse(pipe.hasNext());
    }

    public void testIdentityPipeZero() {
        List<String> uuids = generateUUIDs(0);
        Pipe<String, String> pipe = new IdentityPipe<String>();
        pipe.setStarts(uuids);
        int counter = 0;
        assertFalse(pipe.hasNext());
        assertEquals(counter, 0);
        assertFalse(pipe.hasNext());
    }
}
