package com.tinkerpop.pipes.serial.split;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AdhocSplitPipeTest extends TestCase {

    public void testNumericSortSplitPipeTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 1, 1, 5, 9, 2, 0, 6, 8, 8);
        SplitPipe<Integer> pipe = new ThreeBinSortSplit();
        pipe.setStarts(numbers);
        for (Integer number : pipe.getSplit(0)) {
            assertTrue(number < 4);
            System.out.println("split-0: " + number);
        }
        for (Integer number : pipe.getSplit(1)) {
            assertTrue(number >= 4 && number < 7);
            System.out.println("split-1: " + number);
        }
        for (Integer number : pipe.getSplit(2)) {
            assertTrue(number >= 7);
            System.out.println("split-2: " + number);
        }
    }

    private class ThreeBinSortSplit extends AbstractSplitPipe<Integer> {

        public ThreeBinSortSplit() {
            super(3);
        }

        public void fillNext(final int requestingSplit) {
            if (this.hasNext()) {
                Integer number = this.next();
                if (number < 4) {
                    this.splits.get(0).add(number);
                } else if (number >= 4 && number < 7) {
                    this.splits.get(1).add(number);
                } else {
                    this.splits.get(2).add(number);
                }
            }
        }
    }
}
