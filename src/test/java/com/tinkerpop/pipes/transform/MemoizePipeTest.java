package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.util.PipesPipeline;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class MemoizePipeTest extends TestCase {

    public void testPipeEquality() {
        assertEquals(
                new PipesPipeline().start(Arrays.asList("a", "b", "c"))._().discard("x", FilterPipe.Filter.NOT_EQUAL).memoize(1)._(),
                new PipesPipeline().start(Arrays.asList("a", "b", "c"))._().discard("x", FilterPipe.Filter.NOT_EQUAL)._());
    }

    /*public void testBasicMemoization() {


        Map<String, List<String>> map = new HashMap<String, List<String>>();
        Pipe pipe = new Pipeline(new IdentityPipe(), new AppendCharAndLengthPipe(), new MemoizePipe())
        pipe1.iterate();

        assertEquals(map.get(graph.getVertex(1)).size(), 3);
        assertTrue(map.get(graph.getVertex(1)).contains(graph.getVertex(2)));
        assertTrue(map.get(graph.getVertex(1)).contains(graph.getVertex(3)));
        assertTrue(map.get(graph.getVertex(1)).contains(graph.getVertex(4)));

        assertEquals(map.get(graph.getVertex(2)).size(), 0);

        assertEquals(map.get(graph.getVertex(3)).size(), 0);

        assertEquals(map.get(graph.getVertex(4)).size(), 2);
        assertTrue(map.get(graph.getVertex(4)).contains(graph.getVertex(5)));
        assertTrue(map.get(graph.getVertex(4)).contains(graph.getVertex(3)));

        assertEquals(map.get(graph.getVertex(5)).size(), 0);

        assertEquals(map.get(graph.getVertex(6)).size(), 1);
        assertTrue(map.get(graph.getVertex(6)).contains(graph.getVertex(3)));
    }*/

    private class AppendCharAndLengthPipe extends AbstractPipe<String, Object> {
        Integer n;

        public Object processNextStart() {
            if (null != n) {
                Integer temp = n;
                n = null;
                return temp;
            } else {
                String temp = this.starts.next() + "a";
                n = temp.length();
                return temp;
            }
        }
    }


}
