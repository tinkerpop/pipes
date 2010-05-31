package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.util.ProductPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class KeyCombinePipeTest extends TestCase {

    public void testKeyCombinePipeNormal() {
        List<String> names = Arrays.asList("marko", "peter", "josh", "marko");
        Pipe pipe1 = new ProductPipe<String, Integer>(1, ProductPipe.Join.RIGHT);
        SideEffectPipe<ProductPipe.Pair, ProductPipe.Pair, Map<String, List<Integer>>> pipe2 = new KeyCombinePipe<String, Integer>();
        Pipeline<String, ProductPipe.Pair> pipeline = new Pipeline(Arrays.asList(pipe1, pipe2));
        pipeline.setStarts(names);
        int counter = 0;
        for (ProductPipe.Pair pair : pipeline) {
            counter++;
            //System.out.println(pair);
        }
        assertEquals(counter, 4);
        assertEquals(pipe2.getSideEffect().get("marko").get(0), new Integer(1));
        assertEquals(pipe2.getSideEffect().get("marko").get(1), new Integer(1));
        assertEquals(pipe2.getSideEffect().get("peter").get(0), new Integer(1));
        assertEquals(pipe2.getSideEffect().get("josh").get(0), new Integer(1));
        assertEquals(pipe2.getSideEffect().size(), 3);
        //System.out.println(pipe2.getSideEffect());


    }
}
