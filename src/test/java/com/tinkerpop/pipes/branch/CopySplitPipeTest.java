package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.filter.ObjectFilterPipe;
import com.tinkerpop.pipes.transform.PathPipe;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CopySplitPipeTest extends TestCase {

    public void testFairMerge() {

        Pipe<String, String> pipe1 = new AppendCharPipe("a");
        Pipe<String, String> pipe2 = new AppendCharPipe("b");
        Pipe<String, String> pipe3 = new AppendCharPipe("c");


        CopySplitPipe<String> copySplitPipe = new CopySplitPipe<String>(pipe1, pipe2, pipe3);
        FairMergePipe<String> fairMergePipe = new FairMergePipe<String>(copySplitPipe.getPipes());
        copySplitPipe.setStarts(Arrays.asList("1", "2"));
        int counter = 0;
        List<String> list = new ArrayList<String>();
        while (fairMergePipe.hasNext()) {
            counter++;
            list.add(fairMergePipe.next());
        }
        assertEquals(list.size(), 6);
        assertEquals(counter, 6);
        assertEquals(list.get(0), "1a");
        assertEquals(list.get(1), "1b");
        assertEquals(list.get(2), "1c");
        assertEquals(list.get(3), "2a");
        assertEquals(list.get(4), "2b");
        assertEquals(list.get(5), "2c");
    }

    public void testExhaustMerge() {

        Pipe<String, String> pipe1 = new AppendCharPipe("a");
        Pipe<String, String> pipe2 = new AppendCharPipe("b");
        Pipe<String, String> pipe3 = new AppendCharPipe("c");


        CopySplitPipe<String> copySplitPipe = new CopySplitPipe<String>(pipe1, pipe2, pipe3);
        ExhaustMergePipe<String> exhaustMergePipe = new ExhaustMergePipe<String>(copySplitPipe.getPipes());
        copySplitPipe.setStarts(Arrays.asList("1", "2"));
        int counter = 0;
        List<String> list = new ArrayList<String>();
        while (exhaustMergePipe.hasNext()) {
            counter++;
            list.add(exhaustMergePipe.next());
        }
        assertEquals(list.size(), 6);
        assertEquals(counter, 6);
        assertEquals(list.get(0), "1a");
        assertEquals(list.get(1), "2a");
        assertEquals(list.get(2), "1b");
        assertEquals(list.get(3), "2b");
        assertEquals(list.get(4), "1c");
        assertEquals(list.get(5), "2c");
    }

    public void testExhaustMergeFilter() {

        Pipe<String, String> pipe1 = new ObjectFilterPipe<String>("x", FilterPipe.Filter.EQUAL);
        Pipe<String, String> pipe2 = new AppendCharPipe("b");
        Pipe<String, String> pipe3 = new AppendCharPipe("c");

        CopySplitPipe<String> copySplitPipe = new CopySplitPipe<String>(pipe1, pipe2, pipe3);
        ExhaustMergePipe<String> exhaustMergePipe = new ExhaustMergePipe<String>(copySplitPipe.getPipes());
        copySplitPipe.setStarts(Arrays.asList("1", "2"));
        int counter = 0;
        List<String> list = new ArrayList<String>();
        while (exhaustMergePipe.hasNext()) {
            counter++;
            list.add(exhaustMergePipe.next());
        }
        assertEquals(list.size(), 4);
        assertEquals(counter, 4);
        assertEquals(list.get(0), "1b");
        assertEquals(list.get(1), "2b");
        assertEquals(list.get(2), "1c");
        assertEquals(list.get(3), "2c");
    }

    public void testWithExhaustMergePath() {
        Pipe<String, String> pipe1 = new AppendCharPipe("a");
        Pipe<String, String> pipe2 = new AppendCharPipe("b");
        Pipe<String, String> pipe3 = new AppendCharPipe("c");

        CopySplitPipe<String> copySplitPipe = new CopySplitPipe<String>(pipe1, pipe2, pipe3);
        ExhaustMergePipe<String> exhaustMergePipe = new ExhaustMergePipe<String>(copySplitPipe.getPipes());
        PathPipe<String> pathPipe = new PathPipe<String>();
        pathPipe.setStarts(exhaustMergePipe);
        copySplitPipe.setStarts(Arrays.asList("1", "2"));
        int counter = 0;
        List<List> list = new ArrayList<List>();
        while (pathPipe.hasNext()) {
            List path = pathPipe.next();
            list.add(path);
            //System.out.println(path);
            if (counter == 0) {
                assertEquals(path.get(0), "1");
                assertEquals(path.get(1), "1a");
            } else if (counter == 1) {
                assertEquals(path.get(0), "2");
                assertEquals(path.get(1), "2a");
            } else if (counter == 2) {
                assertEquals(path.get(0), "1");
                assertEquals(path.get(1), "1b");
            } else if (counter == 3) {
                assertEquals(path.get(0), "2");
                assertEquals(path.get(1), "2b");
            } else if (counter == 4) {
                assertEquals(path.get(0), "1");
                assertEquals(path.get(1), "1c");
            } else if (counter == 5) {
                assertEquals(path.get(0), "2");
                assertEquals(path.get(1), "2c");
            } else {
                assertFalse(true);
            }
            counter++;

        }
        assertEquals(list.size(), 6);
        assertEquals(counter, 6);
    }

    public void testWithFairMergePath() {
        Pipe<String, String> pipe1 = new AppendCharPipe("a");
        Pipe<String, String> pipe2 = new AppendCharPipe("b");
        Pipe<String, String> pipe3 = new AppendCharPipe("c");

        CopySplitPipe<String> copySplitPipe = new CopySplitPipe<String>(pipe1, pipe2, pipe3);
        FairMergePipe<String> fairMergePipe = new FairMergePipe<String>(copySplitPipe.getPipes());
        PathPipe<String> pathPipe = new PathPipe<String>();
        pathPipe.setStarts(fairMergePipe);
        copySplitPipe.setStarts(Arrays.asList("1", "2"));
        int counter = 0;
        List<List> list = new ArrayList<List>();
        while (pathPipe.hasNext()) {
            List path = pathPipe.next();
            list.add(path);
            //System.out.println(path);
            if (counter == 0) {
                assertEquals(path.get(0), "1");
                assertEquals(path.get(1), "1a");
            } else if (counter == 1) {
                assertEquals(path.get(0), "1");
                assertEquals(path.get(1), "1b");
            } else if (counter == 2) {
                assertEquals(path.get(0), "1");
                assertEquals(path.get(1), "1c");
            } else if (counter == 3) {
                assertEquals(path.get(0), "2");
                assertEquals(path.get(1), "2a");
            } else if (counter == 4) {
                assertEquals(path.get(0), "2");
                assertEquals(path.get(1), "2b");
            } else if (counter == 5) {
                assertEquals(path.get(0), "2");
                assertEquals(path.get(1), "2c");
            } else {
                assertFalse(true);
            }
            counter++;

        }
        assertEquals(list.size(), 6);
        assertEquals(counter, 6);
    }


    private class AppendCharPipe extends AbstractPipe<String, String> {
        String c;

        public AppendCharPipe(String c) {
            this.c = c;
        }

        public String processNextStart() {
            return this.starts.next() + c;
        }
    }
}
