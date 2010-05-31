package com.tinkerpop.pipes.util;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DynamicStartsPipeTest extends TestCase {

    public void testDynamicStartsPipeNormalNoAdds() {
        List<String> names = Arrays.asList("marko", "josh", "peter");
        DynamicStartsPipe<String> pipe = new DynamicStartsPipe<String>();
        pipe.setStarts(names);
        int counter = 0;
        for (String name : pipe) {
            counter++;
        }
        assertEquals(counter, 3);
    }

    public void testDynamicStartsPipeIteratorNormal() {
        List<String> names = Arrays.asList("marko", "josh", "peter");
        DynamicStartsPipe<String> pipe = new DynamicStartsPipe<String>();
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            System.out.println(pipe.next());
            counter++;
            if (counter == 1) {
                pipe.addStart("povel");
            }
            if (counter == 2) {
                pipe.addStart("tobias");
            }
        }
        assertEquals(counter, 5);
    }

    public void testDynamicStartsPipeIterableNormal() {
        List<String> names = Arrays.asList("marko", "josh", "peter");
        DynamicStartsPipe<String> pipe = new DynamicStartsPipe<String>();
        pipe.setStarts(names);
        int counter = 0;
        for (String name : pipe) {
            System.out.println(name);
            counter++;
            if (counter == 1) {
                pipe.addStart("povel");
            }
            if (counter == 2) {
                pipe.addStart("tobias");
            }
        }
        assertEquals(counter, 5);
    }
}
