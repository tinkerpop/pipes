package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IfThenElsePipeTest extends TestCase {

    public void testIfThenElse() {
        List<String> names = Arrays.asList("marko", "povel", "pete", "josh", "stephen");
        List<Integer> results = Arrays.asList(0, 5, 4, 0, 0);
        Pipe<String, Integer> pipe = new IfThenElsePipe<String, Integer>(new IfPipeClosure(), new ThenPipeClosure(), new ElsePipeClosure());
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), results.get(counter));
            counter++;
        }
        assertEquals(counter, names.size());

    }


    private class IfPipeClosure implements PipeClosure<Boolean, Pipe> {
        public Boolean compute(Object... parameters) {
            return ((String) parameters[0]).startsWith("p");
        }

        public void setPipe(Pipe hostPipe) {

        }
    }

    private class ThenPipeClosure implements PipeClosure<Object, Pipe> {
        public Object compute(Object... parameters) {
            return ((String) parameters[0]).length();
        }

        public void setPipe(Pipe hostPipe) {

        }
    }

    private class ElsePipeClosure implements PipeClosure<Object, Pipe> {
        public Object compute(Object... parameters) {
            return 0;
        }

        public void setPipe(Pipe hostPipe) {

        }
    }
}
