package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Query;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrFilterPipeTest extends TestCase {

    public void testOrPipeBasic() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "povel", "marko");
        ObjectFilterPipe<String> pipe1 = new ObjectFilterPipe<String>("marko", Query.Compare.EQUAL);
        ObjectFilterPipe<String> pipe2 = new ObjectFilterPipe<String>("povel", Query.Compare.EQUAL);
        OrFilterPipe<String> orFilterPipe = new OrFilterPipe<String>(pipe1, pipe2);
        orFilterPipe.setStarts(names);
        int counter = 0;
        while (orFilterPipe.hasNext()) {
            String name = orFilterPipe.next();
            assertTrue(name.equals("marko") || name.equals("povel"));
            counter++;
        }
        assertEquals(counter, 4);
    }

    public void testFutureFilter() {
        List<String> names = Arrays.asList("marko", "peter", "josh", "marko", "jake", "marko", "marko");
        Pipe<String, Integer> pipeA = new CharacterCountPipe();
        Pipe<Integer, Integer> pipeB = new ObjectFilterPipe<Integer>(4, Query.Compare.NOT_EQUAL);
        Pipe<String, String> pipe1 = new OrFilterPipe<String>(new Pipeline<String, Integer>(pipeA, pipeB));
        Pipeline<String, String> pipeline = new Pipeline<String, String>(pipe1);
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
