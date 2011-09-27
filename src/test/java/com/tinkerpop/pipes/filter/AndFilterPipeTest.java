package com.tinkerpop.pipes.filter;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AndFilterPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "povel", "marko");
        ObjectFilterPipe<String> pipe1 = new ObjectFilterPipe<String>("marko", FilterPipe.Filter.EQUAL);
        ObjectFilterPipe<String> pipe2 = new ObjectFilterPipe<String>("marko", FilterPipe.Filter.EQUAL);
        AndFilterPipe<String> andFilterPipe = new AndFilterPipe<String>(pipe1, pipe2);
        andFilterPipe.setStarts(names);
        int counter = 0;
        while (andFilterPipe.hasNext()) {
            assertEquals(andFilterPipe.next(),"marko");
            counter++;
        }
        assertEquals(counter, 2);
    }

    public void testPipeNoElements() {
        List<String> names = Arrays.asList();
        ObjectFilterPipe<String> pipe1 = new ObjectFilterPipe<String>("marko", FilterPipe.Filter.EQUAL);
        ObjectFilterPipe<String> pipe2 = new ObjectFilterPipe<String>("povel", FilterPipe.Filter.EQUAL);
        AndFilterPipe<String> andFilterPipe = new AndFilterPipe<String>(pipe1, pipe2);
        andFilterPipe.setStarts(names);
        int counter = 0;
        while (andFilterPipe.hasNext()) {
            andFilterPipe.next();
            counter++;
        }
        assertEquals(counter, 0);
    }




}
