package com.tinkerpop.pipes;

import com.tinkerpop.pipes.util.PipeHelper;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FunctionPipeTest extends TestCase {

    public void testClosurePipeWithIdentity() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        FunctionPipe<String, String> pipe = new FunctionPipe<String, String>(new IdentityPipeFunction());
        pipe.setStarts(names);
        assertEquals(PipeHelper.counter(pipe), 4);
        pipe.reset();
        pipe.setStarts(names);
        PipeHelper.areEqual(names.iterator(), pipe.iterator());
    }

    public void testClosurePipeWithFilter() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        FunctionPipe<String, String> pipe = new FunctionPipe<String, String>(new StartsWithPipeFunction());
        pipe.setStarts(names);
        assertEquals(PipeHelper.counter(pipe), 2);
        pipe.reset();
        pipe.setStarts(names);
        PipeHelper.areEqual(Arrays.asList("povel", "peter").iterator(), pipe.iterator());
    }

    private class IdentityPipeFunction implements PipeFunction<Iterator, Object> {
        public Object compute(Iterator argument) {
            return argument.next();
        }
    }

    private class StartsWithPipeFunction implements PipeFunction<Iterator, Object> {
        public Object compute(Iterator argument) {
            while (true) {
                Object s = argument.next();
                if (((String) s).startsWith("p"))
                    return s;
            }
        }
    }
}
