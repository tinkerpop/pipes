package com.tinkerpop.pipes.serial.merge;

import com.tinkerpop.pipes.serial.Pipe;
import com.tinkerpop.pipes.serial.Pipeline;
import com.tinkerpop.pipes.serial.split.SplitPipe;
import com.tinkerpop.pipes.serial.split.demo.ThreeBinSortSplitPipe;
import com.tinkerpop.pipes.serial.util.ProductPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AdhocMergePipeTest extends TestCase {

    public void testNumericSortSplitMergePipeTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 1, 1, 5, 9, 2, 0, 6, 8, 8);
        SplitPipe<Integer> splitPipe = new ThreeBinSortSplitPipe();
        Pipe<Integer, ProductPipe.Pair> pipe0 = new ProductPipe<Integer, Integer>(0, ProductPipe.Join.LEFT);
        Pipe<Integer, ProductPipe.Pair> pipe1 = new ProductPipe<Integer, Integer>(1, ProductPipe.Join.LEFT);
        Pipe<Integer, ProductPipe.Pair> pipe2 = new ProductPipe<Integer, Integer>(2, ProductPipe.Join.LEFT);
        pipe0.setStarts((Iterator<Integer>)splitPipe.getSplit(0));
        pipe1.setStarts((Iterator<Integer>)splitPipe.getSplit(1));
        pipe2.setStarts((Iterator<Integer>)splitPipe.getSplit(2));
        Pipe<Iterator<ProductPipe.Pair>, ProductPipe.Pair> mergePipe = new ExhaustiveMergePipe<ProductPipe.Pair>();
        mergePipe.setStarts((Iterator)Arrays.asList(pipe0,pipe1,pipe2).iterator());
        splitPipe.setStarts(numbers);
        assertTrue(pipe0.hasNext());
        assertTrue(pipe1.hasNext());
        assertTrue(pipe2.hasNext());
        assertTrue(splitPipe.getSplit(0).hasNext());
        assertTrue(splitPipe.getSplit(1).hasNext());
        assertTrue(splitPipe.getSplit(2).hasNext());
        int counter = 0;
        int current = 0;
        while(mergePipe.hasNext()) {
            ProductPipe.Pair pair = mergePipe.next();
            //System.out.println(pair);
            counter++;
        }
        assertEquals(counter,numbers.size());
    }

    public void testNumericSortSplitMergePipePipelineTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 1, 1, 5, 9, 2, 0, 6, 8, 8);
        SplitPipe<Integer> splitPipe = new ThreeBinSortSplitPipe();
        Pipe<Integer, ProductPipe.Pair> pipe0 = new ProductPipe<Integer, Integer>(0, ProductPipe.Join.LEFT);
        Pipe<Integer, ProductPipe.Pair> pipe1 = new ProductPipe<Integer, Integer>(1, ProductPipe.Join.LEFT);
        Pipe<Integer, ProductPipe.Pair> pipe2 = new ProductPipe<Integer, Integer>(2, ProductPipe.Join.LEFT);
        pipe0.setStarts((Iterator<Integer>)splitPipe.getSplit(0));
        pipe1.setStarts((Iterator<Integer>)splitPipe.getSplit(1));
        pipe2.setStarts((Iterator<Integer>)splitPipe.getSplit(2));
        Pipe<Iterator<ProductPipe.Pair>, ProductPipe.Pair> mergePipe = new ExhaustiveMergePipe<ProductPipe.Pair>();
        mergePipe.setStarts((Iterator)Arrays.asList(pipe0,pipe1,pipe2).iterator());
        Pipeline<Integer, ProductPipe.Pair> pipeline = new Pipeline<Integer, ProductPipe.Pair>();
        pipeline.setPipes(splitPipe, mergePipe);
        pipeline.setStarts(numbers);

        assertTrue(pipe0.hasNext());
        assertTrue(pipe1.hasNext());
        assertTrue(pipe2.hasNext());
        assertTrue(splitPipe.getSplit(0).hasNext());
        assertTrue(splitPipe.getSplit(1).hasNext());
        assertTrue(splitPipe.getSplit(2).hasNext());
        int counter = 0;
        int current = 0;
        while(pipeline.hasNext()) {
            ProductPipe.Pair pair = pipeline.next();
            //System.out.println(pair);
            counter++;
        }
        assertEquals(counter,numbers.size());
    }
}
