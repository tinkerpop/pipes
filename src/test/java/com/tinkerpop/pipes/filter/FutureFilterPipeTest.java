package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.IdentityPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.pgm.EdgeVertexPipe;
import com.tinkerpop.pipes.pgm.PropertyFilterPipe;
import com.tinkerpop.pipes.pgm.VertexEdgePipe;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FutureFilterPipeTest extends BaseTest {

    public void testBasicFutureFilter() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        FutureFilterPipe<String> pipe1 = new FutureFilterPipe<String>(new IdentityPipe<String>());
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            pipe1.next();
        }
        assertEquals(counter, 4);
    }

    public void testAdvancedFutureFilter() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "josh");
        FutureFilterPipe<String> pipe1 = new FutureFilterPipe<String>(new CollectionFilterPipe<String>(Arrays.asList("marko", "povel"), ComparisonFilterPipe.Filter.EQUAL));
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh"));
        }
        assertEquals(counter, 2);
    }

    public void testGraphFutureFilter() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex(1);
        Pipe outEPipe = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe inVPipe = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, Vertex> propertyFilterPipe = new PropertyFilterPipe<Vertex, String>("name", "lop", ComparisonFilterPipe.Filter.NOT_EQUAL);
        Pipe<Edge, Edge> futureFilterPipe = new FutureFilterPipe<Edge>(new Pipeline<Edge, Vertex>(inVPipe, propertyFilterPipe));
        Pipe<Vertex, Edge> pipeline = new Pipeline<Vertex, Edge>(outEPipe, futureFilterPipe);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            assertEquals(pipeline.next().getId(), "9");
        }
        assertEquals(counter, 1);
    }

    public void testGraphFutureFilterWithPaths() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex(1);
        Pipe outEPipe = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe inVPipe = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, Vertex> propertyFilterPipe = new PropertyFilterPipe<Vertex, String>("name", "lop", ComparisonFilterPipe.Filter.NOT_EQUAL);
        Pipe<Edge, Edge> futureFilterPipe = new FutureFilterPipe<Edge>(new Pipeline<Edge, Vertex>(inVPipe, propertyFilterPipe));
        Pipe<Vertex, Edge> pipeline = new Pipeline<Vertex, Edge>(outEPipe, futureFilterPipe);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
       // pipeline.enablePath();
        while (pipeline.hasNext()) {
            counter++;
            System.out.println(pipeline.next() + "--->" + pipeline.getPath());
        }
        assertEquals(counter, 1);
    }
}
