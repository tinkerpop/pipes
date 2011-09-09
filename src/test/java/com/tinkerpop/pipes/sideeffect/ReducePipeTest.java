package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.Pair;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ReducePipeTest extends BaseTest {

    public void testBasicPipe() {
        ReducePipe<String, Integer> pipe = new ReducePipe<String, Integer>(0, new PipeFunction<Pair<String, Integer>, Integer>() {
            public Integer compute(Pair<String, Integer> argument) {
                return Integer.parseInt(argument.a) + argument.b;
            }
        });
        pipe.setStarts(Arrays.asList("1", "2", "3", "4", "5"));
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(new Integer(++counter), new Integer(Integer.parseInt(pipe.next())));
        }
        assertEquals(counter, 5);
        assertEquals(pipe.getSideEffect(), new Integer(1 + 2 + 3 + 4 + 5));
    }
}
