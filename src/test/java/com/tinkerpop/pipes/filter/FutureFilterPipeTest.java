package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.transform.BothEdgesPipe;
import com.tinkerpop.pipes.transform.BothVerticesPipe;
import com.tinkerpop.pipes.transform.GraphElementPipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.transform.InVertexPipe;
import com.tinkerpop.pipes.transform.OutEdgesPipe;
import com.tinkerpop.pipes.util.Pipeline;

import java.util.Arrays;
import java.util.Iterator;
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
        FutureFilterPipe<String> pipe1 = new FutureFilterPipe<String>(new CollectionFilterPipe<String>(Arrays.asList("marko", "povel"), FilterPipe.Filter.NOT_EQUAL));
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("peter") || name.equals("josh"));
        }
        assertEquals(counter, 2);
    }

    public void testAdvancedNegativeFutureFilter() {
        List<String> names = Arrays.asList("marko", "peter", "povel", "josh");
        FutureFilterPipe<String> pipe1 = new FutureFilterPipe<String>(new CollectionFilterPipe<String>(Arrays.asList("marko", "povel"), FilterPipe.Filter.NOT_EQUAL), false);
        pipe1.setStarts(names);
        int counter = 0;
        Iterator expected = Arrays.asList("marko", "povel").iterator();
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertEquals(expected.next(), name);
        }
        assertEquals(2, counter);
    }

    public void testGraphFutureFilter() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex(1);
        Pipe outEPipe = new OutEdgesPipe();
        Pipe inVPipe = new InVertexPipe();
        Pipe<Vertex, Vertex> propertyFilterPipe = new PropertyFilterPipe<Vertex, String>("name", "lop", FilterPipe.Filter.EQUAL);
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

    public void testGraphFutureFilterWithRangeFilter() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex(1);
        Pipe outEPipe = new OutEdgesPipe();
        Pipe inVPipe = new InVertexPipe();
        Pipe outE2 = new OutEdgesPipe();
        Pipe<Vertex, Vertex> range = new RangeFilterPipe<Vertex>(1, 2);
        Pipe<Vertex, Vertex> futureFilterPipe =
                new FutureFilterPipe<Vertex>(new Pipeline<Vertex, Edge>(outE2, range));
        Pipe<Vertex, Vertex> pipeline = new Pipeline<Vertex, Vertex>(outEPipe, inVPipe, futureFilterPipe);
        pipeline.setStarts(Arrays.asList(marko, marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            Vertex v = pipeline.next();
            assertEquals("4", v.getId());
        }
        assertEquals(2, counter);
    }

    public void testGraphFutureFilterWithRangeFilter2() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Pipe vertexPipe = new GraphElementPipe<Vertex>(GraphElementPipe.ElementType.VERTEX);
        Pipe bothEPipe = new BothEdgesPipe();
        Pipe bothVPipe = new BothVerticesPipe();
        Pipe outE2 = new OutEdgesPipe();
        Pipe<Vertex, Vertex> range = new RangeFilterPipe<Vertex>(2, 3);
        Pipe<Vertex, Vertex> futureFilterPipe =
                new FutureFilterPipe<Vertex>(new Pipeline<Vertex, Edge>(outE2, range));
        Pipe<Graph, Vertex> pipeline = new Pipeline<Graph, Vertex>(vertexPipe, bothEPipe, bothVPipe, futureFilterPipe);
        pipeline.setStarts(Arrays.asList(graph));
        int counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            Vertex v = pipeline.next();
            assertEquals("1", v.getId());
        }
        assertEquals(6, counter);
    }

    public void testGraphFutureFilterWithPaths() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex(1);
        Pipe outEPipe = new OutEdgesPipe();
        Pipe inVPipe = new InVertexPipe();
        Pipe<Vertex, Vertex> propertyFilterPipe = new PropertyFilterPipe<Vertex, String>("name", "lop", FilterPipe.Filter.EQUAL);
        Pipe<Edge, Edge> futureFilterPipe = new FutureFilterPipe<Edge>(new Pipeline<Edge, Vertex>(inVPipe, propertyFilterPipe));
        Pipe<Vertex, Edge> pipeline = new Pipeline<Vertex, Edge>(outEPipe, futureFilterPipe);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        // pipeline.enablePath();
        while (pipeline.hasNext()) {
            counter++;
            pipeline.next();
            //System.out.println(pipeline.next() + "--->" + pipeline.getPath());
        }
        assertEquals(counter, 1);
    }
}
