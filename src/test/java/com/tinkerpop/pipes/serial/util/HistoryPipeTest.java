package com.tinkerpop.pipes.serial.util;

import com.tinkerpop.pipes.serial.AbstractPipe;
import com.tinkerpop.pipes.serial.Pipe;
import com.tinkerpop.pipes.serial.Pipeline;
import com.tinkerpop.pipes.serial.filter.ComparisonFilterPipe;
import com.tinkerpop.pipes.serial.filter.ObjectFilterPipe;
import com.tinkerpop.pipes.serial.sideeffect.BufferPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class HistoryPipeTest extends TestCase {

    public void testHistory() {
        List<String> names = Arrays.asList("marko", "peter", "josh", "marko", "jake", "marko", "marko");
        BufferPipe<String> pipe1 = new BufferPipe<String>(1);
        Pipe<String, Integer> pipe2 = new CharacterCountPipe();
        Pipe<Integer, String> pipe3 = new HistoryPipe<Integer, String>(pipe1, new ObjectFilterPipe<Integer>(4, ComparisonFilterPipe.Filter.NOT_EQUALS));
        Pipeline<String, String> pipeline = new Pipeline<String, String>(pipe1, pipe2, pipe3);
        pipeline.setStarts(names);
        int counter = 0;
        while (pipeline.hasNext()) {
            String name = pipeline.next();
            //System.out.println(name);
            counter++;
            assertTrue((name.equals("marko") || name.equals("peter")) && !name.equals("josh") && !name.equals("jake"));
        }
        assertEquals(counter, 5);
    }

    private class CharacterCountPipe extends AbstractPipe<String, Integer> {
        protected Integer processNextStart() {
            return this.starts.next().length();
        }
    }

}
