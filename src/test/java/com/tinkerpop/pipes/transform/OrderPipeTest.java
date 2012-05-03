package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.PipesPipeline;
import com.tinkerpop.pipes.util.structures.Pair;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrderPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe pipe = new OrderPipe<Integer>();
        pipe.setStarts(Arrays.asList(4, 1, 3, 2));
        List<Integer> list = PipeHelper.makeList(pipe);
        assertEquals(list.get(0), new Integer(1));
        assertEquals(list.get(1), new Integer(2));
        assertEquals(list.get(2), new Integer(3));
        assertEquals(list.get(3), new Integer(4));
        assertEquals(list.size(), 4);
    }

    public void testPipeBasic2() {
        Pipe pipe = new OrderPipe<Integer>(new PipeFunction<Pair<Integer, Integer>, Integer>() {
            public Integer compute(Pair<Integer, Integer> argument) {
                return argument.getB().compareTo(argument.getA());
            }
        });
        pipe.setStarts(Arrays.asList(4, 1, 3, 2));
        List<Integer> list = PipeHelper.makeList(pipe);
        assertEquals(list.get(0), new Integer(4));
        assertEquals(list.get(1), new Integer(3));
        assertEquals(list.get(2), new Integer(2));
        assertEquals(list.get(3), new Integer(1));
        assertEquals(list.size(), 4);
    }

    public void testPipeWithLooping() {
        Pipeline pipeline = new PipesPipeline(Arrays.asList(4, 1, 3, 2))._().order().step(new IncrPipe()).loop(2, LoopPipe.createLoopsFunction(2));
        List<Integer> list = pipeline.toList();
        assertEquals(list.get(0), new Integer(2));
        assertEquals(list.get(1), new Integer(3));
        assertEquals(list.get(2), new Integer(4));
        assertEquals(list.get(3), new Integer(5));
        assertEquals(list.size(), 4);
    }

    public void testPipeWithFluentPipelineComparator() {
        Pipeline pipeline = new PipesPipeline(Arrays.asList(4, 1, 3, 2)).order(new PipeFunction<Pair<Integer, Integer>, Integer>() {
            public Integer compute(Pair<Integer, Integer> argument) {
                return argument.getB().compareTo(argument.getA());
            }
        })._();
        List<Integer> list = pipeline.toList();
        assertEquals(list.get(0), new Integer(4));
        assertEquals(list.get(1), new Integer(3));
        assertEquals(list.get(2), new Integer(2));
        assertEquals(list.get(3), new Integer(1));
        assertEquals(list.size(), 4);
    }

    private class IncrPipe extends AbstractPipe<Integer, Integer> {
        public Integer processNextStart() {
            return this.starts.next() + 1;
        }
    }
}
