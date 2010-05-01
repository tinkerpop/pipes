package com.tinkerpop.pipes.serial.cap;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.serial.Pipe;

import java.util.Collection;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CountPipeCapTest extends BaseTest {

    public void testCountPipeCapNormal() {
        Collection<String> uuids = generateUUIDs(100);
        Pipe<String, Long> pipe = new CountPipeCap<String>();
        pipe.setStarts(uuids);
        assertTrue(pipe.hasNext());
        assertEquals(new Long(100), pipe.next());
        //assertFalse(pipe.hasNext());
    }

    public void testCountPipeCapZero() {
        Collection<String> uuids = generateUUIDs(0);
        Pipe<String, Long> pipe = new CountPipeCap<String>();
        pipe.setStarts(uuids);
        assertTrue(pipe.hasNext());
        assertEquals(new Long(0), pipe.next());
        //assertFalse(pipe.hasNext());
    }
}
