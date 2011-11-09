package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.util.LoopBundle;
import com.tinkerpop.pipes.sideeffect.AggregatePipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe<String, String> pipe = new LoopPipe<String>(new StepsLessThanThreeFunction(), new RemoveCharPipeFunction());
        pipe.setStarts(Arrays.asList("aaaaaa", "bbbb", "c"));
        int counter = 0;
        while (pipe.hasNext()) {
            String string = pipe.next();
            if (counter == 0) {
                assertEquals(string, "aaaa");
            } else if (counter == 1) {
                assertEquals(string, "bb");
            } else if (counter == 2) {
                assertEquals(string, "");
            }

            counter++;
        }
        assertEquals(counter, 3);
    }

    public void testPipeBasicPaths() {
        Pipe<String, String> pipe = new LoopPipe<String>(new LengthGreaterThanOneFunction(), new RemoveCharPipeFunction());
        pipe.setStarts(Arrays.asList("aaaaaa", "bbbb", "c"));
        int counter = 0;
        while (pipe.hasNext()) {
            String string = pipe.next();
            List path = pipe.getPath();
            if (counter == 0) {
                assertEquals(string, "a");
                assertEquals(path.get(0), "aaaaaa");
                assertEquals(path.get(1), "aaaaa");
                assertEquals(path.get(2), "aaaa");
                assertEquals(path.get(3), "aaa");
                assertEquals(path.get(4), "aa");
                assertEquals(path.get(5), "a");
                assertEquals(path.size(), 6);
            } else if (counter == 1) {
                assertEquals(string, "b");
                assertEquals(path.get(0), "bbbb");
                assertEquals(path.get(1), "bbb");
                assertEquals(path.get(2), "bb");
                assertEquals(path.get(3), "b");
                assertEquals(path.size(), 4);
            } else if (counter == 2) {
                assertEquals(string, "c");
                assertEquals(path.get(0), "c");
                assertEquals(path.size(), 1);
            }
            counter++;
        }
        assertEquals(counter, 3);
    }

    public void testPipeNoElements() {
        Pipe<String, String> pipe = new LoopPipe(new StepsLessThanThreeFunction(), new RemoveCharPipeFunction());
        pipe.setStarts(new ArrayList<String>());
        int counter = 0;
        while (pipe.hasNext()) {
            pipe.next();
            counter++;
        }
        assertEquals(counter, 0);
    }

    public void testUnrolledLoopEquality() {
        Pipe pipe1 = new Pipeline(new IdentityPipe(), new LoopPipe(new StepsLessThanThreeFunction(), new AddOnePipeFunction()));
        pipe1.setStarts(Arrays.asList(1, 2, 3));
        Pipe pipe2 = new Pipeline(new IdentityPipe(), new AddOnePipe(), new AddOnePipe());
        pipe2.setStarts(Arrays.asList(1, 2, 3));

        assertTrue(PipeHelper.areEqual(pipe1, pipe2));
    }

    public void testAggregatePipeEquality() {
        List<Integer> x1 = new ArrayList<Integer>();
        Pipe pipe1 = new Pipeline(new LoopPipe(new StepsLessThanThreeFunction(), new AddOnePipeFunction2(x1)));
        pipe1.setStarts(Arrays.asList(1, 2, 3, 4, 5));

        List<Integer> x2 = new ArrayList<Integer>();
        Pipe pipe2 = new Pipeline(new AddOnePipe(), new AggregatePipe(x2), new AddOnePipe(), new AggregatePipe(x2));
        pipe2.setStarts(Arrays.asList(1, 2, 3, 4, 5));

        assertTrue(PipeHelper.areEqual(pipe1, pipe2));
        assertEquals(x1.size(), x2.size());
        for (int i = 0; i < x1.size(); i++) {
            assertEquals(x1.get(i), x2.get(i));
        }
    }


    private class StepsLessThanThreeFunction implements PipeFunction<LoopBundle<String>, Boolean> {
        public Boolean compute(LoopBundle<String> argument) {
            return (argument.getStep() < 3);
        }
    }

    private class LengthGreaterThanOneFunction implements PipeFunction<LoopBundle<String>, Boolean> {
        public Boolean compute(LoopBundle<String> argument) {
            return argument.getObject().length() > 1;
        }
    }

    private class RemoveCharPipeFunction implements PipeFunction<Object, Pipe<String, String>> {
        public Pipe<String, String> compute(Object object) {
            return new RemoveCharPipe();
        }
    }

    private class AddOnePipeFunction implements PipeFunction<Object, Pipe<Integer, Integer>> {
        public Pipe<Integer, Integer> compute(Object object) {
            return new AddOnePipe();
        }
    }

    private class AddOnePipeFunction2 implements PipeFunction<Object, Pipe<Integer, Integer>> {
        List x1;

        public AddOnePipeFunction2(List x1) {
            this.x1 = x1;
        }

        public Pipe<Integer, Integer> compute(Object x1) {
            return new Pipeline(new AddOnePipe(), new AggregatePipe(this.x1));
        }
    }

    private class RemoveCharPipe extends AbstractPipe<String, String> {
        public String processNextStart() {
            String s = this.starts.next();
            if (s.length() == 0) {
                return s;
            } else {
                return s.substring(0, s.length() - 1);
            }
        }
    }


    private class AddOnePipe extends AbstractPipe<Integer, Integer> {
        public Integer processNextStart() {
            return this.starts.next() + 1;
        }
    }


}
