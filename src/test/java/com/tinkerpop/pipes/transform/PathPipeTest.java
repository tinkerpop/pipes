package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.IdentityPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.util.Pipeline;
import java.util.ArrayList;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe a = new StringLengthPipe();
        Pipe b = new PathPipe();
        Pipeline<String, List> pipeline = new Pipeline<String, List>(a, b);
        pipeline.setStarts(Arrays.asList("marko", "josh", "pup"));
        int counter = 0;
        while (pipeline.hasNext()) {
            List path = pipeline.next();
            assertEquals(path.size(), 2);
            if (counter == 0) {
                assertEquals(path.get(0), "marko");
                assertEquals(path.get(1), 5);
            } else if (counter == 1) {
                assertEquals(path.get(0), "josh");
                assertEquals(path.get(1), 4);
            } else {
                assertEquals(path.get(0), "pup");
                assertEquals(path.get(1), 3);
            }
            counter++;
        }
        assertEquals(counter, 3);
    }

    public void testPipeFunctions() {
        Pipe a = new StringLengthPipe();
        Pipe b = new PathPipe(new PipeFunction<Object, String>() {
            public String compute(Object object) {
                return object.toString();
            }
        });
        Pipeline<String, List> pipeline = new Pipeline<String, List>(a, b);
        pipeline.setStarts(Arrays.asList("marko", "josh", "pup"));
        int counter = 0;
        while (pipeline.hasNext()) {
            List path = pipeline.next();
            assertEquals(path.size(), 2);
            if (counter == 0) {
                assertEquals(path.get(0), "marko");
                assertEquals(path.get(1), "5");
            } else if (counter == 1) {
                assertEquals(path.get(0), "josh");
                assertEquals(path.get(1), "4");
            } else {
                assertEquals(path.get(0), "pup");
                assertEquals(path.get(1), "3");
            }
            counter++;
        }
        assertEquals(counter, 3);
    }

    public void testPathSelfLoop() {

        Pipe a = new TransformFunctionPipe(new PipeFunction<String, String>() {
            public String compute(String s) {
                return s;
            }
        });
        Pipe b = new IdentityPipe();
        Pipe c = new PathPipe();

        Pipe<String, List<String>> p = new Pipeline<String, List<String>>(a, b, c);
        p.setStarts(Arrays.asList("a", "b", "c"));
        while (p.hasNext()) {
            assertEquals(p.next().size(), 2);
        }
    }

    public void testPathSelfLoopLoop() {
        Pipe a = new TransformFunctionPipe(new PipeFunction<String, String>() {
            public String compute(String s) {
                return s;
            }
        });
        Pipe b = new IdentityPipe();
        Pipe c = new PathPipe();

        Pipe<String, List<String>> p = new Pipeline<String, List<String>>(new LoopPipe(a, LoopPipe.createLoopsFunction(4)), b, c);
        p.setStarts(Arrays.asList("a", "b", "c"));
        while (p.hasNext()) {
            assertEquals(p.next().size(), 4);
        }
    }

    public void testPathIsAvailableDuringTraversal() {
        Pipe a = new TransformFunctionPipe(new PipeFunction<String, String>() {
            public String compute(String s) {
                return s;
            }
        });
        Pipe b = new IdentityPipe();

        List paths = new ArrayList();
        Pipe c = new RecordPathPipe(paths);

        Pipe<String, List<String>> p = new Pipeline<String, List<String>>(new LoopPipe(a, LoopPipe.createLoopsFunction(4)), b, c);
        p.setStarts(Arrays.asList("a", "b", "c"));

        // consume the pipe
        while (p.hasNext()) {
            p.next();
        }

        // now check that paths were recorded correctly
        assertEquals(paths.size(), 4);
        assertPath((List) paths.get(0), new String[]{null});
        assertPath((List) paths.get(1), new String[]{"a", "a", "a", "a"});
        assertPath((List) paths.get(2), new String[]{"b", "b", "b", "b"});
        assertPath((List) paths.get(3), new String[]{"c", "c", "c", "c"});
    }

    private void assertPath(List path, String[] pathElements) {
        assertEquals(pathElements.length, path.size());
        for (int i = 0; i < pathElements.length; i++) {
            assertEquals(pathElements[i], path.get(i));
        }
    }

    private class StringLengthPipe extends AbstractPipe<String, Integer> {
        public Integer processNextStart() {
            return this.starts.next().length();
        }
    }

    private class RecordPathPipe<S, T> extends AbstractPipe<S, T> {
        private final List paths;

        public RecordPathPipe(final List paths) {
            this.paths = paths;
        }

        @Override
        public void setStarts(final Iterator<S> starts) {
            super.setStarts(starts);
            enablePath(true);
        }

        @Override
        protected T processNextStart() {
            paths.add(getCurrentPath());
            final T t = (T) this.starts.next();
            return t;
        }
    }
}
