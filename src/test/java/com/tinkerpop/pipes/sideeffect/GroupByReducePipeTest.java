package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.PipeFunction;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupByReducePipeTest extends TestCase {

    public void testPipeBasic() {
        GroupByReducePipe<String, String, Integer, Integer> pipe = new GroupByReducePipe<String, String, Integer, Integer>(new PipeFunction<String, String>() {
            public String compute(String argument) {
                return argument.substring(0, 1);
            }
        }, new PipeFunction<String, Integer>() {
            public Integer compute(String argument) {
                return argument.length();
            }
        }, new PipeFunction<Iterator<Integer>, Integer>() {
            public Integer compute(Iterator<Integer> args) {
                int sum = 0;
                while (args.hasNext()) {
                    sum = sum + args.next();
                }
                return sum;
            }
        }
        );
        List<String> starts = Arrays.asList("marko", "josh", "peter", "pavel", "james");
        pipe.setStarts(starts);
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String string = pipe.next();
            assertTrue(starts.contains(string));
        }
        assertEquals(counter, starts.size());
        Map<String, Integer> map = pipe.getSideEffect();
        assertEquals(map.size(), 3);
        assertEquals(map.get("m").intValue(), 5);
        assertEquals(map.get("j").intValue(), 9);
        assertEquals(map.get("p").intValue(), 10);

    }
}