package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.PipesPipeline;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Relying on the Collections.shuffle to do the randomization of list items so not specifically testing that
 * output order is different.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class ShufflePipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe<String, String> pipe = new ShufflePipe<String>();
        pipe.enablePath(true);
        pipe.setStarts(Arrays.asList("marko", "josh", "peter"));
        List list = new ArrayList();
        while (pipe.hasNext()) { list.add(pipe.next()); }
        assertEquals(list.size(), 3);
        assertTrue(list.contains("marko"));
        assertTrue(list.contains("peter"));
        assertTrue(list.contains("josh"));
        assertFalse(pipe.hasNext());
    }

    public void testPipeTestBasicPath() {
        Pipe<String, String> pipeA = new RemoveCharPipe();
        Pipe<String, String> pipeB = new ShufflePipe<String>();
        Pipeline<String, List<String>> pipeline = new Pipeline<String, List<String>>(pipeA, pipeB);
        pipeline.setStarts(Arrays.asList("marko", "josh", "peter"));
        List list = new ArrayList();
        while (pipeline.hasNext()) { list.add(pipeline.next()); }
        assertEquals(list.size(), 3);
        assertTrue(list.contains("mark"));
        assertTrue(list.contains("pete"));
        assertTrue(list.contains("jos"));
        assertFalse(pipeline.hasNext());
    }

    public void testPipeTestBasicPath2() {
        Pipe<String, String> pipeA = new RemoveCharPipe();
        Pipe<String, String> pipeB = new ShufflePipe<String>();
        Pipe<List<String>, String> pipeC = new ScatterPipe<List<String>, String>();
        Pipeline<String, String> pipeline = new Pipeline<String, String>(pipeA, pipeB, pipeC);
        pipeline.setStarts(Arrays.asList("marko", "josh", "peter"));
        int counter = 0;
        while (pipeline.hasNext()) {
            String string = pipeline.next();
            assertTrue(string.equals("mark") || string.equals("jos") || string.equals("pete"));
            counter++;
        }
        assertEquals(counter, 3);
        assertFalse(pipeline.hasNext());
    }

    public void testGatherLooping() {
        PipesPipeline pipeline = new PipesPipeline(Arrays.asList("marko", "josh", "peter")).add(new RemoveCharPipe()).shuffle().scatter().loop(3, LoopPipe.createLoopsFunction(3));
        while (pipeline.hasNext()) {
            String s = (String) pipeline.next();
            if (s.startsWith("m"))
                assertEquals(s, "mar");
            else if (s.startsWith("j"))
                assertEquals(s, "jo");
            else if (s.startsWith("p"))
                assertEquals(s, "pet");
            else
                throw new RuntimeException("An unexpected String came through the pipeline.");
        }
    }

    private class RemoveCharPipe extends AbstractPipe<String, String> {
        public String processNextStart() {
            while (true) {
                String s = this.starts.next();
                if (s.length() > 1) {
                    return s.substring(0, s.length() - 1);
                }
            }
        }
    }
}

