package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.serial.merge.RobinMergePipe;
import com.tinkerpop.pipes.serial.split.demo.ThreeBinSortSplitPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AdhocSplitPipeTest extends TestCase {

    public void testNumericSortSplitPipeNormalTest() {
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

    public void testNumericSortSplitPipeZeroTest() {
        List<Integer> numbers = Arrays.asList();
        SplitPipe<Integer> pipe = new ThreeBinSortSplitPipe();
        pipe.setStarts(numbers);
        assertFalse(pipe.getSplit(0).hasNext());
        assertFalse(pipe.getSplit(1).hasNext());
        assertFalse(pipe.getSplit(2).hasNext());
    }

    /*public void testFeedbackSplitPipe() {
        List<String> words = Arrays.asList("a");
        RobinMergePipe<String> pipe1 = new RobinMergePipe<String>();
        SplitPipe<String> pipe2 = new AbstractSplitPipe<String>(2) {
            public void routeNext() {
                if(this.hasNext()) {
                    String s = this.next();
                    System.out.println("here: " + s);
                    if(s.length() < 6) {
                        this.splits.get(0).add(s + "a");
                        //System.out.println("herehere");
                    } else {
                        this.splits.get(1).add(s);
                    }
                }
            }
        };
        pipe1.setStarts(Arrays.asList(words.iterator(),pipe2.getSplit(0)));
        pipe2.setStarts((Iterator<String>)pipe1);
        while(pipe2.getSplit(1).hasNext()) {
            System.out.println(pipe2.getSplit(1).next());
            //System.out.println(pipe2.getSplit(1).next());

        }
    }*/

}
