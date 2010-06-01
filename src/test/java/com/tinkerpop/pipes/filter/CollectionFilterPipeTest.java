package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CollectionFilterPipeTest extends TestCase {

    public void testBasicCollectionFilterEquals() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Set<String> collection = new HashSet<String>(Arrays.asList("marko", "pavel"));
        Pipe<String, String> pipe1 = new CollectionFilterPipe<String>(collection, ComparisonFilterPipe.Filter.EQUALS);
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("marko") || name.equals("pavel"));
        }
        assertEquals(counter, 4);
    }

     public void testBasicCollectionFilterNotEquals() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Set<String> collection = new HashSet<String>(Arrays.asList("marko", "pavel"));
        Pipe<String, String> pipe1 = new CollectionFilterPipe<String>(collection, ComparisonFilterPipe.Filter.NOT_EQUALS);
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh"));
        }
        assertEquals(counter, 2);
    }
}
