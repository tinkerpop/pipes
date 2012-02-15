package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.PipeFunction;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupByPipeTest extends TestCase {

    public void testPipeBasic() {
        GroupByPipe<String, String, String> pipe = new GroupByPipe<String, String, String>(new PipeFunction<String, String>() {
            public String compute(String argument) {
                return argument;
            }
        }, new PipeFunction<String, String>() {
            public String compute(String argument) {
                return argument;
            }
        }
        );
        pipe.setStarts(Arrays.asList("marko", "a", "rodriguez"));
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String string = pipe.next();
            assertTrue(string.equals("marko") || string.equals("a") || string.equals("rodriguez"));
        }
        assertEquals(counter, 3);
        Map<String, List<String>> map = pipe.getSideEffect();
        assertEquals(map.size(), 3);
        assertEquals(map.get("marko").get(0), "marko");
        assertEquals(map.get("a").get(0), "a");
        assertEquals(map.get("rodriguez").get(0), "rodriguez");
        assertEquals(map.get("marko").size(), 1);
        assertEquals(map.get("a").size(), 1);
        assertEquals(map.get("rodriguez").size(), 1);
    }

    public void testPipeFunctions() {
        GroupByPipe<String, String, Integer> pipe = new GroupByPipe<String, String, Integer>(new PipeFunction<String, String>() {
            public String compute(String argument) {
                return argument.substring(0, 1);
            }
        }, new PipeFunction<String, Integer>() {
            public Integer compute(String argument) {
                return argument.length();
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
        Map<String, List<Integer>> map = pipe.getSideEffect();
        assertEquals(map.size(), 3);
        assertEquals(map.get("m").size(), 1);
        assertEquals(map.get("j").size(), 2);
        assertEquals(map.get("p").size(), 2);
        assertTrue(map.get("m").contains(5));
        assertTrue(map.get("j").contains(4));
        assertTrue(map.get("j").contains(5));
        assertTrue(map.get("p").contains(5));

    }


}
