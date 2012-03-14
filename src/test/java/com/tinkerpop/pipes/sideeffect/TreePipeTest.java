package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TreePipeTest extends TestCase {

    public void testBasicPipe() {
        Pipe<String, Integer> a = new StringLengthPipe();
        SideEffectPipe<Integer, Map> b = new TreePipe<Integer>();
        Pipeline<String, Integer> pipeline = new Pipeline<String, Integer>(a, b);
        pipeline.setStarts(Arrays.asList("marko", "josh"));
        int counter = 0;
        while (pipeline.hasNext()) {
            Integer i = pipeline.next();
            assertTrue(i == 5 || i == 4);
            counter++;
        }
        assertEquals(counter, 2);
        Map map = b.getSideEffect();
        assertEquals(map.size(), 2);
        assertTrue(map.get("marko") instanceof Map);
        assertTrue(map.get("josh") instanceof Map);
        assertEquals(((Map) map.get("marko")).size(), 1);
        assertEquals(((Map) map.get("josh")).size(), 1);
        assertTrue(((Map) map.get("marko")).containsKey(5));
        assertTrue(((Map) map.get("josh")).containsKey(4));

    }

    private class StringLengthPipe extends AbstractPipe<String, Integer> {
        public Integer processNextStart() {
            return this.starts.next().length();
        }
    }
}
