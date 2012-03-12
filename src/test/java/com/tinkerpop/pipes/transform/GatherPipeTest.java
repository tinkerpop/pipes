package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.PipesPipeline;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe<String, List<String>> pipe = new GatherPipe<String>();
        pipe.setStarts(Arrays.asList("marko", "josh", "peter"));
        List list = pipe.next();
        assertEquals(list.size(), 3);
        assertEquals(list.get(0), "marko");
        assertEquals(list.get(1), "josh");
        assertEquals(list.get(2), "peter");
        assertFalse(pipe.hasNext());
    }

    public void testGatherFunction() {
        Pipe<String, Integer> pipeA = new NumCharPipe();
        Pipe<Integer, List<Integer>> pipeB = new GatherPipe<Integer>(new GreaterThanFunction());
        Pipeline<String, List<Integer>> pipeline = new Pipeline<String, List<Integer>>(pipeA, pipeB);
        pipeline.setStarts(Arrays.asList("marko", "josh", "peter", "stephen"));

        assertTrue(pipeline.hasNext());
        List<Integer> list = pipeline.next();
        for (Integer i : list) {
            assertTrue(i > 4);
        }
        //System.out.println(pipeline.getPath());
        assertFalse(pipeline.hasNext());
    }

    public void testGatherLooping() {
        PipesPipeline pipeline = new PipesPipeline(Arrays.asList("marko", "josh", "peter")).add(new RemoveCharPipe()).gather().scatter().loop(3, LoopPipe.createLoopsFunction(3));
        while (pipeline.hasNext()) {
            String s = (String) pipeline.next();
            if (s.startsWith("m"))
                assertEquals(s, "mar");
            else if (s.startsWith("j"))
                assertEquals(s, "jo");
            else if (s.startsWith("p"))
                assertEquals(s, "pet");
            else
                throw new RuntimeException("An unexpected String came through the pipeline.");

            System.out.println(pipeline.getPath());
        }
    }

    private class NumCharPipe extends AbstractPipe<String, Integer> {
        public Integer processNextStart() {
            return this.starts.next().length();
        }
    }

    private class RemoveCharPipe extends AbstractPipe<String, String> {
        public String processNextStart() {
            while (true) {
                String s = this.starts.next();
                if (s.length() > 1) {
                    return s.substring(0, s.length() - 1);
                }
            }
        }
    }

    private class GreaterThanFunction implements PipeFunction<List<Integer>, List<Integer>> {
        public List<Integer> compute(List<Integer> list) {
            List<Integer> temp = new ArrayList<Integer>();
            for (Integer i : list) {
                if (i > 4) {
                    temp.add(i);
                }
            }
            return temp;
        }
    }

}
