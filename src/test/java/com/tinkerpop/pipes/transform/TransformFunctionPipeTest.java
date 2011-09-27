package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TransformFunctionPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        List<Integer> results = Arrays.asList(5, 7, 9, 3, 4, 1);
        Pipe<String, Integer> pipe = new TransformFunctionPipe<String, Integer>(new NumCharPipeFunction());
        pipe.setStarts(list);
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), results.get(counter));
            counter++;
        }
        assertEquals(counter, list.size());
    }

    public void testPipeNoElements() {
        Pipe<String, Integer> pipe = new TransformFunctionPipe<String, Integer>(new NumCharPipeFunction());
        pipe.setStarts(new ArrayList<String>());
        int counter = 0;
        while (pipe.hasNext()) {
            pipe.next();
            counter++;
        }
        assertEquals(counter, 0);
    }

    private class NumCharPipeFunction implements PipeFunction<String, Integer> {
        public Integer compute(String argument) {
            return argument.length();
        }
    }
}
