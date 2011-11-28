package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.PipeFunction;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class StoragePipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        StorePipe<String> pipe = new StorePipe<String>(new ArrayList<String>());
        pipe.setStarts(list.iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), list.get(counter));
            counter++;
        }
        assertEquals(counter, 6);
        assertEquals(pipe.getSideEffect().size(), counter);
        assertEquals(list.size(), counter);
        for (int i = 0; i < counter; i++) {
            assertEquals(list.get(i), pipe.getSideEffect().toArray()[i]);
        }

        pipe.reset();
        assertEquals(0, pipe.getSideEffect().size());
        pipe.setStarts(list.iterator());
        pipe.next();
        assertEquals(1, pipe.getSideEffect().size());
        pipe.next();
        assertEquals(2, pipe.getSideEffect().size());
        pipe.next();
        assertEquals(3, pipe.getSideEffect().size());
        pipe.next();
        assertEquals(4, pipe.getSideEffect().size());
        pipe.next();
        assertEquals(5, pipe.getSideEffect().size());
        pipe.next();
        assertEquals(6, pipe.getSideEffect().size());
    }

    public void testPipeWithFunction() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        StorePipe<String> pipe = new StorePipe<String>(new ArrayList<Integer>(), new LengthPipeFunction());
        pipe.setStarts(list.iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), list.get(counter));
            counter++;
        }
        assertEquals(counter, 6);
        assertEquals(pipe.getSideEffect().size(), counter);
        assertEquals(list.size(), counter);
        for (int i = 0; i < counter; i++) {
            assertEquals(list.get(i).length(), pipe.getSideEffect().toArray()[i]);
        }

        pipe.reset();
        assertEquals(0, pipe.getSideEffect().size());
        pipe.setStarts(list.iterator());
    }

    private class LengthPipeFunction implements PipeFunction<String, Integer> {
        public Integer compute(String argument) {
            return argument.length();
        }
    }

}
