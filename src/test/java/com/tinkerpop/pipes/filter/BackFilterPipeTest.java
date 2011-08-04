package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BackFilterPipeTest extends TestCase {

    public void testBackFilterWithNoFilter() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        FilterPipe<String> pipe1 = new BackFilterPipe<String>(new IdentityPipe<String>());
        pipe1.setStarts(names);
        assertEquals(PipeHelper.counter(pipe1), 4);
    }


    public void testBackFilterWithFilter() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        BackFilterPipe<String> pipe1 = new BackFilterPipe<String>(new CollectionFilterPipe<String>(Arrays.asList("marko", "povel"), FilterPipe.Filter.NOT_EQUAL));
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
