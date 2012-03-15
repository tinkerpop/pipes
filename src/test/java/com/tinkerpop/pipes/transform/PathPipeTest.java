package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.Arrays;
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

    public void testTrue() {
        assertTrue(true);
    }

    private class StringLengthPipe extends AbstractPipe<String, Integer> {
        public Integer processNextStart() {
            return this.starts.next().length();
        }
    }
}
