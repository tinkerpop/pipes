package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SideEffectFunctionPipeTest extends TestCase {

    public void testBasicSideEffect() {
        AtomicInteger count = new AtomicInteger(0);
        int counter = 0;
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        Pipe<String, String> pipe = new SideEffectFunctionPipe(new CountPipeFunction(count));
        pipe.setStarts(list);
        while (pipe.hasNext()) {
            assertTrue(list.contains(pipe.next()));
            counter++;
        }
        assertEquals(count.get(), list.size());
        assertEquals(count.get(), counter);

    }

    private class CountPipeFunction implements PipeFunction<Object, Object> {

        private AtomicInteger integer;

        public CountPipeFunction(AtomicInteger integer) {
            this.integer = integer;
        }

        public Object compute(Object argument) {
            this.integer.getAndAdd(1);
            return null;
        }
    }
}
