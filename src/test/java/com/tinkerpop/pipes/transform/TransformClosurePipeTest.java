package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipeClosure;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TransformClosurePipeTest extends TestCase {

    public void testTransformClosure() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        List<Integer> results = Arrays.asList(5, 7, 9, 3, 4, 1);
        Pipe<String, Integer> pipe = new TransformClosurePipe<String, Integer>(new NumCharPipeClosure());
        pipe.setStarts(list);
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), results.get(counter));
            counter++;
        }
        assertEquals(counter, list.size());
    }

    private class NumCharPipeClosure extends AbstractPipeClosure<Integer, Pipe> {
        public Integer compute(Object... parameters) {
            return ((String) parameters[0]).length();
        }
    }
}
