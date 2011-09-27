package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.sideeffect.AggregatePipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe<String, String> pipe = new LoopPipe(new RemoveCharPipe(), new LoopPipeFunction());
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

    public void testPipeNoElements() {
        Pipe<String, String> pipe = new LoopPipe(new RemoveCharPipe(), new LoopPipeFunction());
        pipe.setStarts(new ArrayList<String>());
        int counter = 0;
        while (pipe.hasNext()) {
            pipe.next();
            counter++;
        }
        assertEquals(counter, 0);
    }

    public void testUnrolledLoopEquality() {
        Pipe pipe1 = new Pipeline(new IdentityPipe(), new LoopPipe(new AddOnePipe(), new LoopPipeFunction()));
        pipe1.setStarts(Arrays.asList(1, 2, 3));
        Pipe pipe2 = new Pipeline(new IdentityPipe(), new AddOnePipe(), new AddOnePipe());
        pipe2.setStarts(Arrays.asList(1, 2, 3));

        assertTrue(PipeHelper.areEqual(pipe1, pipe2));
    }

    public void testAggregatePipeEquality() {
        Set<String> x1 = new HashSet<String>();
        Pipe pipe1 = new Pipeline(new LoopPipe(new Pipeline(new AddOnePipe(), new AggregatePipe(x1)), new LoopPipeFunction()));
        pipe1.setStarts(Arrays.asList(1, 2, 3, 4, 5));

        Set<String> x2 = new HashSet<String>();
        Pipe pipe2 = new Pipeline(new AddOnePipe(), new AggregatePipe(x2), new AddOnePipe(), new AggregatePipe(x2));
        pipe2.setStarts(Arrays.asList(1, 2, 3, 4, 5));

        assertTrue(PipeHelper.areEqual(pipe1, pipe2));
    }


    private class LoopPipeFunction implements PipeFunction<LoopPipe.LoopBundle, Boolean> {
        public Boolean compute(LoopPipe.LoopBundle argument) {
            return (argument.getLoops() < 3);
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
