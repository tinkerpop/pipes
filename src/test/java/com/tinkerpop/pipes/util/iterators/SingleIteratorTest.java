package com.tinkerpop.pipes.util.iterators;

import com.tinkerpop.pipes.TimingTest;
import com.tinkerpop.pipes.filter.BackFilterPipe;
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.transform.IdentityPipe;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SingleIteratorTest extends TimingTest {

    public void testSingleIterator() {
        SingleIterator<String> itty = new SingleIterator<String>("marko");
        for (int i = 0; i < 25; i++) {
            assertTrue(itty.hasNext());
        }
        assertEquals(itty.next(), "marko");
        for (int i = 0; i < 25; i++) {
            assertFalse(itty.hasNext());
        }
        try {
            itty.next();
            assertFalse(true);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    public void testTiming() {
        int numberToDo = 100000;
        this.stopWatch();
        for (int i = 0; i < numberToDo; i++) {
            Arrays.asList("marko").iterator().next();
        }
        printPerformance("Arrays.asList() for single object", numberToDo, "arrays constructed and next()'d", this.stopWatch());
        this.stopWatch();
        for (int i = 0; i < numberToDo; i++) {
            new SingleIterator<String>("marko").next();
        }
        printPerformance("SingleIterator for single object", numberToDo, "iterators constructed and next()'d", this.stopWatch());
        //
        this.stopWatch();
        for (int i = 0; i < numberToDo; i++) {
            Arrays.asList("marko").iterator().next();
        }
        printPerformance("Arrays.asList() for single object", numberToDo, "arrays constructed and next()'d", this.stopWatch());
        this.stopWatch();
        for (int i = 0; i < numberToDo; i++) {
            new SingleIterator<String>("marko").next();
        }
        printPerformance("SingleIterator for single object", numberToDo, "iterators constructed and next()'d", this.stopWatch());

        this.stopWatch();
        for (int i = 0; i < numberToDo; i++) {
           SingleIterator<String> iterator = new SingleIterator<String>("marko");
           iterator.next();
			try {
				iterator.next();
			} catch (NoSuchElementException e) {
				// Ignore
			}
        }
        printPerformance("SingleIterator for single object", numberToDo, "iterators constructed and next()'d twice to cause NoSuchElementException", this.stopWatch());
    }


}
