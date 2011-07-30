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
        List results = Arrays.asList(0, "hehe", 5, 4, 0, "hehe", 0, "hehe");
        Pipe<String, Integer> pipe = new IfThenElsePipe<String, Integer>(new IfPipeClosure(), new ThenPipeClosure(), new ElsePipeClosure());
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), results.get(counter));
            counter++;
        }
        assertEquals(counter, results.size());

    }

    public void testIfThenElseWithPaths() {
        List<String> names = Arrays.asList("marko", "povel", "pete", "josh", "stephen");
        List results = Arrays.asList(0, "hehe", 5, 4, 0, "hehe", 0, "hehe");
        List path1 = Arrays.asList("marko", "marko", "povel", "pete", "josh", "josh", "stephen", "stephen");
        Pipe<String, Integer> pipe = new IfThenElsePipe<String, Integer>(new IfPipeClosure(), new ThenPipeClosure(), new ElsePipeClosure());
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), results.get(counter));
            List path = pipe.getPath();
            assertEquals(path.get(0), path1.get(counter));
            assertEquals(path.get(1), results.get(counter));
            assertEquals(path.size(), 2);
            counter++;
        }
        assertEquals(counter, results.size());

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
            return Arrays.asList(0, "hehe");
        }

        public void setPipe(Pipe hostPipe) {

        }
    }
}
