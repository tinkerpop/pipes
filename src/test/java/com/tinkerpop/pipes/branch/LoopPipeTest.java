package com.tinkerpop.pipes.branch;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import com.tinkerpop.pipes.sideeffect.AggregatePipe;
import com.tinkerpop.pipes.transform.OutPipe;
import com.tinkerpop.pipes.transform.VerticesPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.SingleIterator;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopPipeTest extends TestCase {

    public void testLoopPipe() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe = new LoopPipe(new OutPipe(), (PipeClosure) new LoopPipeClosure());
        pipe.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        int counter = 0;
        while (pipe.hasNext()) {
            Object vertex = pipe.next();
            assertTrue(vertex.equals(graph.getVertex(3)) || vertex.equals(graph.getVertex(5)));
            counter++;
        }
        assertEquals(counter, 2);
    }

    public void testLoopPipe2() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe1 = new Pipeline(new VerticesPipe(), new LoopPipe(new OutPipe(), (PipeClosure) new LoopPipeClosure()));
        pipe1.setStarts(new SingleIterator<Graph>(graph));
        Pipe pipe2 = new Pipeline(new OutPipe(), new OutPipe());
        pipe2.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        assertTrue(PipeHelper.areEqual(pipe1, pipe2));
    }

    public void testLoopPipe3() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();

        Set<Vertex> x1 = new HashSet<Vertex>();
        Pipe pipe1 = new Pipeline(new LoopPipe(new Pipeline(new OutPipe(), new AggregatePipe(x1)), (PipeClosure) new LoopPipeClosure()));
        pipe1.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));

        Set<Vertex> x2 = new HashSet<Vertex>();
        Pipe pipe2 = new Pipeline(new OutPipe(), new AggregatePipe(x2), new OutPipe(), new AggregatePipe(x2));
        pipe2.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));

        assertTrue(PipeHelper.areEqual(pipe1, pipe2));
    }


    private class LoopPipeClosure implements PipeClosure<Boolean, LoopPipe> {
        LoopPipe pipe;

        public Boolean compute(Object... parameters) {
            LoopPipe.LoopBundle loopBundle = (LoopPipe.LoopBundle) parameters[0];
            return (loopBundle.getLoops() < 3);
        }

        public void setPipe(LoopPipe hostPipe) {
            this.pipe = hostPipe;
        }
    }
}
