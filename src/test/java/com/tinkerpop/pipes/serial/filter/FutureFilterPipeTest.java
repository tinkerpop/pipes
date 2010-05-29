package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.serial.AbstractPipe;
import com.tinkerpop.pipes.serial.Pipe;
import com.tinkerpop.pipes.serial.Pipeline;
import com.tinkerpop.pipes.serial.pgm.*;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FutureFilterPipeTest extends TestCase {

    public void testFutureFilter() {
        List<String> names = Arrays.asList("marko", "peter", "josh", "marko", "jake", "marko", "marko");
        Pipe<String, Integer> pipeA = new CharacterCountPipe();
        Pipe<Integer, Integer> pipeB = new ObjectFilterPipe<Integer>(4, ComparisonFilterPipe.Filter.NOT_EQUALS);
        Pipe<String, String> pipe1 = new FutureFilterPipe<String>(new Pipeline<String, Integer>(pipeA, pipeB));
        Pipeline<String, String> pipeline = new Pipeline<String, String>(pipe1);
        pipeline.setStarts(names);
        int counter = 0;
        while (pipeline.hasNext()) {
            String name = pipeline.next();
            //System.out.println(name);
            counter++;
            assertTrue((name.equals("marko") || name.equals("peter")) && !name.equals("josh") && !name.equals("jake"));
        }
        assertEquals(counter, 5);
    }

    public void testFutureFilterGraph() {
        // ./outE[@label='created']/inV[@name='lop']/../../@name

        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");

        Pipe<Vertex, Edge> pipeA = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Edge, Edge> pipeB = new LabelFilterPipe("created", ComparisonFilterPipe.Filter.EQUALS);
        Pipe<Edge, Vertex> pipeC = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, Vertex> pipeD = new PropertyFilterPipe<Vertex, String>("name", "lop", ComparisonFilterPipe.Filter.EQUALS);
        Pipe<Vertex, Vertex> pipe1 = new FutureFilterPipe<Vertex>(new Pipeline<Vertex, Vertex>(pipeA, pipeB, pipeC, pipeD));
        Pipe<Vertex, String> pipe2 = new PropertyPipe<Vertex, String>("name");
        Pipeline<Vertex, String> pipeline = new Pipeline<Vertex, String>(pipe1, pipe2);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            String name = pipeline.next();
            assertEquals(name,"marko");
            counter++;
        }
        assertEquals(counter, 1);
    }

    public void testComplexFutureFilterGraph() {
        // ./outE[@weight > 0.5]/inV/../../outE/inV/@name

        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");

        Pipe<Vertex, Edge> pipeA = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Edge, Edge> pipeB = new PropertyFilterPipe<Edge, Float>("weight", 0.5f, ComparisonFilterPipe.Filter.GREATER_THAN);
        Pipe<Edge, Vertex> pipeC = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, Vertex> pipe1 = new FutureFilterPipe<Vertex>(new Pipeline<Vertex, Vertex>(pipeA, pipeB, pipeC));
        Pipe<Vertex, Edge> pipe2 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Edge, Vertex> pipe3 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, String> pipe4 = new PropertyPipe<Vertex, String>("name");
        Pipeline<Vertex, String> pipeline = new Pipeline<Vertex, String>(pipe1, pipe2, pipe3, pipe4);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            String name = pipeline.next();
            assertTrue(name.equals("vadas") || name.equals("lop") || name.equals("josh"));
            counter++;
        }
        assertEquals(counter, 3);

    }

    public void testComplexTwoFutureFilterGraph() {
        // ./outE/inV/../../outE/../outE/inV/@name

        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");

        Pipe<Vertex, Edge> pipeA = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Edge, Vertex> pipeB = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, Vertex> pipe1 = new FutureFilterPipe<Vertex>(new Pipeline<Vertex, Vertex>(pipeA, pipeB));
        Pipe<Vertex, Edge> pipeC = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Vertex, Vertex> pipe2 = new FutureFilterPipe<Vertex>(pipeC);
        Pipe<Vertex, Edge> pipe3 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Edge, Vertex> pipe4 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, String> pipe5 = new PropertyPipe<Vertex, String>("name");
        Pipeline<Vertex, String> pipeline = new Pipeline<Vertex, String>(pipe1, pipe2, pipe3, pipe4, pipe5);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            String name = pipeline.next();
            assertTrue(name.equals("vadas") || name.equals("lop") || name.equals("josh"));
            counter++;
        }
        assertEquals(counter, 3);

    }

    private class CharacterCountPipe extends AbstractPipe<String, Integer> {
        protected Integer processNextStart() {
            return this.starts.next().length();
        }
    }

}
