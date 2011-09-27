package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.PipeFunction;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FilterFunctionPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "povel", "marko");
        FilterPipe<String> pipe = new FilterFunctionPipe<String>(new StartsWithPipeFunction());
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            String name = pipe.next();
            assertTrue(name.equals("povel") || name.equals("peter"));
            counter++;
        }
        assertEquals(counter, 3);

    }

    private class StartsWithPipeFunction implements PipeFunction<String, Boolean> {
        public Boolean compute(String argument) {
            return argument.startsWith("p");
        }
    }
}
