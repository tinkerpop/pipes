package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;
import com.tinkerpop.pipes.merge.ExhaustiveMergePipe;
import com.tinkerpop.pipes.split.CopySplitPipe;
import com.tinkerpop.pipes.split.SplitPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IndexElementPipeTest extends TestCase {

    public void testIndexElementBasicMarko() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe<Index, Vertex> pipe = new IndexElementPipe<Vertex>("name", "marko");
        pipe.setStarts(new SingleIterator<Index>(graph.getIndex()));
        int counter = 0;
        Set<Vertex> vertices = new HashSet<Vertex>();
        while (pipe.hasNext()) {
            counter++;
            Vertex vertex = pipe.next();
            vertices.add(vertex);
            assertEquals(vertex.getProperty("name"), "marko");
        }
        assertEquals(counter, 1);
        assertEquals(vertices.size(), 1);
    }

    public void testIndexElementBasicJava() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe<Index, Vertex> pipe = new IndexElementPipe<Vertex>("lang", "java");
        pipe.setStarts(new SingleIterator<Index>(graph.getIndex()));
        int counter = 0;
        Set<Vertex> vertices = new HashSet<Vertex>();
        while (pipe.hasNext()) {
            counter++;
            Vertex vertex = pipe.next();
            vertices.add(vertex);
            assertEquals(vertex.getProperty("lang"), "java");
        }
        assertEquals(counter, 2);
        assertEquals(vertices.size(), 2);
    }

    public void testIndexElementMergeTwo() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe<Index, Vertex> pipe1 = new IndexElementPipe<Vertex>("name", "marko");
        Pipe<Index, Vertex> pipe2 = new IndexElementPipe<Vertex>("lang", "java");
        SplitPipe<Index> split = new CopySplitPipe<Index>(2);
        pipe1.setStarts((Iterator) split.getSplit(0));
        pipe2.setStarts((Iterator) split.getSplit(1));
        Pipe<Iterator<Vertex>, Vertex> merge = new ExhaustiveMergePipe<Vertex>();
        merge.setStarts((Iterator) Arrays.asList(pipe1, pipe2).iterator());
        split.setStarts(new SingleIterator<Index>(graph.getIndex()));
        int counter = 0;
        while (merge.hasNext()) {
            counter++;
            Vertex vertex = merge.next();
            assertTrue(vertex.getProperty("name").equals("marko") || vertex.getProperty("lang").equals("java"));
        }
        assertEquals(counter, 3);
    }
}
