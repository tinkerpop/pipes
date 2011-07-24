package com.tinkerpop.pipes.branch;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import com.tinkerpop.pipes.transform.OutPipe;
import com.tinkerpop.pipes.util.SingleIterator;
import junit.framework.TestCase;

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
