package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
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
        Pipe<String, Integer> pipe = new IfThenElsePipe<String, Integer>(new IfPipeFunction(), new ThenPipeFunction(), new ElsePipeFunction());
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
        Pipe<String, Integer> pipe = new IfThenElsePipe<String, Integer>(new IfPipeFunction(), new ThenPipeFunction(), new ElsePipeFunction());
        pipe.enablePath(true);
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), results.get(counter));
            List path = pipe.getCurrentPath();
            assertEquals(path.get(0), path1.get(counter));
            assertEquals(path.get(1), results.get(counter));
            assertEquals(path.size(), 2);
            counter++;
        }
        assertEquals(counter, results.size());

    }


    private class IfPipeFunction implements PipeFunction<String, Boolean> {
        public Boolean compute(String argument) {
            return argument.startsWith("p");
        }
    }

    private class ThenPipeFunction implements PipeFunction<String, Integer> {
        public Integer compute(String argument) {
            return argument.length();
        }
    }

    private class ElsePipeFunction implements PipeFunction<String, List> {
        public List compute(String argument) {
            return Arrays.asList(0, "hehe");
        }
    }
}
