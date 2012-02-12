package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.Pair;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupCountFunctionPipeTest extends TestCase {

    public void testPipeBasic() {
        List<Integer> names = Arrays.asList(1, 2, 2, 3, 3, 3);
        SideEffectPipe<Integer, Map<Integer, Number>> pipe = new GroupCountFunctionPipe<Integer, Integer>(new PipeFunction<Integer, Integer>() {
            public Integer compute(Integer integer) {
                return integer;
            }
        }, new PipeFunction<Pair<Integer, Number>, Number>() {
            public Number compute(Pair<Integer, Number> argument) {
                return argument.getA() + argument.getB().intValue();
            }
        }
        );
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        int counter = 0;
        for (Integer name : pipe) {
            assertTrue(name.equals(1) || name.equals(2) || name.equals(3));
            counter++;
        }
        assertEquals(counter, 6);
        assertEquals(pipe.getSideEffect().get(1), 1);
        assertEquals(pipe.getSideEffect().get(2), 4);
        assertEquals(pipe.getSideEffect().get(3), 9);
        assertNull(pipe.getSideEffect().get(4));
        assertFalse(pipe.hasNext());

        pipe.reset();
        assertEquals(0, pipe.getSideEffect().size());
    }

}
