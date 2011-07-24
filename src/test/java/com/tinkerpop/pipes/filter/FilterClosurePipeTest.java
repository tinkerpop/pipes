package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipeClosure;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FilterClosurePipeTest extends TestCase {

    public void testBasicClosureFilter() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "povel", "marko");
        FilterPipe<String> pipe = new FilterClosurePipe<String>(new StartsWithPipeClosure());
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            assertTrue(name.equals("povel") || name.equals("peter"));
            counter++;
        }
        assertEquals(counter, 3);

    }

    private class StartsWithPipeClosure extends AbstractPipeClosure {
        public Boolean compute(Object... objects) {
            return (((String) objects[0]).startsWith("p"));
        }
    }
}
