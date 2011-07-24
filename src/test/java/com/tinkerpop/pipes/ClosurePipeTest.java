package com.tinkerpop.pipes;

import com.tinkerpop.pipes.util.PipeHelper;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ClosurePipeTest extends TestCase {

    public void testClosurePipeWithIdentity() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        ClosurePipe<String, String> pipe = new ClosurePipe<String, String>(new IdentityPipeClosure());
        pipe.setStarts(names);
        assertEquals(PipeHelper.counter(pipe), 4);
        pipe.reset();
        pipe.setStarts(names);
        PipeHelper.areEqual(names.iterator(), pipe.iterator());
    }

    public void testClosurePipeWithFilter() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        ClosurePipe<String, String> pipe = new ClosurePipe<String, String>(new StartsWithPipeClosure());
        pipe.setStarts(names);
        assertEquals(PipeHelper.counter(pipe), 2);
        pipe.reset();
        pipe.setStarts(names);
        PipeHelper.areEqual(Arrays.asList("povel", "peter").iterator(), pipe.iterator());
    }

    private class IdentityPipeClosure implements PipeClosure {
        private ClosurePipe pipe;

        public Object compute(Object... parameters) {
            return this.pipe.s();
        }

        public void setPipe(Pipe pipe) {
            this.pipe = (ClosurePipe) pipe;
        }
    }

    private class StartsWithPipeClosure implements PipeClosure {
        private ClosurePipe pipe;

        public Object compute(Object... parameters) {
            while (true) {
                Object s = this.pipe.s();
                if (((String) s).startsWith("p"))
                    return s;
            }
        }

        public void setPipe(Pipe pipe) {
            this.pipe = (ClosurePipe) pipe;
        }
    }
}
