package com.tinkerpop.pipes.serial.util;

import com.tinkerpop.pipes.serial.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PrintStreamPipeTest extends TestCase {

    public void testPrintStreamPipeNormal() {
        List<String> names = Arrays.asList("marko","peter","josh","povel");
        Pipe<String,String> pipe = new PrintStreamPipe<String>(System.out);
        pipe.setStarts(names);
        int counter = 0;
        while(pipe.hasNext()) {
            pipe.next();
            counter++;
        }
        assertEquals(counter, names.size());
    }
}
