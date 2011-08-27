package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipeClosure;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterClosurePipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class HasNextPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        HasNextPipe<String> pipe1 = new HasNextPipe<String>(new IdentityPipe<String>());
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            assertTrue(pipe1.next());
        }
        assertEquals(counter, 4);
    }

    public void testPipeWithFilter() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        HasNextPipe<String> pipe1 = new HasNextPipe<String>(new FilterClosurePipe(new AbstractPipeClosure<Boolean, Pipe>() {
            public Boolean compute(Object... objects) {
                return ((String) objects[0]).startsWith("p");
            }
        }));
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            if (counter == 1 || counter == 2) {
                assertTrue(pipe1.next());
            } else {
                assertFalse(pipe1.next());
            }
            counter++;
        }
        assertEquals(counter, 4);
    }

}
