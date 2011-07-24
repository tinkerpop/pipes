package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.Pipe;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ScatterPipeTest extends BaseTest {

    public void testScatterPipe() {
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
}

