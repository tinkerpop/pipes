package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipeClosure;
import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SideEffectClosurePipeTest extends TestCase {

    public void testBasicSideEffect() {
        AtomicInteger count = new AtomicInteger(0);
        int counter = 0;
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        Pipe<String, String> pipe = new SideEffectClosurePipe(new CountPipeClosure(count));
        pipe.setStarts(list);
        while (pipe.hasNext()) {
            assertTrue(list.contains(pipe.next()));
            counter++;
        }
        assertEquals(count.get(), list.size());
        assertEquals(count.get(), counter);

    }

    private class CountPipeClosure extends AbstractPipeClosure {

        private AtomicInteger integer;

        public CountPipeClosure(AtomicInteger integer) {
            this.integer = integer;
        }

        public Object compute(Object... parameters) {
            this.integer.getAndAdd(1);
            return null;
        }
    }
}
