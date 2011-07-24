package com.tinkerpop.pipes.branch;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.transform.InVertexPipe;
import com.tinkerpop.pipes.transform.LabelPipe;
import com.tinkerpop.pipes.transform.OutEdgesPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.SingleIterator;
import junit.framework.TestCase;

import java.util.*;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CopySplitPipeTest extends TestCase {

    public void testFairMerge() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe1 = new Pipeline(new OutEdgesPipe(), new InVertexPipe());
        Pipe pipe2 = new Pipeline(new OutEdgesPipe(), new InVertexPipe());
        Pipe pipe3 = new Pipeline(new OutEdgesPipe(), new InVertexPipe());


        CopySplitPipe<Vertex> copySplitPipe = new CopySplitPipe<Vertex>(Arrays.asList(pipe1, pipe2, pipe3));
        FairMergePipe<Vertex> fairMergePipe = new FairMergePipe<Vertex>(copySplitPipe.getPipes());
        copySplitPipe.setStarts(graph.getVertices());
        assertEquals(PipeHelper.counter(fairMergePipe), PipeHelper.counter(graph.getVertices().iterator()) * 3);
    }

    public void testFairMerge2() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe1 = new Pipeline(new OutEdgesPipe("knows"), new LabelPipe());
        Pipe pipe2 = new Pipeline(new OutEdgesPipe("created"), new LabelPipe());


        CopySplitPipe<Vertex> copySplitPipe = new CopySplitPipe<Vertex>(Arrays.asList(pipe1, pipe2));
        FairMergePipe<Vertex> exhaustiveMergePipe = new FairMergePipe<Vertex>(copySplitPipe.getPipes());
        copySplitPipe.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        List list = new ArrayList();
        PipeHelper.fillCollection(exhaustiveMergePipe.iterator(), list);
        assertEquals(list.get(0), "knows");
        assertEquals(list.get(1), "created");
        assertEquals(list.get(2), "knows");
        assertEquals(list.size(), 3);
    }

    public void testExhaustiveMerge() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe1 = new Pipeline(new OutEdgesPipe("knows"), new LabelPipe());
        Pipe pipe2 = new Pipeline(new OutEdgesPipe("created"), new LabelPipe());


        CopySplitPipe<Vertex> copySplitPipe = new CopySplitPipe<Vertex>(Arrays.asList(pipe1, pipe2));
        ExhaustiveMergePipe<Vertex> exhaustiveMergePipe = new ExhaustiveMergePipe<Vertex>(copySplitPipe.getPipes());
        copySplitPipe.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        List list = new ArrayList();
        PipeHelper.fillCollection(exhaustiveMergePipe.iterator(), list);
        assertEquals(list.get(0), "knows");
        assertEquals(list.get(1), "knows");
        assertEquals(list.get(2), "created");
        assertEquals(list.size(), 3);
    }

    public void testBasicNext() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe1 = new Pipeline(new OutEdgesPipe("knows"), new LabelPipe());
        Pipe pipe2 = new Pipeline(new OutEdgesPipe("created"), new LabelPipe());


        CopySplitPipe<Vertex> copySplitPipe = new CopySplitPipe<Vertex>(Arrays.asList(pipe1, pipe2));
        copySplitPipe.setStarts(graph.getVertices());
        int counter = 0;
        Set<Vertex> set = new HashSet<Vertex>();
        for (Vertex vertex : copySplitPipe) {
            counter++;
            set.add(vertex);
        }
        assertEquals(counter, 6);
        assertEquals(set.size(), 6);
    }
}
