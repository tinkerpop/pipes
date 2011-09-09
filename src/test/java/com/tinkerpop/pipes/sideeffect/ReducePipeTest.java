package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.PipeFunction;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ReducePipeTest extends BaseTest {

    public void testBasicPipe() {
        ReducePipe<Integer> pipe = new ReducePipe<Integer>(0, new PipeFunction<ReducePipe<Integer>.Tuple<Integer>, Integer>() {
            public Integer compute(ReducePipe<Integer>.Tuple<Integer> argument) {
                return argument.a + argument.b;
            }
        });
        pipe.setStarts(Arrays.asList(1, 2, 3, 4, 5));
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(new Integer(++counter), pipe.next());
        }
        assertEquals(counter, 5);
        assertEquals(pipe.getSideEffect(), new Integer(1 + 2 + 3 + 4 + 5));
    }
}
