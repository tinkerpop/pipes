package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherFunctionPipeTest extends TestCase {

    public void testGatherFunction() {
        Pipe<String, Integer> pipeA = new NumCharPipe();
        Pipe<Integer, List<Integer>> pipeB = new GatherFunctionPipe<Integer, List<Integer>>(new GreaterThanFunction());
        Pipeline<String, List<Integer>> pipeline = new Pipeline<String, List<Integer>>(pipeA, pipeB);
        pipeline.setStarts(Arrays.asList("marko", "josh", "peter", "stephen"));
        pipeline.enablePath(true);

        assertTrue(pipeline.hasNext());
        List<Integer> list = pipeline.next();
        for (Integer i : list) {
            assertTrue(i > 4);
        }
        //System.out.println(pipeline.getCurrentPath());
        assertFalse(pipeline.hasNext());
    }

    public void testGatherFunctionOneReturn() {
        Pipe<String, Integer> pipeA = new NumCharPipe();
        Pipe<Integer, Integer> pipeB = new GatherFunctionPipe<Integer, Integer>(new ReturnFourFunction());
        Pipeline<String, List<Integer>> pipeline = new Pipeline<String, List<Integer>>(pipeA, pipeB);
        pipeline.setStarts(Arrays.asList("marko", "josh", "peter", "stephen"));

        assertTrue(pipeline.hasNext());
        assertEquals(pipeline.next(), new Integer(4));
        assertFalse(pipeline.hasNext());
    }

    public void testGatherFunctionOneReturnPath() {
        Pipe<String, Integer> pipeA = new NumCharPipe();
        Pipe<Integer, Integer> pipeB = new GatherFunctionPipe<Integer, Integer>(new ReturnFourFunction());
        Pipeline<String, List<Integer>> pipeline = new Pipeline<String, List<Integer>>(pipeA, pipeB);
        pipeline.setStarts(Arrays.asList("marko", "josh", "peter", "stephen"));
        pipeline.enablePath(true);

        assertTrue(pipeline.hasNext());
        assertEquals(pipeline.next(), new Integer(4));
        List path = pipeline.getCurrentPath();
        //System.out.println(path);
        assertFalse(pipeline.hasNext());
    }

    private class NumCharPipe extends AbstractPipe<String, Integer> {
        public Integer processNextStart() {
            return this.starts.next().length();
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

    private class ReturnFourFunction implements PipeFunction<List<Integer>, Integer> {
        public Integer compute(List<Integer> list) {
            return 4;
        }
    }
}
