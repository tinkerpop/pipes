package com.tinkerpop.pipes.sideeffect;


import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.AbstractPipeClosure;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.CollectionFilterPipe;
import com.tinkerpop.pipes.filter.ExceptFilterPipe;
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.filter.RetainFilterPipe;
import com.tinkerpop.pipes.transform.GatherPipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.transform.SideEffectCapPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AggregatePipeTest extends TestCase {

    public void testReset() {
        List<String> aggregate = new ArrayList<String>();
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        AggregatePipe<String> pipe = new AggregatePipe<String>(aggregate);
        pipe.setStarts(list.iterator());
        assertTrue(pipe.hasNext());
        assertEquals(aggregate.get(0), "marko");
        assertEquals(aggregate.size(), 6);
        pipe.reset();
        assertEquals(aggregate.get(0), "marko");
        assertEquals(aggregate.size(), 6);
        assertFalse(pipe.hasNext());
        assertEquals(pipe.getSideEffect().getClass(), ArrayList.class);
        assertEquals(pipe.getSideEffect().size(), 0);
    }

    public void testAggregatorPipe() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        AggregatePipe<String> pipe = new AggregatePipe<String>(new ArrayList<String>());
        pipe.setStarts(list.iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), list.get(counter));
            counter++;
        }
        assertEquals(counter, 6);
        assertEquals(pipe.getSideEffect().size(), counter);
        assertEquals(list.size(), counter);
        for (int i = 0; i < counter; i++) {
            assertEquals(list.get(i), pipe.getSideEffect().toArray()[i]);
        }

        pipe.reset();
        assertEquals(0, pipe.getSideEffect().size());
        pipe.setStarts(list.iterator());
        counter = 0;
        assertTrue(pipe.hasNext());
        assertEquals(6, pipe.getSideEffect().size());
    }


    public void testAggregatorPipeWithClosure() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        AggregatePipe<String> pipe = new AggregatePipe<String>(new ArrayList<Integer>(), new LengthPipeClosure());
        pipe.setStarts(list.iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), list.get(counter));
            counter++;
        }
        assertEquals(counter, 6);
        assertEquals(pipe.getSideEffect().size(), counter);
        assertEquals(list.size(), counter);
        for (int i = 0; i < counter; i++) {
            assertEquals(list.get(i).length(), pipe.getSideEffect().toArray()[i]);
        }

        pipe.reset();
        assertEquals(0, pipe.getSideEffect().size());
        pipe.setStarts(list.iterator());
        counter = 0;
        assertTrue(pipe.hasNext());
        assertEquals(6, pipe.getSideEffect().size());
    }

    public void testSelfFilter() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        AggregatePipe<String> pipe1 = new AggregatePipe<String>(new ArrayList<String>());
        Pipe pipe2 = new RetainFilterPipe<String>(pipe1.getSideEffect());
        Pipeline<String, String> pipeline = new Pipeline<String, String>(Arrays.asList(pipe1, pipe2));
        pipeline.setStarts(list.iterator());
        int counter = 0;
        assertTrue(pipeline.hasNext());
        while (pipeline.hasNext()) {
            pipeline.next();
            counter++;
        }
        assertEquals(counter, 6);

        pipe1 = new AggregatePipe<String>(new ArrayList<String>());
        pipe2 = new ExceptFilterPipe<String>(pipe1.getSideEffect());
        pipeline = new Pipeline<String, String>(Arrays.asList(pipe1, pipe2));
        pipeline.setStarts(list.iterator());
        counter = 0;
        assertFalse(pipeline.hasNext());
        while (pipeline.hasNext()) {
            pipeline.next();
            counter++;
        }
        assertEquals(counter, 0);
    }

    public void testNullIterator() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        Iterator<String> itty = list.iterator();
        int counter = 0;
        while (itty.hasNext()) {
            itty.next();
            counter++;
        }
        assertEquals(counter, 6);
        assertFalse(itty.hasNext());
        try {
            itty.next();
            assertFalse(true);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
        list = Arrays.asList(null, null, null, null, null, null);
        itty = list.iterator();
        counter = 0;
        while (itty.hasNext()) {
            itty.next();
            counter++;
        }
        assertEquals(counter, 6);
        assertFalse(itty.hasNext());
        try {
            itty.next();
            assertFalse(true);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    public void testNoSuchElement() {
        List<String> list = Arrays.asList(null, null, null);
        AggregatePipe<String> pipe1 = new AggregatePipe<String>(new ArrayList<String>());
        pipe1.setStarts(list.iterator());
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            assertNull(pipe1.next());
        }
        assertEquals(counter, 3);
        try {
            pipe1.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
        counter = 0;
        for (String s : (Collection<String>) pipe1.getSideEffect()) {
            assertNull(s);
            counter++;
        }
        assertEquals(counter, 3);
        assertEquals(pipe1.getSideEffect().size(), 3);
    }

    public void testAggregatorPath() {
        List<String> list = Arrays.asList("marko", "a.", "rodriguez");
        Pipe<String, String> pipe = new Pipeline<String, String>(new AddCharPipe(), new AggregatePipe<String>(new ArrayList<String>()), new AddCharPipe());
        pipe.setStarts(list);
        int counter = 0;
        while (pipe.hasNext()) {
            String string = pipe.next();
            if (counter == 0) {
                List path = pipe.getPath();
                assertEquals(path.size(), 3);
                assertEquals(path.get(0), string.substring(0, string.length() - 2));
                assertEquals(path.get(1), string.substring(0, string.length() - 1));
                assertEquals(path.get(2), string);
                assertEquals(string, "marko..");
            } else if (counter == 1) {
                assertEquals(string, "a...");
            } else if (counter == 2) {
                List path = pipe.getPath();
                assertEquals(path.size(), 3);
                assertEquals(path.get(0), string.substring(0, string.length() - 2));
                assertEquals(path.get(1), string.substring(0, string.length() - 1));
                assertEquals(path.get(2), string);
                assertEquals(string, "rodriguez..");
            } else {
                assertTrue(false);
            }
            counter++;
        }
    }

    public void testEqualityToGatherWithCap() {
        List<String> list = Arrays.asList("marko", "a.", "rodriguez");
        Pipe<String, List<String>> pipeA = new SideEffectCapPipe<String, List<String>>(new AggregatePipe(new LinkedList<String>()));
        Pipe<String, List<String>> pipeB = new GatherPipe<String>();
        pipeA.setStarts(list);
        pipeB.setStarts(list);
        assertTrue(PipeHelper.areEqual(((List) pipeA.next()).iterator(), ((List) pipeB.next()).iterator()));
    }

    public void testEqualityToNonAggregation() {
        List<String> list = Arrays.asList("marko", "a.", "rodriguez");
        Pipe<String, String> pipeA = new AggregatePipe<String>(new HashSet<String>());
        Pipe<String, String> pipeB = new IdentityPipe<String>();
        pipeA.setStarts(list);
        pipeB.setStarts(list);
        assertTrue(PipeHelper.areEqual(pipeA, pipeB));
    }

    private class AddCharPipe extends AbstractPipe<String, String> {

        public String processNextStart() {
            return this.starts.next() + ".";
        }
    }

    private class LengthPipeClosure extends AbstractPipeClosure<Integer, Pipe> {
        public Integer compute(Object... parameters) {
            return ((String) parameters[0]).length();
        }
    }
}
