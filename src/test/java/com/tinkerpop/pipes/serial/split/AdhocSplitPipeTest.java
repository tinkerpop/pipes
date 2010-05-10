package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.serial.split.demo.ThreeBinSortSplitPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AdhocSplitPipeTest extends TestCase {

    public void testNumericSortSplitPipeTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 1, 1, 5, 9, 2, 0, 6, 8, 8);
        SplitPipe<Integer> pipe = new ThreeBinSortSplitPipe();
        pipe.setStarts(numbers);
        for (Integer number : pipe.getSplit(0)) {
            assertTrue(number < 4);
            //System.out.println("split-0: " + number);
        }
        for (Integer number : pipe.getSplit(1)) {
            assertTrue(number >= 4 && number < 7);
            //System.out.println("split-1: " + number);
        }
        for (Integer number : pipe.getSplit(2)) {
            assertTrue(number >= 7);
            //System.out.println("split-2: " + number);
        }
    }
}
