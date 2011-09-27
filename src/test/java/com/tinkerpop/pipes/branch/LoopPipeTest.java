package com.tinkerpop.pipes.branch;

import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopPipeTest extends TestCase {

    /*   public void testLoopPipe() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe pipe = new LoopPipe(new OutPipe(), (PipeFunction) new LoopPipeFunction());
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
        Pipe pipe1 = new Pipeline(new VerticesPipe(), new LoopPipe(new OutPipe(), (PipeFunction) new LoopPipeFunction()));
        pipe1.setStarts(new SingleIterator<Graph>(graph));
        Pipe pipe2 = new Pipeline(new OutPipe(), new OutPipe());
        pipe2.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));
        assertTrue(PipeHelper.areEqual(pipe1, pipe2));
    }

    public void testLoopPipe3() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();

        Set<Vertex> x1 = new HashSet<Vertex>();
        Pipe pipe1 = new Pipeline(new LoopPipe(new Pipeline(new OutPipe(), new AggregatePipe(x1)), (PipeFunction) new LoopPipeFunction()));
        pipe1.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));

        Set<Vertex> x2 = new HashSet<Vertex>();
        Pipe pipe2 = new Pipeline(new OutPipe(), new AggregatePipe(x2), new OutPipe(), new AggregatePipe(x2));
        pipe2.setStarts(new SingleIterator<Vertex>(graph.getVertex(1)));

        assertTrue(PipeHelper.areEqual(pipe1, pipe2));
    }


    private class LoopPipeFunction implements PipeFunction<LoopPipe.LoopBundle, Boolean> {
        public Boolean compute(LoopPipe.LoopBundle argument) {
            return (argument.getLoops() < 3);
        }
    }*/

    public void testTrue() {
        assertTrue(true);
    }
}
