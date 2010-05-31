package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ProductPipeTest extends TestCase {

    public void testProductPipeNormalRight() {
        List<String> names = Arrays.asList("marko", "josh", "peter");
        Pipe<String, ProductPipe.Pair> pipe = new ProductPipe<String, Integer>(10, ProductPipe.Join.RIGHT);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        int counter = 0;
        for (ProductPipe.Pair pair : pipe) {
            assertEquals(pair.getB(), 10);
            assertTrue(pair.getA().equals("marko") || pair.getA().equals("josh") || pair.getA().equals("peter"));
            counter++;
        }
        assertEquals(counter, 3);
        assertFalse(pipe.hasNext());
    }

    public void testProductPipeNormalLeft() {
        List<String> names = Arrays.asList("marko", "josh", "peter");
        Pipe<String, ProductPipe.Pair> pipe = new ProductPipe<String, Integer>(10, ProductPipe.Join.LEFT);
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        int counter = 0;
        for (ProductPipe.Pair pair : pipe) {
            assertEquals(pair.getA(), 10);
            assertTrue(pair.getB().equals("marko") || pair.getB().equals("josh") || pair.getB().equals("peter"));
            counter++;
        }
        assertEquals(counter, 3);
        assertFalse(pipe.hasNext());
    }

    public void testProductPipeZero() {
        List<String> names = Arrays.asList();
        Pipe<String, ProductPipe.Pair> pipe = new ProductPipe<String, Integer>(10, ProductPipe.Join.RIGHT);
        pipe.setStarts(names);
        assertFalse(pipe.hasNext());
    }
}
