package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.PipesFunction;
import com.tinkerpop.pipes.util.PipesPipeline;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExceptFilterPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Pipe<String, String> pipe1 = new ExceptFilterPipe<String>(new HashSet<String>(Arrays.asList("marko", "pavel")));
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh"));
        }
        assertEquals(counter, 2);
    }

    public void testPipeNoExceptions() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Pipe<String, String> pipe1 = new ExceptFilterPipe<String>(new HashSet<String>(Arrays.asList("bill")));
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko") || name.equals("pavel"));
        }
        assertEquals(counter, 6);
    }

    public void testPipeNoExceptions2() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Pipe<String, String> pipe1 = new ExceptFilterPipe<String>(new HashSet<String>());
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh") || name.equals("marko") || name.equals("pavel"));
        }
        assertEquals(counter, 6);
    }

    public void testAsPipeExcept() {
        List<String> list = new PipesPipeline<String, String>(Arrays.asList("1", "2", "3"))._().as("x").transform(new PipesFunction<String, String>() {
            @Override
            public String compute(String argument) {
                if (argument.equals("1"))
                    return "1";
                else
                    return argument + argument;
            }
        })._()._().except("x").toList();
        assertEquals(list.size(), 2);
        assertTrue(list.contains("22"));
        assertTrue(list.contains("33"));
    }
}