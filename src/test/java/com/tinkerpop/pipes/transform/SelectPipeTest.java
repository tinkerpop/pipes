package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.PipesPipeline;
import com.tinkerpop.pipes.util.Row;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SelectPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipeline<String, Row> pipe = new PipesPipeline<String, Row>(Arrays.asList("1", "22", "333")).as("a").add(new StringLengthPipe()).as("b").select();
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Row list = pipe.next();
            assertEquals(list.size(), 2);
            assertEquals(((String)list.getColumn("a")).length(), counter);
            assertEquals(list.getColumn("b"), counter);
            assertEquals(((String) list.get(0)).length(), counter);
            assertEquals(list.get(1), counter);
        }
        assertEquals(counter, 3);
    }

    public void testPipeWithFunctions() {
        Pipeline<String, Row> pipe = new PipesPipeline<String, Row>(Arrays.asList("1", "22", "333")).as("a").add(new StringLengthPipe()).as("b").select(new PipeFunction<Object, Integer>() {
            public Integer compute(Object object) {
                return object.toString().length();
            }
        });
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Row list = pipe.next();
            assertEquals(list.size(), 2);
            assertEquals(list.get(0), counter);
            assertEquals(list.get(1), 1);

            assertEquals(list.getColumn("a"), counter);
            assertEquals(list.getColumn("b"), 1);
        }
        assertEquals(counter, 3);
    }

    public void testPipeWithFunctionsAndColumnNames() {
        Pipeline<String, Row> pipe = new PipesPipeline<String, Row>(Arrays.asList("1", "22", "333")).as("a").add(new StringLengthPipe()).as("b").select(Arrays.asList("a"), new PipeFunction<Object, Integer>() {
            public Integer compute(Object object) {
                return object.toString().length();
            }
        });
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            Row list = pipe.next();
            assertEquals(list.size(), 1);
            assertEquals(list.get(0), counter);

            assertEquals(list.getColumn("a"), counter);
        }
        assertEquals(counter, 3);
    }

    private class StringLengthPipe extends AbstractPipe<String, Integer> {
        public Integer processNextStart() {
            return this.starts.next().length();
        }
    }
}
